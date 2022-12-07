package cn.idealframework2.operation.block;

import cn.idealframework2.operation.OperationLog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/12/7
 */
public final class Operations {
  private static final ThreadLocal<OperationLog> THREAD_LOCAL = new ThreadLocal<>();

  public static void set(@Nonnull OperationLog operationLog) {
    THREAD_LOCAL.set(operationLog);
  }

  @Nullable
  public static OperationLog get() {
    return THREAD_LOCAL.get();
  }

  public static void remove() {
    THREAD_LOCAL.remove();
  }

  private Operations() {
  }
}
