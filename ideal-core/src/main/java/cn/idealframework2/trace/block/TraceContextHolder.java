package cn.idealframework2.trace.block;

import cn.idealframework2.trace.TraceContext;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author 宋志宗 on 2022/10/9
 */
public final class TraceContextHolder {
  private static final ThreadLocal<TraceContext> THREAD_LOCAL = new ThreadLocal<>();

  private TraceContextHolder() {
  }

  @Nonnull
  public static Optional<TraceContext> current() {
    return Optional.ofNullable(THREAD_LOCAL.get());
  }

  public static void release() {
    THREAD_LOCAL.remove();
  }

  static void set(@Nonnull TraceContext traceContext) {
    THREAD_LOCAL.set(traceContext);
  }
}
