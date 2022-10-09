package cn.idealframework2.trace;

import cn.idealframework2.lang.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.Transient;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 宋志宗 on 2022/9/22
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class TraceContext {
  private transient final AtomicInteger spanIdGenerator = new AtomicInteger(0);
  private final long createMillis = System.currentTimeMillis();
  @Nonnull
  private final String traceId;

  @Nonnull
  private final String spanId;

  /** 用户操作日志 */
  @Nullable
  private OperationLog operationLog = null;

  @Nonnull
  private final transient String logPrefix;

  public TraceContext(@Nonnull String traceId, @Nonnull String spanId) {
    this.traceId = traceId;
    this.spanId = spanId;
    if (StringUtils.isBlank(spanId)) {
      this.logPrefix = "[" + traceId + "] ";
    } else {
      this.logPrefix = "[" + traceId + ", " + spanId + "] ";
    }
  }

  public TraceContext(@Nonnull String traceId) {
    this(traceId, "");
  }

  @Nonnull
  public String nextSpanId() {
    int increment = spanIdGenerator.incrementAndGet();
    if (StringUtils.isBlank(spanId)) {
      return String.valueOf(increment);
    }
    return spanId + "." + increment;
  }

  public long getSurvivalMillis() {
    return System.currentTimeMillis() - createMillis;
  }

  public long getCreateMillis() {
    return createMillis;
  }

  @Nonnull
  public String getTraceId() {
    return traceId;
  }

  @Nonnull
  public String getSpanId() {
    return spanId;
  }

  @Nullable
  public OperationLog getOperationLog() {
    return operationLog;
  }

  public TraceContext setOperationLog(@Nullable OperationLog operationLog) {
    this.operationLog = operationLog;
    return this;
  }

  @Nonnull
  @Transient
  public String getLogPrefix() {
    return logPrefix;
  }
}
