package cn.idealframework2.cache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2022/12/5
 */
public interface Cache<K, V> {

  /**
   * 从缓存中获取对象, 如果缓存中没有则返回null
   *
   * @param key 缓存key
   * @return 缓存值
   */
  @Nullable
  V getIfPresent(@Nonnull K key);

  /**
   * 尝试从缓存中获取对象, 如果缓存中没有则调用回退方法获取并写入缓存
   *
   * @param key      缓存key
   * @param function 缓存中不包含指定的key时调用此函数获取对象
   * @return 缓存对象
   */
  @Nullable
  V get(@Nonnull K key, @Nonnull Function<K, V> function);

  /**
   * 写入缓存
   *
   * @param key   缓存键
   * @param value 缓存值
   */
  void put(@Nonnull K key, @Nonnull V value);

  /**
   * 批量写入缓存
   *
   * @param map 缓存键值对
   */
  void putAll(@Nonnull Map<K, V> map);

  /**
   * 移除缓存
   *
   * @param key 缓存键
   */
  void invalidate(@Nonnull K key);

  /**
   * 批量移除缓存
   *
   * @param keys 缓存键列表
   */
  void invalidateAll(@Nonnull Iterable<K> keys);
}
