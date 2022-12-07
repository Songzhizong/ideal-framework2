package cn.idealframework2.event.mongo;

import cn.idealframework2.event.DirectEventPublisher;
import cn.idealframework2.event.JsonStringEventSupplier;
import com.mongodb.client.result.DeleteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2022/12/6
 */
public class MongoEventLoop implements ApplicationRunner, DisposableBean {
  private static final Logger log = LoggerFactory.getLogger(MongoEventLoop.class);
  private static final String LOCK_VALUE = UUID.randomUUID().toString();
  private static final Query QUERY = new Query().limit(200)
    .with(Sort.by(Sort.Order.asc("id")));

  private final AtomicBoolean running = new AtomicBoolean(false);
  @SuppressWarnings("AlibabaThreadPoolCreation")
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  @Nullable
  private final MongoTemplate mongoTemplate;
  private final DirectEventPublisher eventPublisher;
  @Nullable
  private final ReactiveMongoTemplate reactiveMongoTemplate;

  public MongoEventLoop(@Nullable MongoTemplate mongoTemplate,
                        @Nonnull DirectEventPublisher eventPublisher,
                        @Nullable ReactiveMongoTemplate reactiveMongoTemplate) {
    this.mongoTemplate = mongoTemplate;
    this.eventPublisher = eventPublisher;
    this.reactiveMongoTemplate = reactiveMongoTemplate;
  }

  private void start() {
    boolean running = this.running.getAndSet(true);
    if (running) {
      return;
    }
    executor.execute(() -> {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        // ignore
      }
      while (this.running.get()) {
        execWhile();
      }
    });
  }

  private void execWhile() {
    boolean executed = false;
    boolean sleep = true;
    try {
      boolean tryLock = tryLock();
      if (!tryLock) {
        return;
      }
      executed = true;
      List<MongoEventTemp> temps = loadTemps();
      if (temps.isEmpty()) {
        return;
      }
      sleep = false;
      List<JsonStringEventSupplier> collect = temps.stream()
        .map(t -> {
          String eventInfo = t.getEventInfo();
          String topic = t.getTopic();
          String exchange = t.getExchange();
          return new JsonStringEventSupplier(eventInfo, topic, exchange);
        })
        .collect(Collectors.toList());
      eventPublisher.directPublish(collect);
      removeTemps();
    } catch (Throwable throwable) {
      log.info("发布消息出现异常: ", throwable);
    } finally {
      try {
        if (executed) {
          unlock();
        }
        if (sleep) {
          TimeUnit.SECONDS.sleep(1);
        }
      } catch (InterruptedException e) {
        // ignore
      }
    }
  }

  private boolean tryLock() {
    MongoEventLock lock = MongoEventLock.create(LOCK_VALUE);
    if (mongoTemplate != null) {
      try {
        mongoTemplate.insert(lock, MongoEventLock.DOCUMENT);
        return true;
      } catch (Throwable throwable) {
        return false;
      }
    }
    if (reactiveMongoTemplate != null) {
      try {
        reactiveMongoTemplate.insert(lock, MongoEventLock.DOCUMENT).block();
        return true;
      } catch (Throwable throwable) {
        return false;
      }
    }
    return false;
  }

  private void unlock() {
    Criteria criteria = Criteria.where("value").is(LOCK_VALUE);
    Query query = Query.query(criteria);
    if (mongoTemplate != null) {
      DeleteResult result = mongoTemplate.remove(query, MongoEventLock.class);
      long deletedCount = result.getDeletedCount();
      if (deletedCount == 0) {
        log.warn("未能成功释放锁");
      }
      return;
    }
    if (reactiveMongoTemplate != null) {
      reactiveMongoTemplate.remove(query, MongoEventLock.class).blockOptional()
        .ifPresent(res -> {
          long deletedCount = res.getDeletedCount();
          if (deletedCount == 0) {
            log.warn("未能成功释放锁");
          }
        });
      return;
    }
    log.error("MongoTemplate 和 reactiveMongoTemplate 均为null");
  }

  @Nonnull
  private List<MongoEventTemp> loadTemps() {
    if (mongoTemplate != null) {
      return mongoTemplate.find(QUERY, MongoEventTemp.class);
    }
    if (reactiveMongoTemplate != null) {
      return reactiveMongoTemplate.find(QUERY, MongoEventTemp.class)
        .collectList().blockOptional().orElse(Collections.emptyList());
    }
    log.error("MongoTemplate 和 reactiveMongoTemplate 均为null");
    return Collections.emptyList();
  }

  private void removeTemps() {
    if (mongoTemplate != null) {
      mongoTemplate.remove(QUERY, MongoEventTemp.class);
      return;
    }
    if (reactiveMongoTemplate != null) {
      reactiveMongoTemplate.remove(QUERY, MongoEventTemp.class).block();
      return;
    }
    log.error("MongoTemplate 和 reactiveMongoTemplate 均为null");
  }


  @Override
  public void destroy() {
    running.set(false);
    executor.shutdown();
  }

  @Override
  public void run(ApplicationArguments args){
    this.start();
  }
}
