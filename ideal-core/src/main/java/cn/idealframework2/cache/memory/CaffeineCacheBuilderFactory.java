package cn.idealframework2.cache.memory;

import cn.idealframework2.cache.CacheBuilder;
import cn.idealframework2.cache.CacheBuilderFactory;
import cn.idealframework2.cache.serialize.ValueSerializer;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/12/5
 */
public class CaffeineCacheBuilderFactory implements CacheBuilderFactory {

  @Nonnull
  @Override
  public <K, V> CacheBuilder<K, V> newBuilder(@Nonnull ValueSerializer<V> valueSerializer) {
    return null;
  }
}
