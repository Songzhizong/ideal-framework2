package cn.idealframework2.trace.reactive;

import cn.idealframework2.trace.TraceConstants;
import cn.idealframework2.trace.TraceContext;
import org.springframework.web.server.ServerWebExchange;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

/**
 * @author 宋志宗 on 2022/9/22
 */
public final class TraceExchangeUtils {
  private static final String UNKNOWN = "UNKNOWN";

  private TraceExchangeUtils() {
  }

  public static void putTraceContext(@Nonnull ServerWebExchange exchange,
                                     @Nonnull TraceContext traceContext) {
    Map<String, Object> attributes = exchange.getAttributes();
    attributes.put(TraceConstants.CTX_KEY, traceContext);
  }

  /**
   * 获取链路追踪上下文
   *
   * @return 链路追踪上下文
   * @author 宋志宗 on 2022/5/6
   */
  @Nonnull
  public static Optional<TraceContext> getTraceContext(@Nonnull ServerWebExchange exchange) {
    Object attribute = exchange.getAttribute(TraceConstants.CTX_KEY);
    if (attribute instanceof TraceContext) {
      return Optional.of((TraceContext) attribute);
    }
    return Optional.empty();
  }
}
