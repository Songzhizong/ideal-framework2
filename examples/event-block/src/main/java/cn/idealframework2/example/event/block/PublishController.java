package cn.idealframework2.example.event.block;

import cn.idealframework2.event.EventPublisher;
import cn.idealframework2.event.TransactionalEventPublisher;
import cn.idealframework2.transmission.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author 宋志宗 on 2022/9/30
 */
@RestController
public class PublishController {
  private final EventPublisher eventPublisher;
  private final TransactionalEventPublisher transactionalEventPublisher;

  public PublishController(EventPublisher eventPublisher,
                           TransactionalEventPublisher transactionalEventPublisher) {
    this.eventPublisher = eventPublisher;
    this.transactionalEventPublisher = transactionalEventPublisher;
  }

  /** 普通发布 */
  @GetMapping("/publish")
  public Result<TestEvent> publish() {
    TestEvent event = new TestEvent();
    event.setId(ThreadLocalRandom.current().nextLong());
    event.setName(UUID.randomUUID().toString());
    eventPublisher.publish(event);
    return Result.success(event);
  }

  /** 事务发布 */
  @GetMapping("/transactional_publish")
  public Result<TestEvent> transactionalPublish() {
    TestEvent event = new TestEvent();
    event.setId(ThreadLocalRandom.current().nextLong());
    event.setName(UUID.randomUUID().toString());
    transactionalEventPublisher.publish(event);
    return Result.success(event);
  }
}
