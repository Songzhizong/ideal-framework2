package cn.idealframework2.event.impl.rabbit;

import cn.idealframework2.event.Event;
import cn.idealframework2.event.EventPublisher;
import cn.idealframework2.event.EventSupplier;
import cn.idealframework2.json.JsonUtils;
import org.springframework.amqp.core.*;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @author 宋志宗 on 2022/9/29
 */
public class AmqpEventPublisher implements EventPublisher {
  private final String exchange;
  private final AmqpTemplate amqpTemplate;

  public AmqpEventPublisher(@Nonnull String exchange,
                            @Nonnull AmqpAdmin amqpAdmin,
                            @Nonnull AmqpTemplate amqpTemplate) {
    this.exchange = exchange;
    this.amqpTemplate = amqpTemplate;
    TopicExchange topicExchange = new TopicExchange(exchange);
    amqpAdmin.declareExchange(topicExchange);
  }


  @Override
  public void publish(@Nonnull Collection<EventSupplier> suppliers) {
    if (suppliers.isEmpty()) {
      return;
    }
    for (EventSupplier supplier : suppliers) {
      Event event = supplier.get();
      String topic = event.getTopic();
      String jsonString = JsonUtils.toJsonString(event);
      byte[] originalBytes = jsonString.getBytes(StandardCharsets.UTF_8);
      Message message = MessageBuilder.withBody(originalBytes).build();
      MessageProperties properties = message.getMessageProperties();
      properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
      amqpTemplate.send(exchange, topic, message);
    }
  }
}
