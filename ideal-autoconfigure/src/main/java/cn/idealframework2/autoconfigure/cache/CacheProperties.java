package cn.idealframework2.autoconfigure.cache;

import cn.idealframework2.cache.CacheUtils;
import cn.idealframework2.lang.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/8/13
 */
@ConfigurationProperties("ideal-cache")
public class CacheProperties {
  @Nonnull
  private String prefix = "";

  @JsonIgnore
  private transient volatile String formattedPrefix = null;

  @Nonnull
  public String formattedPrefix() {
    if (formattedPrefix != null) {
      return formattedPrefix;
    }
    synchronized (this) {
      if (formattedPrefix != null) {
        return formattedPrefix;
      }
      String prefix = getPrefix();
      if (StringUtils.isBlank(prefix)) {
        formattedPrefix = "";
        return "";
      }
      if (prefix.endsWith(CacheUtils.CACHE_CONNECTOR)) {
        formattedPrefix = prefix;
        return prefix;
      }
      prefix = prefix + CacheUtils.CACHE_CONNECTOR;
      formattedPrefix = prefix;
      return prefix;
    }
  }

  @Nonnull
  public String formatKey(@Nonnull String key) {
    String prefix = formattedPrefix();
    if (StringUtils.isBlank(prefix)) {
      return key;
    }
    return prefix + key;
  }

  @Nonnull
  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(@Nonnull String prefix) {
    this.prefix = prefix;
  }

}
