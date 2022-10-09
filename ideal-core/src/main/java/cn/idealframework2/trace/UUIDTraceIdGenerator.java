package cn.idealframework2.trace;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * @author 宋志宗 on 2022/9/22
 */
public enum UUIDTraceIdGenerator implements TraceIdGenerator {
  /** 对象实例 */
  INSTANCE;

  @Nonnull
  @Override
  public String generate() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
