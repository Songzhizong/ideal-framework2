package cn.idealframework2.event.rabbit;

import cn.idealframework2.event.DirectEventPublisher;
import cn.idealframework2.event.JsonStringEventSupplier;
import cn.idealframework2.lang.CollectionUtils;
import cn.idealframework2.lang.StringUtils;
import org.springframework.amqp.core.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @author 宋志宗 on 2022/9/29
 */
public class AmqpEventPublisher implements DirectEventPublisher {
  private final AmqpTemplate amqpTemplate;

  public AmqpEventPublisher(@Nonnull AmqpAdmin amqpAdmin,
                            @Nonnull AmqpTemplate amqpTemplate) {
    this.amqpTemplate = amqpTemplate;
  }

  @Override
  public void directPublish(@Nullable Collection<JsonStringEventSupplier> suppliers) {
    if (CollectionUtils.isEmpty(suppliers)) {
      return;
    }
    for (JsonStringEventSupplier supplier : suppliers) {
      String eventJsonString = supplier.eventJsonString();
      String topic = supplier.topic();
      String exchange = supplier.exchange();
      if (StringUtils.isBlank(exchange)) {
        exchange = RabbitEventUtils.defaultExchange();
      } else {
        exchange = RabbitEventUtils.formatExchange(exchange);
      }
      byte[] originalBytes = eventJsonString.getBytes(StandardCharsets.UTF_8);
      Message message = MessageBuilder.withBody(originalBytes).build();
      MessageProperties properties = message.getMessageProperties();
      properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
      amqpTemplate.send(exchange, topic, message);
    }
  }
}
