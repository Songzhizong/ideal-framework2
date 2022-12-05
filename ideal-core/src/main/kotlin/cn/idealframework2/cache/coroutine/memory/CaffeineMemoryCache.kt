package cn.idealframework2.cache.coroutine.memory

import com.github.benmanes.caffeine.cache.Cache

/**
 * @author 宋志宗 on 2022/12/5
 */
class CaffeineMemoryCache<K : Any, V : Any>(
  private val cache: Cache<K, V>,
  private val nullCache: Cache<K, Boolean>?
) : MemoryCache<K, V> {

  override suspend fun getIfPresent(key: K): V? {
    return cache.getIfPresent(key)
  }

  override suspend fun get(key: K, block: suspend (K) -> V?): V? {
    val v = cache.getIfPresent(key)
    if (v != null) {
      return v
    }
    if (nullCache != null) {
      if (nullCache.getIfPresent(key) != null) {
        return null
      }
    }
    val invoke = block.invoke(key)
    if (invoke == null) {
      nullCache?.put(key, true)
      return null
    }
    cache.put(key, invoke)
    nullCache?.invalidate(key)
    return invoke
  }

  override suspend fun put(key: K, value: V) {
    nullCache?.invalidate(key)
    cache.put(key, value)
  }

  override suspend fun putAll(map: Map<K, V>) {
    nullCache?.invalidateAll(map.keys)
    cache.putAll(map)
  }

  override suspend fun invalidate(key: K) {
    cache.invalidate(key)
  }

  override suspend fun invalidateAll(keys: Iterable<K>) {
    cache.invalidateAll(keys)
  }
}
