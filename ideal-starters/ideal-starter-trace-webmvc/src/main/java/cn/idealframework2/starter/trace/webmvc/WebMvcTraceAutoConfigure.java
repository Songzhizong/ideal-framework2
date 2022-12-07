package cn.idealframework2.starter.trace.webmvc;

import cn.idealframework2.trace.block.TraceFilter;
import cn.idealframework2.trace.block.TraceInterceptor;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author 宋志宗 on 2022/10/9
 */
@ConditionalOnClass({Filter.class, HandlerInterceptor.class})
public class WebMvcTraceAutoConfigure {

  @Bean
  public TraceFilter traceFilter() {
    return new TraceFilter();
  }

  @Bean
  public TraceInterceptor traceInterceptor() {
    return new TraceInterceptor();
  }
}
