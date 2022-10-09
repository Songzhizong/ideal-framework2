package cn.idealframework2.spring;

import cn.idealframework2.trace.TraceConstants;
import cn.idealframework2.trace.TraceContext;
import org.springframework.http.HttpHeaders;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * @author 宋志宗 on 2021/7/4
 */
@SuppressWarnings("unused")
public final class WebClientTraceUtils {

  private WebClientTraceUtils() {
  }

  @Nonnull
  public static Consumer<TraceContext> setTraceHeaders(@Nonnull HttpHeaders httpHeaders) {
    return traceContext -> setTraceHeaders(httpHeaders, traceContext);
  }

  public static void setTraceHeaders(@Nonnull HttpHeaders httpHeaders,
                                     @Nonnull TraceContext traceContext) {
    String traceId = traceContext.getTraceId();
    String spanId = traceContext.nextSpanId();
    httpHeaders.set(TraceConstants.TRACE_ID_HEADER_NAME, traceId);
    httpHeaders.set(TraceConstants.TRACE_ID_HEADER_NAME, spanId);
  }
}
