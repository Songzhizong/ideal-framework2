package cn.idealframework2.cache.coroutine

/**
 * @author 宋志宗 on 2022/8/15
 */
interface RedisCache<K : Any, V : Any> {

  /**
   * 从缓存中获取对象, 如果缓存中没有则返回null
   *
   * @param key 缓存key
   * @return 缓存值
   */
  suspend fun getIfPresent(key: K): V?

  /**
   * 尝试从缓存中获取对象, 如果缓存中没有则调用回退方法获取并写入缓存
   *
   * @param key      缓存key
   * @param block 缓存中不包含指定的key时调用此函数获取对象
   * @return 缓存对象
   */
  suspend fun get(key: K, block: suspend (K) -> V?): V?

  /**
   * 写入缓存
   *
   * @param key   缓存键
   * @param value 缓存值
   */
  suspend fun put(key: K, value: V)

  /**
   * 批量写入缓存
   *
   * @param map 缓存键值对
   */
  suspend fun putAll(map: Map<K, V>)

  /**
   * 移除缓存
   *
   * @param key 缓存键
   */
  suspend fun invalidate(key: K)


  /**
   * 批量移除缓存
   *
   * @param keys 缓存键列表
   */
  suspend fun invalidateAll(keys: Iterable<K>)
}
