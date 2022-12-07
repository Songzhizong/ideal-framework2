package cn.idealframework2.event.rabbit;

import cn.idealframework2.event.EventExchangeDeclarer;
import cn.idealframework2.event.EventListener;
import cn.idealframework2.event.EventListenerRegistrar;
import cn.idealframework2.event.EventListenerRegistry;
import cn.idealframework2.idempotent.IdempotentHandler;
import cn.idealframework2.idempotent.Idempotentable;
import cn.idealframework2.json.JsonUtils;
import cn.idealframework2.lang.StringUtils;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author 宋志宗 on 2022/9/29
 */
public class RabbitEventListenerManager implements ChannelAwareMessageListener, EventListenerRegistry, SmartInitializingSingleton {
  private static final Logger log = LoggerFactory.getLogger(RabbitEventListenerManager.class);
  private final ConcurrentMap<String, RabbitEventListener<?>> listenerMap = new ConcurrentHashMap<>();
  private final ConcurrentMap<String, TopicExchange> exchangeMap = new ConcurrentHashMap<>();
  /** 启用此配置则为监听器创建随机名称的队列, 并在程序关闭时删除队列 */
  private final boolean temporary;
  private final String queuePrefix;
  private final AmqpAdmin amqpAdmin;
  private final TopicExchange defaultExchange;
  private final IdempotentHandler idempotentHandler;
  private final ApplicationContext applicationContext;
  private final SingletonBeanRegistry singletonBeanRegistry;

  public RabbitEventListenerManager(boolean temporary,
                                    @Nonnull String queuePrefix,
                                    @Nonnull AmqpAdmin amqpAdmin,
                                    @Nonnull IdempotentHandler idempotentHandler,
                                    @Nonnull ApplicationContext applicationContext,
                                    @Nonnull SingletonBeanRegistry singletonBeanRegistry) {
    this.temporary = temporary;
    this.queuePrefix = queuePrefix;
    this.amqpAdmin = amqpAdmin;
    this.defaultExchange = new TopicExchange(RabbitEventUtils.defaultExchange());
    this.idempotentHandler = idempotentHandler;
    this.applicationContext = applicationContext;
    this.singletonBeanRegistry = singletonBeanRegistry;
    declareExchange(amqpAdmin, applicationContext);
  }

  private static void declareExchange(@Nonnull AmqpAdmin amqpAdmin, @Nonnull ApplicationContext applicationContext) {
    Map<String, EventExchangeDeclarer> beansOfType = applicationContext.getBeansOfType(EventExchangeDeclarer.class);
    Set<String> exchanges = new HashSet<>();
    beansOfType.forEach((n, d) -> exchanges.addAll(d.exchanges()));
    TopicExchange defaultExchange = new TopicExchange(RabbitEventUtils.defaultExchange());
    amqpAdmin.declareExchange(defaultExchange);
    log.info("Declare exchange: {}", defaultExchange.getName());
    for (String exchange : exchanges) {
      TopicExchange topicExchange = new TopicExchange(exchange);
      amqpAdmin.declareExchange(topicExchange);
      log.info("Declare exchange: {}", topicExchange.getName());
    }
  }

  @Override
  public void onMessage(@Nullable Message message, @Nullable Channel channel) throws Exception {
    if (channel == null) {
      log.error("Channel is null");
      return;
    }
    if (message == null) {
      log.error("Message is null");
      return;
    }
    MessageProperties messageProperties = message.getMessageProperties();
    long deliveryTag = messageProperties.getDeliveryTag();
    String consumerQueue = messageProperties.getConsumerQueue();
    RabbitEventListener<?> listener = listenerMap.get(consumerQueue);
    if (listener == null) {
      log.error("找不到对列监听器: " + consumerQueue);
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (Throwable t) {
        // ignore
      }
      channel.basicNack(deliveryTag, false, true);
      return;
    }
    byte[] body = message.getBody();
    String value = new String(body, StandardCharsets.UTF_8);
    if (StringUtils.isBlank(value) || value.charAt(0) != '{') {
      log.warn("消息处理失败, 非json结构: " + message);
      channel.basicAck(deliveryTag, false);
      return;
    }
    try {
      listener.accept(value);
      channel.basicAck(deliveryTag, false);
    } catch (Throwable throwable) {
      log.info("消息交付出现异常: ", throwable);
      // 消费出现异常时线程睡眠1秒在nack, 防止死循环导致的资源浪费
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (Throwable t) {
        // ignore
      }
      channel.basicNack(deliveryTag, false, true);
    }
  }

  @Override
  public <E> void register(@Nonnull String name,
                           @Nonnull Class<E> clazz,
                           @Nonnull Consumer<E> consumer) {
    cn.idealframework2.event.annotation.Event annotation = clazz.getAnnotation(cn.idealframework2.event.annotation.Event.class);
    if (annotation == null) {
      String message = "event 实现类:" + clazz.getName() + " 缺少 @cn.idealframework2.event.annotation.Event 注解";
      log.error(message);
      throw new RuntimeException(message);
    }
    String exchangeName = annotation.exchange();
    TopicExchange exchange;
    if (StringUtils.isBlank(exchangeName)) {
      exchange = defaultExchange;
    } else {
      exchange = exchangeMap.computeIfAbsent(exchangeName, TopicExchange::new);
    }
    String topic = annotation.topic();
    String queueName = queuePrefix + name;
    Queue queue;
    if (temporary) {
      queueName += UUID.randomUUID().toString().replace("-", "");
      queue = new Queue(queueName, false, false, true);
    } else {
      queue = new Queue(queueName, true, false, false);
    }
    RabbitEventQueues.add(queueName);
    RabbitEventListener<E> listener = new RabbitEventListener<>(clazz, queueName, consumer, idempotentHandler);
    listenerMap.put(queueName, listener);
    amqpAdmin.declareQueue(queue);
    amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(topic));
    singletonBeanRegistry.registerSingleton(name, listener);
    log.info("Register event listener: {}  ->  {}", name, topic);
  }

  @Override
  public void afterSingletonsInstantiated() {
    Map<String, EventListenerRegistrar> beansOfType = applicationContext.getBeansOfType(EventListenerRegistrar.class);
    beansOfType.forEach((n, r) -> r.registerEventListener(this));
  }

  public static class RabbitEventListener<E> implements EventListener {
    private final Class<E> clazz;
    private final String queueName;
    private final Consumer<E> consumer;
    private final IdempotentHandler idempotentHandler;

    public RabbitEventListener(@Nonnull Class<E> clazz,
                               @Nonnull String queueName,
                               @Nonnull Consumer<E> consumer,
                               @Nonnull IdempotentHandler idempotentHandler) {
      this.clazz = clazz;
      this.queueName = queueName;
      this.consumer = consumer;
      this.idempotentHandler = idempotentHandler;
    }

    public void accept(@Nonnull String message) {
      E event;
      try {
        event = JsonUtils.parse(message, clazz);
      } catch (Throwable throwable) {
        log.error("将消息反序列化为 {} 出现异常, message = {} e:", clazz.getName(), message, throwable);
        return;
      }
      String key = null;
      if (event instanceof Idempotentable idempotentable) {
        String idempotentKey = idempotentable.idempotentKey();
        if (StringUtils.isNotBlank(idempotentKey)) {
          key = queueName + ":" + idempotentKey;
          boolean idempotent = idempotentHandler.idempotent(key);
          if (!idempotent) {
            return;
          }
        }
      }
      try {
        consumer.accept(event);
      } catch (Throwable throwable) {
        if (key != null) {
          idempotentHandler.release(key);
        }
        throw throwable;
      }
    }
  }

}
