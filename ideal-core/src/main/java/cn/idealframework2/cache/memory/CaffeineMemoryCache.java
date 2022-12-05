package cn.idealframework2.cache.memory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2022/12/5
 */
public class CaffeineMemoryCache<K, V> implements MemoryCache<K, V> {

  @Nullable
  @Override
  public V getIfPresent(@Nonnull K key) {
    return null;
  }

  @Nullable
  @Override
  public V get(@Nonnull K key, @Nonnull Function<K, V> function) {
    return null;
  }

  @Override
  public void put(@Nonnull K key, @Nonnull V value) {

  }

  @Override
  public void putAll(@Nonnull Map<K, V> map) {

  }

  @Override
  public void invalidate(@Nonnull K key) {

  }

  @Override
  public void invalidateAll(@Nonnull Iterable<K> keys) {

  }
}
