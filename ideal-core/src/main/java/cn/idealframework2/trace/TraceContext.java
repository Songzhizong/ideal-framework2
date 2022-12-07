package cn.idealframework2.trace;

import cn.idealframework2.lang.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 宋志宗 on 2022/9/22
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class TraceContext {
  @JsonIgnore
  private transient final AtomicInteger spanIdGenerator = new AtomicInteger(0);
  private final long createMillis = System.currentTimeMillis();
  @Nonnull
  private final String traceId;

  @Nonnull
  private final String spanId;

  public TraceContext(@Nonnull String traceId, @Nonnull String spanId) {
    this.traceId = traceId;
    this.spanId = spanId;
  }

  public TraceContext(@Nonnull String traceId) {
    this(traceId, "0");
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
}
