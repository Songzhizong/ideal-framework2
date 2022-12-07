package cn.idealframework2.autoconfigure.trace;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 宋志宗 on 2022/9/22
 */
@ConfigurationProperties("ideal-trace")
public class TraceProperties {

  @Nonnull
  private Set<String> excludePatterns = new HashSet<>();

  @Nonnull
  public Set<String> getExcludePatterns() {
    return excludePatterns;
  }

  public void setExcludePatterns(@Nonnull Set<String> excludePatterns) {
    this.excludePatterns = excludePatterns;
  }
}
