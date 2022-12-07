package cn.idealframework2.starter.trace.webflux;

import cn.idealframework2.autoconfigure.trace.TraceProperties;
import cn.idealframework2.trace.reactive.TraceFilter;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * @author 宋志宗 on 2022/9/22
 */
public class WebFluxTraceAutoConfigure {

  @Bean
  public TraceFilter traceFilter(@Nonnull TraceProperties properties) {
    Set<String> excludePatterns = properties.getExcludePatterns();
    return new TraceFilter(excludePatterns);
  }
}
