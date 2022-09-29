package cn.idealframework2.cache.serialize;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/8/25
 */
public class StringKeySerializer<K> implements KeySerializer<K> {
  @Nonnull
  @Override
  public String serialize(@Nonnull K key) {
    return String.valueOf(key);
  }
}
