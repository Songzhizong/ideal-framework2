package cn.idealframework2.event.impl.rabbit;

import cn.idealframework2.event.Event;
import cn.idealframework2.event.EventSupplier;
import cn.idealframework2.event.ReactiveEventPublisher;
import cn.idealframework2.json.JsonUtils;
import com.rabbitmq.client.AMQP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private static final Logger log = LoggerFactory.getLogger(ReactorRabbitEventPublisher.class);
  private static final int DELIVERY_MODE_PERSISTENT = 2;
  private final Sender sender;
  private final String defaultExchange;

  public ReactorRabbitEventPublisher(@Nonnull Sender sender, @Nonnull String defaultExchange) {
    this.sender = sender;
    this.defaultExchange = defaultExchange;
    ExchangeSpecification topic = ExchangeSpecification.exchange(defaultExchange).type("topic").durable(true);
    sender.declareExchange(topic).block();
    log.info("declare exchange: " + defaultExchange);
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
        return new OutboundMessage(defaultExchange, topic, properties, originalBytes);
      });
    return sender.sendWithPublishConfirms(messages).collectList().map(l -> true);
  }
}
