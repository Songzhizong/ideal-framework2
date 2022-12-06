package cn.idealframework2.example.event.coroutine;

import cn.idealframework2.event.BaseEvent;
import cn.idealframework2.event.annotation.Event;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/9/30
 */
@Event(exchange = TestEvent.EXCHANGE, topic = "idealframework2.example.test")
public class TestEvent extends BaseEvent {
  public static final String EXCHANGE = "ideal.example";

  @Nullable
  private Long id;

  @Nullable
  private String name;

  @Nullable
  public Long getId() {
    return id;
  }

  public TestEvent setId(@Nullable Long id) {
    this.id = id;
    return this;
  }

  @Nullable
  public String getName() {
    return name;
  }

  public TestEvent setName(@Nullable String name) {
    this.name = name;
    return this;
  }
}
