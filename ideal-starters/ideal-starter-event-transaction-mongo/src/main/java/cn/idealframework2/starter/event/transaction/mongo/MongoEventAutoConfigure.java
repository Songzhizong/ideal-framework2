package cn.idealframework2.starter.event.transaction.mongo;

import cn.idealframework2.event.DirectEventPublisher;
import cn.idealframework2.event.TransactionalEventPublisher;
import cn.idealframework2.event.impl.mongo.MongoTemplateTransactionalEventPublisher;
import cn.idealframework2.starter.model.event.EventModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/30
 */
@ConditionalOnClass(EventModel.class)
public class MongoEventAutoConfigure {
  private static final Logger log = LoggerFactory.getLogger(MongoEventAutoConfigure.class);

  @Bean
  public TransactionalEventPublisher transactionalEventPublisher(@Nonnull MongoTemplate mongoTemplate,
                                                                 @Nonnull DirectEventPublisher directEventPublisher) {
    log.info("Usage MongoTemplateTransactionalEventPublisher");
    return new MongoTemplateTransactionalEventPublisher(mongoTemplate, directEventPublisher);
  }
}
