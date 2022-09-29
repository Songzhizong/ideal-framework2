package cn.idealframework2.autoconfigure.event.properties;

/**
 * @author 宋志宗 on 2022/8/13
 */
public class EventTransactionProperties {

  private boolean enabled = true;

  private Type type = Type.mongo;

  public enum Type {
    //    jdbc,
    mongo
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }
}
