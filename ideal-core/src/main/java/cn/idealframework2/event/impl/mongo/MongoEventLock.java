package cn.idealframework2.event.impl.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

/**
 * @author 宋志宗 on 2022/8/13
 */
@SuppressWarnings("unused")
@Document(MongoEventLock.DOCUMENT)
@CompoundIndexes({
  @CompoundIndex(name = "uk_lock", def = "{lock:1}", unique = true),
  @CompoundIndex(name = "value", def = "{value:1}"),
})
public class MongoEventLock {
  public static final String DOCUMENT = "ideal_event_lock";

  @Id
  private ObjectId id;

  private String lock;

  private String value;

  @Indexed(name = "created", expireAfterSeconds = 30)
  private LocalDateTime created;

  @Nonnull
  public static MongoEventLock create(@Nonnull String value) {
    MongoEventLock mongoEventLock = new MongoEventLock();
    mongoEventLock.setLock("event_lock");
    mongoEventLock.setValue(value);
    mongoEventLock.setCreated(LocalDateTime.now());
    return mongoEventLock;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getLock() {
    return lock;
  }

  public void setLock(String lock) {
    this.lock = lock;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public LocalDateTime getCreated() {
    return created;
  }

  public void setCreated(LocalDateTime created) {
    this.created = created;
  }
}
