package cn.idealframework2.example.event.block.mongo;

import cn.idealframework2.event.IEventListener;
import cn.idealframework2.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/11/4
 */
@Component
public class TestListener2 implements IEventListener<TestEvent> {
  private static final Logger log = LoggerFactory.getLogger(TestListener2.class);

  @Nonnull
  @Override
  public String listenerName() {
    return "idealframework2.example.event.block.TestEvent.2";
  }

  @Override
  public void execute(@Nonnull TestEvent event) {
    String jsonString = JsonUtils.toJsonString(event);
    log.info("监听到测试事件: {}", jsonString);
  }
}
