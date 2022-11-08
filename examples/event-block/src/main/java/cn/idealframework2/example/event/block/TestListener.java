package cn.idealframework2.example.event.block;

import cn.idealframework2.event.EventListenerRegistrar;
import cn.idealframework2.event.EventListenerRegistry;
import cn.idealframework2.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/30
 */
@Component
public class TestListener implements EventListenerRegistrar {
  private static final Logger log = LoggerFactory.getLogger(TestEvent.class);

  @Override
  public void register(@Nonnull EventListenerRegistry registry) {

    registry.register(
      "idealframework2.example.event.block.TestEvent",
      TestEvent.class, testEvent -> {
        String jsonString = JsonUtils.toJsonString(testEvent);
        log.info("监听到测试事件: {}", jsonString);
      });

  }
}
