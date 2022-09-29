package cn.idealframework2.trace;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/22
 */
public interface TraceIdGenerator {

  /** 生成traceId */
  @Nonnull
  String generate();

  class Holder {
    private static TraceIdGenerator traceIdGenerator = UUIDTraceIdGenerator.INSTANCE;

    public static TraceIdGenerator get() {
      return traceIdGenerator;
    }

    public static void set(TraceIdGenerator traceIdGenerator) {
      Holder.traceIdGenerator = traceIdGenerator;
    }
  }
}
