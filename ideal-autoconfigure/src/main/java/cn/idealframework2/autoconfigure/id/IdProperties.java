package cn.idealframework2.autoconfigure.id;

import cn.idealframework2.autoconfigure.id.snowflake.SnowflakeProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/8/14
 */
@ConfigurationProperties("ideal-id")
public class IdProperties {

  @Nonnull
  @NestedConfigurationProperty
  private SnowflakeProperties snowflake = new SnowflakeProperties();

  @Nonnull
  public SnowflakeProperties getSnowflake() {
    return snowflake;
  }

  public void setSnowflake(@Nonnull SnowflakeProperties snowflake) {
    this.snowflake = snowflake;
  }
}
