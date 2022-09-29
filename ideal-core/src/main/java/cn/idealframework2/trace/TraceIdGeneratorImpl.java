package cn.idealframework2.trace;

import cn.idealframework2.id.IDGenerator;
import cn.idealframework2.id.IDGeneratorFactory;
import cn.idealframework2.utils.NumberSystemConverter;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/22
 */
public class TraceIdGeneratorImpl implements TraceIdGenerator {
  private final IDGenerator generator;

  public TraceIdGeneratorImpl(@Nonnull IDGeneratorFactory snowflakeFactory) {
    this.generator = snowflakeFactory.getGenerator("traceId");
  }

  @Nonnull
  @Override
  public String generate() {
    long generate = generator.generate();
    return NumberSystemConverter.tenSystemTo(generate, NumberSystemConverter.SYSTEM_36);
  }
}
