package cn.idealframework2.event.rabbit;

import cn.idealframework2.event.JsonStringEventSupplier;
import cn.idealframework2.event.ReactiveDirectEventPublisher;
import cn.idealframework2.lang.CollectionUtils;
import cn.idealframework2.lang.StringUtils;
import cn.idealframework2.utils.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 宋志宗 on 2022/12/6
 */
public class ReactiveAmqpEventPublisher implements ReactiveDirectEventPublisher {
  private static final Logger log = LoggerFactory.getLogger(ReactiveAmqpEventPublisher.class);
  private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
    Runtime.getRuntime().availableProcessors(), 128,
    60, TimeUnit.SECONDS, new SynchronousQueue<>(),
    BasicThreadFactory.builder().namingPattern("event-publisher-%d").build(),
    (r, executor) -> {
      log.error("异步事件发布线程池资源不足");
      if (!executor.isShutdown()) {
        r.run();
      }
    });
  private final AmqpTemplate amqpTemplate;

  public ReactiveAmqpEventPublisher(@Nonnull AmqpAdmin amqpAdmin,
                                    @Nonnull AmqpTemplate amqpTemplate) {
    this.amqpTemplate = amqpTemplate;
  }

  @Nonnull
  @Override
  public Mono<Boolean> directPublish(@Nullable Collection<JsonStringEventSupplier> suppliers) {
    if (CollectionUtils.isEmpty(suppliers)) {
      return Mono.just(true);
    }
    send(suppliers);
    CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> send(suppliers), EXECUTOR);
    return Mono.fromFuture(future);
  }

  private boolean send(@Nonnull Collection<JsonStringEventSupplier> suppliers) {
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
      properties.setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
      amqpTemplate.send(exchange, topic, message);
    }
    return true;
  }
}
