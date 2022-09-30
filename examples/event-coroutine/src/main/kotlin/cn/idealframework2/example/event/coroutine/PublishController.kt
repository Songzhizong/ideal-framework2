package cn.idealframework2.example.event.coroutine

import cn.idealframework2.event.ReactiveEventPublisher
import cn.idealframework2.event.ReactiveTransactionalEventPublisher
import cn.idealframework2.event.coroutine.publishAndAwait
import cn.idealframework2.transmission.Result
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * @author 宋志宗 on 2022/9/30
 */
@RestController
class PublishController(
  private val eventPublisher: ReactiveEventPublisher,
  private val transactionalEventPublisher: ReactiveTransactionalEventPublisher,
) {

  @GetMapping("/publish")
  suspend fun publish(): Result<TestEvent> {
    val event = TestEvent()
    event.id = ThreadLocalRandom.current().nextLong()
    event.name = UUID.randomUUID().toString()
    eventPublisher.publishAndAwait(event)
    return Result.success(event)
  }

  @GetMapping("/transactional_publish")
  suspend fun transactionalPublish(): Result<TestEvent> {
    val event = TestEvent()
    event.id = ThreadLocalRandom.current().nextLong()
    event.name = UUID.randomUUID().toString()
    transactionalEventPublisher.publishAndAwait(event)
    return Result.success(event)
  }
}
