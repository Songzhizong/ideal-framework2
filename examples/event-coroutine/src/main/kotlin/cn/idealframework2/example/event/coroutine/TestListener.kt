package cn.idealframework2.example.event.coroutine

import cn.idealframework2.event.coroutine.EventListenerManager
import cn.idealframework2.json.toJsonString
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author 宋志宗 on 2022/9/30
 */
@Configuration
class TestListener(
  private val eventListenerManager: EventListenerManager
) {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(TestListener::class.java)
  }

  @Bean("idealframework2.example.event.coroutine.TestEvent")
  fun listener() = eventListenerManager.listen(
    "idealframework2.example.event.coroutine.TestEvent",
    TestEvent.TOPIC,
    TestEvent::class.java
  ) {
    val jsonString = it.toJsonString()
    log.info("监听到测试事件: {}", jsonString)
  }
}
