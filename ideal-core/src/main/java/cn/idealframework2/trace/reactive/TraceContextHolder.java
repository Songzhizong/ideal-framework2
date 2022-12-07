package cn.idealframework2.trace.reactive;

import cn.idealframework2.exception.InternalServerException;
import cn.idealframework2.trace.TraceConstants;
import cn.idealframework2.trace.TraceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author 宋志宗 on 2022/9/22
 */
public final class TraceContextHolder {
  private static final Logger log = LoggerFactory.getLogger(TraceContextHolder.class);

  @Nonnull
  public static Mono<Optional<TraceContext>> current() {
    return Mono.deferContextual(ctx -> {
      boolean hasKey = ctx.hasKey(TraceConstants.CTX_KEY);
      if (!hasKey) {
        return Mono.just(Optional.empty());
      }
      Object o = ctx.get(TraceConstants.CTX_KEY);
      if (o instanceof TraceContext traceContext) {
        return Mono.just(Optional.of(traceContext));
      }
      String className = o.getClass().getName();
      log.error("从上下文获取到的TraceContext值实际类型为: " + className);
      return Mono.error(new InternalServerException("value not instanceof TraceContext"));
    });
  }

  private TraceContextHolder() {
  }
}
