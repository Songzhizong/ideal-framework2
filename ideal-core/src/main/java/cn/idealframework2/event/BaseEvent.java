package cn.idealframework2.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author 宋志宗 on 2022/8/14
 */
@SuppressWarnings("unused")
public abstract class BaseEvent implements IdempotentableEvent {

  /** 事件的唯一id */
  @Nonnull
  private String eventId = UUID.randomUUID().toString().replace("-", "");

  /** 事件产生的毫秒时间戳 */
  private long eventTime = System.currentTimeMillis();

  @Nullable
  @Override
  public String idempotentKey() {
    return getEventId();
  }

  @Nonnull
  public String getEventId() {
    return eventId;
  }

  public void setEventId(@Nonnull String eventId) {
    this.eventId = eventId;
  }

  public long getEventTime() {
    return eventTime;
  }

  public void setEventTime(long eventTime) {
    this.eventTime = eventTime;
  }
}
