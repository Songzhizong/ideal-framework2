package cn.idealframework2.autoconfigure.event;

import cn.idealframework2.event.DirectEventPublisher;
import cn.idealframework2.event.TransactionalEventPublisher;
import cn.idealframework2.event.impl.mongo.MongoTemplateTransactionalEventPublisher;
import cn.idealframework2.starter.model.event.EventModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/30
 */
@ConditionalOnExpression("""
  '${ideal-event.transaction.type:mongo}'.equalsIgnoreCase('mongo') && ${ideal-event.transaction.enabled:true}
  """)
@ConditionalOnClass(EventModel.class)
public class MongoEventAutoConfigure {

  @Bean
  public TransactionalEventPublisher transactionalEventPublisher(@Nonnull MongoTemplate mongoTemplate,
                                                                 @Nonnull DirectEventPublisher directEventPublisher) {
    return new MongoTemplateTransactionalEventPublisher(mongoTemplate, directEventPublisher);
  }
}
