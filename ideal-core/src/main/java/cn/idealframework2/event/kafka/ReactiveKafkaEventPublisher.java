package cn.idealframework2.event.kafka;

import cn.idealframework2.event.JsonStringEventSupplier;
import cn.idealframework2.event.ReactiveDirectEventPublisher;
import cn.idealframework2.lang.CollectionUtils;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author 宋志宗 on 2022/12/7
 */
public class ReactiveKafkaEventPublisher implements ReactiveDirectEventPublisher {
  private final KafkaTemplate<String, String> kafkaTemplate;

  public ReactiveKafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }


  @Nonnull
  @Override
  public Mono<Boolean> directPublish(@Nullable Collection<JsonStringEventSupplier> suppliers) {
    if (CollectionUtils.isEmpty(suppliers)) {
      return Mono.just(true);
    }
    return Flux.fromIterable(suppliers)
      .flatMap(supplier -> {
        String eventJsonString = supplier.eventJsonString();
        String exchange = supplier.exchange();
        return Mono.fromFuture(kafkaTemplate.send(exchange, eventJsonString));
      }).collectList().map(rs -> true);
  }
}
