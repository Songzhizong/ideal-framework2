package cn.idealframework2.cache.memory;

import cn.idealframework2.cache.CacheBuilder;
import cn.idealframework2.cache.serialize.KeySerializer;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author 宋志宗 on 2022/12/5
 */
public class CaffeineCacheBuilder<K, V> implements CacheBuilder<K, V> {
  @Nullable
  private Duration nullCacheTimeout = null;
  @Nonnull
  private Duration cacheTimeout = Duration.ofDays(30);

  @Nonnull
  @Override
  public CacheBuilder<K, V> keySerializer(@Nonnull KeySerializer<K> keySerializer) {
    return this;
  }

  @Nonnull
  @Override
  public CacheBuilder<K, V> cacheNull(@Nonnull Duration timeout) {
    this.nullCacheTimeout = timeout;
    return this;
  }

  @Nonnull
  @Override
  public CacheBuilder<K, V> multiLevel(long size, @Nonnull Duration timeout) {
    return this;
  }

  @Nonnull
  @Override
  public CacheBuilder<K, V> enableLock(@Nonnull Duration lockTimeout,
                                       @Nonnull Duration cacheNullTimeout,
                                       @Nonnull Duration waitTimeout) {
    return this;
  }

  @Nonnull
  @Override
  public CacheBuilder<K, V> expireAfterWrite(@Nonnull Duration expireAfterWrite) {
    this.cacheTimeout = expireAfterWrite;
    return this;
  }

  @Nonnull
  @Override
  public CacheBuilder<K, V> expireAfterWrite(@Nonnull Duration minTimeout,
                                             @Nonnull Duration maxTimeout) {
    long minMillis = minTimeout.toMillis();
    long maxMillis = maxTimeout.toMillis();
    long nextLong = ThreadLocalRandom.current().nextLong(minMillis, maxMillis);
    this.cacheTimeout = Duration.ofMillis(nextLong);
    return this;
  }

  @Nonnull
  @Override
  public MemoryCache<K, V> build(@Nonnull String namespace) {
    Cache<K, V> cache = Caffeine
      .newBuilder()
      .expireAfterWrite(cacheTimeout)
      .build();
    Cache<K, Boolean> nullCache = null;
    if (nullCacheTimeout != null) {
      nullCache = Caffeine
        .newBuilder()
        .expireAfterWrite(nullCacheTimeout)
        .build();
    }
    return new CaffeineMemoryCache<>(cache, nullCache);
  }
}
