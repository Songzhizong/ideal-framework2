package cn.idealframework2.event.impl.mongo;

import cn.idealframework2.event.*;
import cn.idealframework2.json.JsonUtils;
import cn.idealframework2.lang.CollectionUtils;
import cn.idealframework2.lang.Lists;
import com.mongodb.client.result.DeleteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2022/9/29
 */
public class MongoTemplateTransactionalEventPublisher
  implements TransactionalEventPublisher, ApplicationRunner, DisposableBean {
  private static final Logger log = LoggerFactory.getLogger(MongoTemplateTransactionalEventPublisher.class);
  private static final String LOCK_VALUE = UUID.randomUUID().toString();
  private final AtomicBoolean running = new AtomicBoolean(false);
  @SuppressWarnings("AlibabaThreadPoolCreation")
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final MongoTemplate mongoTemplate;
  private final EventPublisher eventPublisher;

  public MongoTemplateTransactionalEventPublisher(@Nonnull MongoTemplate mongoTemplate,
                                                  @Nonnull EventPublisher eventPublisher) {
    this.mongoTemplate = mongoTemplate;
    this.eventPublisher = eventPublisher;
  }

  @Override
  @SuppressWarnings("DuplicatedCode")
  public void publish(@Nonnull Collection<EventSupplier> suppliers) {
    if (CollectionUtils.isEmpty(suppliers)) {
      return;
    }
    long currentTimeMillis = System.currentTimeMillis();
    List<MongoEventTemp> collect = suppliers.stream().map(s -> {
      Event event = s.get();
      String jsonString = JsonUtils.toJsonString(event);
      MongoEventTemp temp = new MongoEventTemp();
      temp.setEventInfo(jsonString);
      temp.setTimestamp(currentTimeMillis);
      return temp;
    }).toList();
    mongoTemplate.insert(collect, MongoEventTemp.class);
  }

  private void start() {
    boolean running = this.running.getAndSet(true);
    if (running) {
      return;
    }
    executor.execute(() -> {
      while (this.running.get()) {
        boolean executed = false;
        boolean sleep = true;
        try {
          try {
            MongoEventLock lock = MongoEventLock.create(LOCK_VALUE);
            mongoTemplate.insert(lock, MongoEventLock.DOCUMENT);
          } catch (Exception e) {
            continue;
          }
          executed = true;
          int limit = 500;
          Query query = new Query().limit(limit)
            .with(Sort.by(Sort.Order.asc("id")));
          List<MongoEventTemp> temps = mongoTemplate.find(query, MongoEventTemp.class);
          if (Lists.isEmpty(temps)) {
            continue;
          }
          sleep = temps.size() < limit;
          List<EventSupplier> collect = temps.stream()
            .map(t -> JsonUtils.parse(t.getEventInfo(), GeneralEvent.class))
            .collect(Collectors.toList());
          eventPublisher.publish(collect);
          mongoTemplate.remove(query, MongoEventTemp.class);
        } catch (Exception e) {
          log.info("发布消息出现异常: ", e);
        } finally {
          try {
            if (executed) {
              Criteria criteria = Criteria.where("value").is(LOCK_VALUE);
              Query query = Query.query(criteria);
              DeleteResult result = mongoTemplate.remove(query, MongoEventLock.class);
              long deletedCount = result.getDeletedCount();
              if (deletedCount == 0) {
                log.warn("未能成功释放锁");
              }
            }
            if (sleep) {
              TimeUnit.SECONDS.sleep(1);
            }
          } catch (InterruptedException e) {
            // ignore
          }
        }
      }
    });
  }

  @Override
  public void destroy() {
    running.set(false);
    executor.shutdown();
  }

  @Override
  public void run(ApplicationArguments args) {
    this.start();
  }
}
