package cn.idealframework2.example.event.coroutine;

import cn.idealframework2.event.BaseEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/9/30
 */
public class TestEvent extends BaseEvent {
  public static final String TOPIC = "idealframework2.example.test";

  @Nullable
  private Long id;

  @Nullable
  private String name;

  @Nonnull
  @Override
  public String getTopic() {
    return TOPIC;
  }

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
