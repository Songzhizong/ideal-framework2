package cn.idealframework2.cache.memory;

import cn.idealframework2.cache.Cache;
import cn.idealframework2.cache.CacheBuilder;
import cn.idealframework2.cache.serialize.KeySerializer;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * @author 宋志宗 on 2022/12/5
 */
public class CaffeineCacheBuilder<K, V> implements CacheBuilder<K, V> {

  @Nonnull
  @Override
  public CacheBuilder<K, V> keySerializer(@Nonnull KeySerializer<K> keySerializer) {
    return null;
  }

  @Nonnull
  @Override
  public CacheBuilder<K, V> cacheNull(@Nonnull Duration timeout) {
    return null;
  }

  @Nonnull
  @Override
  public CacheBuilder<K, V> multiLevel(long size, @Nonnull Duration timeout) {
    return null;
  }

  @Nonnull
  @Override
  public CacheBuilder<K, V> enableLock(@Nonnull Duration lockTimeout,
                                       @Nonnull Duration cacheNullTimeout,
                                       @Nonnull Duration waitTimeout) {
    return null;
  }

  @Nonnull
  @Override
  public CacheBuilder<K, V> expireAfterWrite(@Nonnull Duration expireAfterWrite) {
    return null;
  }

  @Nonnull
  @Override
  public CacheBuilder<K, V> expireAfterWrite(@Nonnull Duration minTimeout,
                                             @Nonnull Duration maxTimeout) {
    return null;
  }

  @Nonnull
  @Override
  public Cache<K, V> build(@Nonnull String namespace) {
    return null;
  }
}
