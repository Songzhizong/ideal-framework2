package cn.idealframework2.autoconfigure.trace;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/22
 */
@ConfigurationProperties("ideal-trace")
public class TraceProperties {

  /** 系统编码 */
  @Nonnull
  private String system = "";

  @Nonnull
  public String getSystem() {
    return system;
  }

  public TraceProperties setSystem(@Nonnull String system) {
    this.system = system;
    return this;
  }
}
