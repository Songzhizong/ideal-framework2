package cn.idealframework2.cache;

import cn.idealframework2.cache.serialize.KeySerializer;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * @author 宋志宗 on 2022/12/5
 */
public interface CacheBuilder<K, V> {

  @Nonnull
  CacheBuilder<K, V> keySerializer(@Nonnull KeySerializer<K> keySerializer);

  @Nonnull
  CacheBuilder<K, V> cacheNull(@Nonnull Duration timeout);

  @Nonnull
  CacheBuilder<K, V> multiLevel(long size, @Nonnull Duration timeout);

  @Nonnull
  CacheBuilder<K, V> enableLock(@Nonnull Duration lockTimeout,
                                @Nonnull Duration cacheNullTimeout,
                                @Nonnull Duration waitTimeout);

  @Nonnull
  CacheBuilder<K, V> expireAfterWrite(@Nonnull Duration expireAfterWrite);

  @Nonnull
  CacheBuilder<K, V> expireAfterWrite(@Nonnull Duration minTimeout,
                                      @Nonnull Duration maxTimeout);

  @Nonnull
  Cache<K, V> build(@Nonnull String namespace);

}
