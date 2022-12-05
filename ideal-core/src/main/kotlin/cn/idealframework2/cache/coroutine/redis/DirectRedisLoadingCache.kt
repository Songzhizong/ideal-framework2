package cn.idealframework2.cache.coroutine.redis


/**
 * @author 宋志宗 on 2022/8/25
 */
class DirectRedisLoadingCache<K : Any, V : Any>(
  private val directRedisCache: DirectRedisCache<K, V>,
  private val block: suspend (K) -> V?
) : RedisLoadingCache<K, V> {

  override suspend fun getIfPresent(key: K): V? {
    return directRedisCache.getIfPresent(key)
  }

  override suspend fun get(key: K): V? {
    return directRedisCache.get(key, block)
  }

  override suspend fun invalidate(key: K) {
    directRedisCache.invalidate(key)
  }

  override suspend fun invalidateAll(keys: Iterable<K>) {
    directRedisCache.invalidateAll(keys)
  }

  override suspend fun putAll(map: Map<K, V>) {
    directRedisCache.putAll(map)
  }

  override suspend fun put(key: K, v: V) {
    directRedisCache.put(key, v)
  }
}
