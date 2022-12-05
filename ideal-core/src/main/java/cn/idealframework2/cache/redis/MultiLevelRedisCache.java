package cn.idealframework2.cache.redis;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2022/9/30
 */
public class MultiLevelRedisCache<K, V> implements RedisCache<K, V> {
  private final Cache<String, V> cache;
  private final DirectRedisCache<K, V> directRedisCache;

  public MultiLevelRedisCache(long maxSize, @Nonnull Duration timeout,
                              @Nonnull DirectRedisCache<K, V> directRedisCache) {
    this.directRedisCache = directRedisCache;
    this.cache = Caffeine.newBuilder().maximumSize(maxSize).expireAfterWrite(timeout).build();
  }


  @Nullable
  @Override
  public V getIfPresent(@Nonnull K key) {
    String serialize = directRedisCache.keySerializer.serialize(key);
    V value = cache.getIfPresent(serialize);
    if (value != null) {
      return value;
    }
    value = directRedisCache.getIfPresent(key);
    if (value != null) {
      cache.put(serialize, value);
    }
    return value;
  }

  @Nullable
  @Override
  public V get(@Nonnull K key, @Nonnull Function<K, V> function) {
    String serialize = directRedisCache.keySerializer.serialize(key);
    return cache.get(serialize, k -> function.apply(key));
  }

  @Override
  public void put(@Nonnull K key, @Nonnull V value) {
    String serialize = directRedisCache.keySerializer.serialize(key);
    cache.put(serialize, value);
    directRedisCache.put(key, value);
  }

  @Override
  public void putAll(@Nonnull Map<K, V> map) {
    map.forEach(this::put);
  }

  @Override
  public void invalidate(@Nonnull K key) {
    String serialize = directRedisCache.keySerializer.serialize(key);
    cache.invalidate(serialize);
    directRedisCache.invalidate(key);
  }

  @Override
  public void invalidateAll(@Nonnull Iterable<K> keys) {
    keys.forEach(this::invalidate);
  }
}
