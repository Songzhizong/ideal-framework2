package cn.idealframework2.starter.event.transaction.mongo;

import cn.idealframework2.event.DirectEventPublisher;
import cn.idealframework2.event.ReactiveTransactionalEventPublisher;
import cn.idealframework2.event.TransactionalEventPublisher;
import cn.idealframework2.event.mongo.MongoEventLoop;
import cn.idealframework2.event.mongo.MongoTemplateTransactionalEventPublisher;
import cn.idealframework2.event.mongo.ReactiveMongoTemplateTransactionalEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/9/30
 */
@ConditionalOnClass({MongoTemplate.class})
public class MongoEventAutoConfigure {
  private static final Logger log = LoggerFactory.getLogger(MongoEventAutoConfigure.class);

  @Bean
  @Nullable
  public TransactionalEventPublisher transactionalEventPublisher(@Nullable MongoTemplate mongoTemplate) {
    if (mongoTemplate == null) {
      return null;
    }
    log.info("Usage MongoTemplateTransactionalEventPublisher");
    return new MongoTemplateTransactionalEventPublisher(mongoTemplate);
  }

  @Bean
  @Nullable
  public ReactiveTransactionalEventPublisher reactiveTransactionalEventPublisher(@Nullable ReactiveMongoTemplate reactiveMongoTemplate) {
    if (reactiveMongoTemplate == null) {
      return null;
    }
    log.info("Usage ReactiveMongoTemplateTransactionalEventPublisher");
    return new ReactiveMongoTemplateTransactionalEventPublisher(reactiveMongoTemplate);
  }

  @Bean
  public MongoEventLoop mongoEventLoop(
    @Nullable MongoTemplate mongoTemplate,
    @Nonnull DirectEventPublisher directEventPublisher,
    @Nullable ReactiveMongoTemplate reactiveMongoTemplate
  ) {
    if (mongoTemplate == null && reactiveMongoTemplate == null) {
      throw new RuntimeException("创建MongoEventLoop失败, mongoTemplate与reactiveMongoTemplate均为null");
    }
    return new MongoEventLoop(mongoTemplate, directEventPublisher, reactiveMongoTemplate);
  }
}
