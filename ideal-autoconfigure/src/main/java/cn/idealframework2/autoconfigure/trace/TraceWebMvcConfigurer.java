package cn.idealframework2.autoconfigure.trace;

import cn.idealframework2.starter.model.trace.TraceModel;
import cn.idealframework2.trace.block.TraceInterceptor;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/10/9
 */
@ConditionalOnClass({TraceModel.class, Filter.class, HandlerInterceptor.class})
public class TraceWebMvcConfigurer implements WebMvcConfigurer {
  private final TraceInterceptor traceInterceptor;

  public TraceWebMvcConfigurer(TraceInterceptor traceInterceptor) {
    this.traceInterceptor = traceInterceptor;
  }

  @Override
  public void addInterceptors(@Nonnull InterceptorRegistry registry) {
    registry.addInterceptor(traceInterceptor).addPathPatterns("/**");
  }
}
