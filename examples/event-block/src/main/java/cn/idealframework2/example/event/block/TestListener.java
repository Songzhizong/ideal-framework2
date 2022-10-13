package cn.idealframework2.example.event.block;

import cn.idealframework2.event.EventListener;
import cn.idealframework2.event.EventListenerManager;
import cn.idealframework2.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 宋志宗 on 2022/9/30
 */
@Configuration
public class TestListener {
  private static final Logger log = LoggerFactory.getLogger(TestEvent.class);
  private final EventListenerManager eventListenerManager;

  public TestListener(EventListenerManager eventListenerManager) {
    this.eventListenerManager = eventListenerManager;
  }

  @Bean("idealframework2.example.event.block.TestEvent")
  public EventListener eventListener() {
    return eventListenerManager.listen("idealframework2.example.event.block.TestEvent",
      TestEvent.TOPIC, TestEvent.class, testEvent -> {
        String jsonString = JsonUtils.toJsonString(testEvent);
        log.info("监听到测试事件: {}", jsonString);
      });
  }
}
