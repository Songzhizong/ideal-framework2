package cn.idealframework2.event.impl.rabbit;

import cn.idealframework2.event.DirectEventPublisher;
import cn.idealframework2.event.DirectEventSupplier;
import cn.idealframework2.event.Event;
import cn.idealframework2.json.JsonUtils;
import cn.idealframework2.lang.StringUtils;
import org.springframework.amqp.core.*;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @author 宋志宗 on 2022/9/29
 */
public class AmqpEventPublisher implements DirectEventPublisher {
  private final String defaultExchange;
  private final AmqpTemplate amqpTemplate;

  public AmqpEventPublisher(@Nonnull String defaultExchange,
                            @Nonnull AmqpAdmin amqpAdmin,
                            @Nonnull AmqpTemplate amqpTemplate) {
    this.defaultExchange = defaultExchange;
    this.amqpTemplate = amqpTemplate;
    TopicExchange topicExchange = new TopicExchange(defaultExchange);
    amqpAdmin.declareExchange(topicExchange);
  }

  @Override
  public void directPublish(@Nonnull Collection<DirectEventSupplier> suppliers) {
    for (DirectEventSupplier supplier : suppliers) {
      Event event = supplier.event();
      String topic = supplier.topic();
      if (StringUtils.isBlank(topic)) {
        Class<? extends Event> clazz = event.getClass();
        throw new RuntimeException("event 实现类:" + clazz.getName() + " topic为空");
      }
      String exchange = supplier.exchange();
      if (StringUtils.isBlank(exchange)) {
        exchange = defaultExchange;
      }
      String jsonString = JsonUtils.toJsonString(event);
      byte[] originalBytes = jsonString.getBytes(StandardCharsets.UTF_8);
      Message message = MessageBuilder.withBody(originalBytes).build();
      MessageProperties properties = message.getMessageProperties();
      properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
      amqpTemplate.send(exchange, topic, message);
    }
  }
}
