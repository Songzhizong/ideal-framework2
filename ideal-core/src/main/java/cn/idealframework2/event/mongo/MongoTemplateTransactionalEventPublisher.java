package cn.idealframework2.event.mongo;

import cn.idealframework2.event.Event;
import cn.idealframework2.event.EventSupplier;
import cn.idealframework2.event.TransactionalEventPublisher;
import cn.idealframework2.json.JsonUtils;
import cn.idealframework2.lang.CollectionUtils;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * @author 宋志宗 on 2022/9/29
 */
public class MongoTemplateTransactionalEventPublisher implements TransactionalEventPublisher {
  @SuppressWarnings("AlibabaThreadPoolCreation")
  private final MongoTemplate mongoTemplate;

  public MongoTemplateTransactionalEventPublisher(@Nonnull MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  @SuppressWarnings("DuplicatedCode")
  public void publish(@Nullable Collection<EventSupplier> suppliers) {
    if (CollectionUtils.isEmpty(suppliers)) {
      return;
    }
    long currentTimeMillis = System.currentTimeMillis();
    List<MongoEventTemp> collect = suppliers.stream().map(s -> {
      Event event = s.getEvent();
      Class<? extends Event> clazz = event.getClass();
      cn.idealframework2.event.annotation.Event annotation = clazz.getAnnotation(cn.idealframework2.event.annotation.Event.class);
      if (annotation == null) {
        throw new RuntimeException("event 实现类:" + clazz.getName() + " 缺少 @cn.idealframework2.event.annotation.Event 注解");
      }
      String exchange = annotation.exchange();
      String topic = annotation.topic();

      String jsonString = JsonUtils.toJsonString(event);
      MongoEventTemp temp = new MongoEventTemp();
      temp.setEventInfo(jsonString);
      temp.setTopic(topic);
      temp.setExchange(exchange);
      temp.setTimestamp(currentTimeMillis);
      return temp;
    }).toList();
    mongoTemplate.insert(collect, MongoEventTemp.class);
  }
}
