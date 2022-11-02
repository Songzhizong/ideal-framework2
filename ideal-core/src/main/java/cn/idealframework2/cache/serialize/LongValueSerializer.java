package cn.idealframework2.cache.serialize;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/11/2
 */
public class LongValueSerializer implements ValueSerializer<Long> {
  private static final LongValueSerializer INSTANCE = new LongValueSerializer();

  private LongValueSerializer() {
  }

  @Nonnull
  public static LongValueSerializer instance() {
    return INSTANCE;
  }

  @Nonnull
  @Override
  public String serialize(@Nonnull Long value) {
    return String.valueOf(value);
  }

  @Nonnull
  @Override
  public Long deserialize(@Nonnull String value) {
    return Long.valueOf(value);
  }
}
