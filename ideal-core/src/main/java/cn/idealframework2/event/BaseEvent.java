package cn.idealframework2.event;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * @author 宋志宗 on 2022/8/14
 */
public abstract class BaseEvent implements Event {

  @Nonnull
  private String uuid = UUID.randomUUID().toString().replace("-", "");

  private long eventTime = System.currentTimeMillis();

  @Nonnull
  @Override
  public String getUuid() {
    return uuid;
  }

  @Override
  public long getEventTime() {
    return eventTime;
  }

  public void setUuid(@Nonnull String uuid) {
    this.uuid = uuid;
  }

  public void setEventTime(long eventTime) {
    this.eventTime = eventTime;
  }
}
