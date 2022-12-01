package cn.idealframework2.event.impl.rabbit;

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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
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
  private final Set<String> queues = Collections.newSetFromMap(new ConcurrentHashMap<>());
  /** 启用此配置则为监听器创建随机名称的队列, 并在程序关闭时删除队列 */
  private final boolean temporary;
  private final String queuePrefix;
  private final AmqpAdmin amqpAdmin;
  private final TopicExchange defaultExchange;
  private final IdempotentHandler idempotentHandler;
  private final ApplicationContext applicationContext;
  private final SingletonBeanRegistry singletonBeanRegistry;

  public RabbitEventListenerManager(boolean temporary,
                                    @Nonnull String defaultExchange,
                                    @Nonnull String queuePrefix,
                                    @Nonnull AmqpAdmin amqpAdmin,
                                    @Nonnull IdempotentHandler idempotentHandler,
                                    @Nonnull ApplicationContext applicationContext,
                                    @Nonnull SingletonBeanRegistry singletonBeanRegistry) {
    this.temporary = temporary;
    this.queuePrefix = queuePrefix;
    this.amqpAdmin = amqpAdmin;
    this.defaultExchange = new TopicExchange(defaultExchange);
    this.idempotentHandler = idempotentHandler;
    this.applicationContext = applicationContext;
    this.singletonBeanRegistry = singletonBeanRegistry;
  }

  @Override
  public void onMessage(@Nullable Message message, @Nullable Channel channel) throws Exception {
    if (channel == null) {
      log.error("channel is null");
      return;
    }
    if (message == null) {
      log.error("message is null");
      return;
    }
    MessageProperties messageProperties = message.getMessageProperties();
    long deliveryTag = messageProperties.getDeliveryTag();
    String consumerQueue = messageProperties.getConsumerQueue();
    RabbitEventListener<?> listener = listenerMap.get(consumerQueue);
    if (listener == null) {
      log.error("找不到对列监听器: " + consumerQueue);
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
    } catch (IOException e) {
      log.info("消息交付出现异常: ", e);
      // 消费出现异常时线程睡眠1秒在nack, 防止死循环导致的资源浪费
      TimeUnit.SECONDS.sleep(1);
      channel.basicNack(deliveryTag, false, true);
    }
  }

  @Override
  public <E> void register(@Nonnull String name,
                           @Nonnull Class<E> clazz,
                           @Nonnull Consumer<E> consumer) {
    cn.idealframework2.event.annotation.Event annotation = clazz.getAnnotation(cn.idealframework2.event.annotation.Event.class);
    if (annotation == null) {
      throw new RuntimeException("event 实现类:" + clazz.getName() + " 缺少 @cn.idealframework2.event.annotation.Event 注解");
    }
    String exchangeName = annotation.exchange();
    TopicExchange exchange;
    if (StringUtils.isBlank(exchangeName)) {
      exchange = defaultExchange;
    } else {
      exchange = exchangeMap.computeIfAbsent(exchangeName, TopicExchange::new);
    }
    String topic = annotation.topic();
    Queue queue;
    String queueName = queuePrefix + name;
    if (temporary) {
      queueName += UUID.randomUUID().toString().replace("-", "");
      queue = new Queue(queueName, false, false, true);
    } else {
      queue = new Queue(queueName, true, false, false);
    }
    queues.add(queueName);
    RabbitEventListener<E> listener = new RabbitEventListener<>(
      clazz, queueName, consumer, idempotentHandler);
    listenerMap.put(queueName, listener);
    amqpAdmin.declareQueue(queue);
    amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(topic));
    singletonBeanRegistry.registerSingleton(name, listener);
  }

  @Nonnull
  public Set<String> getQueues() {
    return queues;
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
      } catch (Exception e) {
        log.error("将消息反序列化为 " + clazz.getName() +
          " 出现异常, message = " + message + " e:", e);
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
      } catch (Exception e) {
        if (key != null) {
          idempotentHandler.release(key);
        }
        log.warn("事件处理出现异常: ", e);
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
          // ignore
        }
        throw e;
      }
    }
  }

}
