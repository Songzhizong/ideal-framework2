package cn.idealframework2.cache;

import cn.idealframework2.cache.serialize.ValueSerializer;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/12/5
 */
public interface CacheBuilderFactory {

  @Nonnull
  <K, V> CacheBuilder<K, V> newBuilder(@Nonnull ValueSerializer<V> valueSerializer);
}
