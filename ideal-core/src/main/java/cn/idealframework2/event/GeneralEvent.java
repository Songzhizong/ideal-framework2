package cn.idealframework2.event;

import javax.annotation.Nonnull;
import java.beans.Transient;
import java.io.Serial;
import java.util.LinkedHashMap;

/**
 * @author 宋志宗 on 2021/5/1
 */
public class GeneralEvent extends LinkedHashMap<String, Object> implements Event {
  @Serial
  private static final long serialVersionUID = 362498820763181265L;

  @Nonnull
  @Override
  public String getUuid() {
    Object uuid = this.get("uuid");
    if (uuid == null) {
      return "";
    }
    return uuid.toString();
  }

  @Nonnull
  @Override
  @Transient
  public String getTopic() {
    Object topic = this.get("topic");
    if (topic == null) {
      return "";
    }
    return topic.toString();
  }

  @Override
  public long getEventTime() {
    Object eventTime = this.get("eventTime");
    if (eventTime instanceof Number number) {
      return number.longValue();
    }
    return 0;
  }
}
