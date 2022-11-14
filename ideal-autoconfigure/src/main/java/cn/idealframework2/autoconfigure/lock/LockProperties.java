package cn.idealframework2.autoconfigure.lock;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/8/13
 */
@ConfigurationProperties("ideal-lock")
public class LockProperties {

  @Nonnull
  private String prefix = "global_lock";

  @Nonnull
  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(@Nonnull String prefix) {
    this.prefix = prefix;
  }
}
