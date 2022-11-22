package cn.idealframework2.example.event.block.mongo;

import cn.idealframework2.event.BaseEvent;
import cn.idealframework2.event.annotation.Event;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/9/30
 */
@Event(topic = "idealframework2.example.test")
public class TestEvent extends BaseEvent {

  @Nullable
  private Long id;

  @Nullable
  private String name;

  @Nullable
  public Long getId() {
    return id;
  }

  public void setId(@Nullable Long id) {
    this.id = id;
  }

  @Nullable
  public String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }
}
