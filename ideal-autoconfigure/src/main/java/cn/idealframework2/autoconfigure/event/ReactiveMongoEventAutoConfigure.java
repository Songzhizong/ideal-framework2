package cn.idealframework2.autoconfigure.event;

import cn.idealframework2.event.ReactiveEventPublisher;
import cn.idealframework2.event.ReactiveTransactionalEventPublisher;
import cn.idealframework2.event.impl.mongo.ReactiveMongoTemplateTransactionalEventPublisher;
import cn.idealframework2.starter.model.event.coroutine.CoroutineEventModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/30
 */
@ConditionalOnExpression("""
  '${ideal-event.transaction.type:mongo}'.equalsIgnoreCase('mongo') && ${ideal-event.transaction.enabled:true}
  """)
@ConditionalOnClass(CoroutineEventModel.class)
public class ReactiveMongoEventAutoConfigure {


  @Bean
  public ReactiveTransactionalEventPublisher reactiveTransactionalEventPublisher(
    @Nonnull ReactiveMongoTemplate template,
    @Nonnull ReactiveEventPublisher publisher
  ) {
    return new ReactiveMongoTemplateTransactionalEventPublisher(template, publisher);
  }
}
