package cn.idealframework2.event.impl.jdbc;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/11/22
 */
public class JdbcEventTemp {

  private long id;

  @Nonnull
  private String eventInfo = "";

  @Nonnull
  private String topic = "";

  @Nonnull
  private String exchange = "";

  private long timestamp;

  public JdbcEventTemp() {
  }

  public JdbcEventTemp(long id,
                       @Nonnull String eventInfo,
                       @Nonnull String topic,
                       @Nonnull String exchange,
                       long timestamp) {
    this.id = id;
    this.eventInfo = eventInfo;
    this.topic = topic;
    this.exchange = exchange;
    this.timestamp = timestamp;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Nonnull
  public String getEventInfo() {
    return eventInfo;
  }

  public void setEventInfo(@Nonnull String eventInfo) {
    this.eventInfo = eventInfo;
  }

  @Nonnull
  public String getTopic() {
    return topic;
  }

  public void setTopic(@Nonnull String topic) {
    this.topic = topic;
  }

  @Nonnull
  public String getExchange() {
    return exchange;
  }

  public void setExchange(@Nonnull String exchange) {
    this.exchange = exchange;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}
