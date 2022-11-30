package cn.idealframework2.autoconfigure.event.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/8/13
 */
@ConfigurationProperties("ideal-event")
public class EventProperties {

  @Nonnull
  @NestedConfigurationProperty
  private EventRabbitProperties rabbit = new EventRabbitProperties();

  @Nonnull
  @NestedConfigurationProperty
  private EventIdempotentProperties idempotent = new EventIdempotentProperties();

  public enum Type {
    /** 同步 */
    block,
    /** 异步 */
    reactive,
  }

  @Nonnull
  public EventRabbitProperties getRabbit() {
    return rabbit;
  }

  public void setRabbit(@Nonnull EventRabbitProperties rabbit) {
    this.rabbit = rabbit;
  }


  @Nonnull
  public EventIdempotentProperties getIdempotent() {
    return idempotent;
  }

  public void setIdempotent(@Nonnull EventIdempotentProperties idempotent) {
    this.idempotent = idempotent;
  }
}
