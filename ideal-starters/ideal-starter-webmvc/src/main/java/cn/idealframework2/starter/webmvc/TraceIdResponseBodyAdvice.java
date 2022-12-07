package cn.idealframework2.starter.webmvc;

import cn.idealframework2.trace.TraceContext;
import cn.idealframework2.trace.block.TraceContextHolder;
import cn.idealframework2.transmission.BasicResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author 宋志宗 on 2022/11/28
 */
@ControllerAdvice
@ConditionalOnClass({WebMvcConfigurer.class})
public class TraceIdResponseBodyAdvice implements ResponseBodyAdvice<Object> {
  @Override
  public boolean supports(@Nonnull MethodParameter returnType,
                          @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(Object body,
                                @Nonnull MethodParameter returnType,
                                @Nonnull MediaType selectedContentType,
                                @Nonnull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                @Nonnull ServerHttpRequest request,
                                @Nonnull ServerHttpResponse response) {
    // 为响应结果设置traceId
    if (body instanceof BasicResult result) {

      Optional<TraceContext> context = TraceContextHolder.current();
      if (context.isPresent()) {
        String traceId = context.get().getTraceId();
        result.setTraceId(traceId);
        return result;
      }
    }
    return body;
  }
}
