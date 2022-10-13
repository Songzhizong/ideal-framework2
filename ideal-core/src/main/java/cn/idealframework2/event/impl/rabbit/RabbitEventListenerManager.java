package cn.idealframework2.event.impl.rabbit;

import cn.idealframework2.event.Event;
import cn.idealframework2.event.EventListener;
import cn.idealframework2.event.EventListenerManager;
import cn.idealframework2.idempotent.IdempotentHandler;
import cn.idealframework2.json.JsonUtils;
import cn.idealframework2.lang.StringUtils;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author 宋志宗 on 2022/9/29
 */
public class RabbitEventListenerManager implements ChannelAwareMessageListener, EventListenerManager {
  private static final Logger log = LoggerFactory.getLogger(RabbitEventListenerManager.class);
  private final ConcurrentMap<String, RabbitEventListener<?>> listenerMap = new ConcurrentHashMap<>();
  private final Set<String> queues = Collections.newSetFromMap(new ConcurrentHashMap<>());
  /** 启用此配置则为监听器创建随机名称的队列, 并在程序关闭时删除队列 */
  private final boolean temporary;
  private final String queuePrefix;
  private final AmqpAdmin amqpAdmin;
  private final TopicExchange exchange;
  private final IdempotentHandler idempotentHandler;

  public RabbitEventListenerManager(boolean temporary,
                                    @Nonnull String exchange,
                                    @Nonnull String queuePrefix,
                                    @Nonnull AmqpAdmin amqpAdmin,
                                    @Nonnull IdempotentHandler idempotentHandler) {
    this.temporary = temporary;
    this.queuePrefix = queuePrefix;
    this.amqpAdmin = amqpAdmin;
    this.exchange = new TopicExchange(exchange);
    this.idempotentHandler = idempotentHandler;
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
  public <E extends Event> EventListener listen(@Nonnull String name,
                                                @Nonnull String topic,
                                                @Nonnull Class<E> clazz,
                                                @Nonnull Consumer<E> consumer) {
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
    return listener;
  }

  @Nonnull
  public Set<String> getQueues() {
    return queues;
  }

  public static class RabbitEventListener<E extends Event> implements EventListener {
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
      String uuid = event.getUuid();
      String key = queueName + ":" + uuid;
      boolean tryLock = idempotentHandler.idempotent(key);
      if (!tryLock) {
        return;
      }
      try {
        consumer.accept(event);
      } catch (Exception e) {
        idempotentHandler.release(key);
        throw e;
      }
    }
  }

}
