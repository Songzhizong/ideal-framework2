package cn.idealframework2.event.impl.rabbit;

import cn.idealframework2.event.Event;
import cn.idealframework2.event.EventSupplier;
import cn.idealframework2.event.ReactiveEventPublisher;
import cn.idealframework2.json.JsonUtils;
import com.rabbitmq.client.AMQP;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.ExchangeSpecification;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @author 宋志宗 on 2022/4/2
 */
public class ReactorRabbitEventPublisher implements ReactiveEventPublisher {
  private static final Log log = LogFactory.getLog(ReactorRabbitEventPublisher.class);
  private static final int DELIVERY_MODE_PERSISTENT = 2;
  private final Sender sender;
  private final String exchange;

  public ReactorRabbitEventPublisher(@Nonnull Sender sender, @Nonnull String exchange) {
    this.sender = sender;
    this.exchange = exchange;
    ExchangeSpecification topic = ExchangeSpecification.exchange(exchange).type("topic").durable(true);
    sender.declareExchange(topic).block();
    log.info("declare exchange: " + exchange);
  }

  @Nonnull
  @Override
  public Mono<Boolean> publish(@Nonnull Collection<EventSupplier> suppliers) {
    if (suppliers.isEmpty()) {
      return Mono.just(true);
    }
    Flux<OutboundMessage> messages = Flux.fromIterable(suppliers)
      .map(supplier -> {
        Event event = supplier.get();
        String topic = event.getTopic();
        String jsonString = JsonUtils.toJsonStringIgnoreNull(supplier);
        byte[] originalBytes = jsonString.getBytes(StandardCharsets.UTF_8);
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder()
          .deliveryMode(DELIVERY_MODE_PERSISTENT);
        AMQP.BasicProperties properties = builder.build();
        return new OutboundMessage(exchange, topic, properties, originalBytes);
      });
    return sender.sendWithPublishConfirms(messages).collectList().map(l -> true);
  }
}
