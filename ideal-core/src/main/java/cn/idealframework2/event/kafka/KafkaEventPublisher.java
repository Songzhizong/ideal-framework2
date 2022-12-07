package cn.idealframework2.event.kafka;

import cn.idealframework2.event.DirectEventPublisher;
import cn.idealframework2.event.JsonStringEventSupplier;
import cn.idealframework2.lang.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author 宋志宗 on 2022/12/7
 */
public class KafkaEventPublisher implements DirectEventPublisher {
  private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisher.class);
  private final KafkaTemplate<String, String> kafkaTemplate;

  public KafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }


  @Override
  public void directPublish(@Nullable Collection<JsonStringEventSupplier> suppliers) {
    if (CollectionUtils.isEmpty(suppliers)) {
      return;
    }
    List<CompletableFuture<SendResult<String, String>>> futures = suppliers.stream()
      .map(supplier -> {
        String eventJsonString = supplier.eventJsonString();
        String exchange = supplier.exchange();
        return kafkaTemplate.send(exchange, eventJsonString);
      }).toList();
    try {
      for (CompletableFuture<SendResult<String, String>> future : futures) {
        future.get();
      }
    } catch (Throwable throwable) {
      log.warn("发布消息到kafka出现异常: ", throwable);
      throw new RuntimeException(throwable);
    }
  }
}
