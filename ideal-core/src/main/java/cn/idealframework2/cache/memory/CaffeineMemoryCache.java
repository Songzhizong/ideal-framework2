package cn.idealframework2.cache.memory;

import com.github.benmanes.caffeine.cache.Cache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2022/12/5
 */
public class CaffeineMemoryCache<K, V> implements MemoryCache<K, V> {
  @Nonnull
  private final Cache<K, V> cache;
  @Nullable
  private final Cache<K, Boolean> nullCache;

  public CaffeineMemoryCache(@Nonnull Cache<K, V> cache,
                             @Nullable Cache<K, Boolean> nullCache) {
    this.cache = cache;
    this.nullCache = nullCache;
  }

  @Nullable
  @Override
  public V getIfPresent(@Nonnull K key) {
    return cache.getIfPresent(key);
  }

  @Nullable
  @Override
  public V get(@Nonnull K key, @Nonnull Function<K, V> function) {
    return cache.get(key, k -> {
      if (nullCache != null && nullCache.getIfPresent(key) != null) {
        return null;
      }
      V apply = function.apply(key);
      if (nullCache != null) {
        if (apply == null) {
          nullCache.put(key, true);
        } else {
          nullCache.invalidate(key);
        }
      }
      return apply;
    });
  }

  @Override
  public void put(@Nonnull K key, @Nonnull V value) {
    if (nullCache != null) {
      nullCache.invalidate(key);
    }
    cache.put(key, value);
  }

  @Override
  public void putAll(@Nonnull Map<K, V> map) {
    if (nullCache != null) {
      nullCache.invalidateAll(map.keySet());
    }
    cache.putAll(map);
  }

  @Override
  public void invalidate(@Nonnull K key) {
    cache.invalidate(key);
  }

  @Override
  public void invalidateAll(@Nonnull Iterable<K> keys) {
    cache.invalidateAll(keys);
  }
}
