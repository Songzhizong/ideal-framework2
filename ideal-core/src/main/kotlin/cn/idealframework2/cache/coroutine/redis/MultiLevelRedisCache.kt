package cn.idealframework2.cache.coroutine.redis

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import java.time.Duration

/**
 * @author 宋志宗 on 2022/8/15
 */
class MultiLevelRedisCache<K : Any, V : Any>(
  maxSize: Long,
  timeout: Duration,
  private val directRedisCache: DirectRedisCache<K, V>
) : RedisCache<K, V> {
  private val cache: Cache<String, V>

  init {
    cache = Caffeine.newBuilder()
      .maximumSize(maxSize)
      .expireAfterWrite(timeout).build()
  }

  override suspend fun getIfPresent(key: K): V? {
    val serializeKey = directRedisCache.keySerializer.serialize(key)
    var value = cache.getIfPresent(serializeKey)
    if (value != null) {
      return value
    }
    value = directRedisCache.getIfPresent(key)
    if (value != null) {
      cache.put(serializeKey, value)
    }
    return value
  }

  override suspend fun get(key: K, block: suspend (K) -> V?): V? {
    val serializeKey = directRedisCache.keySerializer.serialize(key)
    var value = cache.getIfPresent(serializeKey)
    if (value != null) {
      return value
    }
    value = directRedisCache.get(key, block)
    if (value != null) {
      cache.put(serializeKey, value)
    }
    return value
  }

  override suspend fun put(key: K, value: V) {
    cache.put(directRedisCache.keySerializer.serialize(key), value)
    directRedisCache.put(key, value)
  }

  override suspend fun putAll(map: Map<K, V>) {
    map.forEach { (k, v) ->
      cache.put(directRedisCache.keySerializer.serialize(k), v)
    }
    directRedisCache.putAll(map)
  }

  override suspend fun invalidate(key: K) {
    cache.invalidate(directRedisCache.keySerializer.serialize(key))
    directRedisCache.invalidate(key)
  }

  override suspend fun invalidateAll(keys: Iterable<K>) {
    keys.forEach { cache.invalidate(directRedisCache.keySerializer.serialize(it)) }
    directRedisCache.invalidateAll(keys)
  }
}
