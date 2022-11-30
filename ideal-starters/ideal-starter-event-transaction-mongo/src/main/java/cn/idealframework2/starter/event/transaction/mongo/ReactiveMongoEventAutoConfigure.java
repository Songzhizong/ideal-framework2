package cn.idealframework2.starter.event.transaction.mongo;

import cn.idealframework2.event.ReactiveDirectEventPublisher;
import cn.idealframework2.event.ReactiveTransactionalEventPublisher;
import cn.idealframework2.event.impl.mongo.ReactiveMongoTemplateTransactionalEventPublisher;
import cn.idealframework2.starter.model.event.coroutine.CoroutineEventModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/30
 */
@ConditionalOnClass(CoroutineEventModel.class)
public class ReactiveMongoEventAutoConfigure {
  private static final Logger log = LoggerFactory.getLogger(ReactiveMongoEventAutoConfigure.class);

  @Bean
  public ReactiveTransactionalEventPublisher reactiveTransactionalEventPublisher(
    @Nonnull ReactiveMongoTemplate template,
    @Nonnull ReactiveDirectEventPublisher directReactiveEventPublisher
  ) {
    log.info("Usage ReactiveMongoTemplateTransactionalEventPublisher");
    return new ReactiveMongoTemplateTransactionalEventPublisher(template, directReactiveEventPublisher);
  }
}
