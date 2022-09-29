package cn.idealframework2.event.impl.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/4/1
 */
@Document(MongoEventTemp.DOCUMENT)
public class MongoEventTemp {
  public static final String DOCUMENT = "ideal_event_publish_temp";

  @Id
  private ObjectId id;

  @Nonnull
  private String eventInfo = "";

  private long timestamp;

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  @Nonnull
  public String getEventInfo() {
    return eventInfo;
  }

  public void setEventInfo(@Nonnull String eventInfo) {
    this.eventInfo = eventInfo;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}
