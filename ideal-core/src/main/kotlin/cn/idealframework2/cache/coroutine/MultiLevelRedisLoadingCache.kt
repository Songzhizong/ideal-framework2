package cn.idealframework2.cache.coroutine

import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import reactor.kotlin.core.publisher.toMono
import java.time.Duration
import java.util.concurrent.CompletableFuture

/**
 * @author 宋志宗 on 2022/9/2
 */
class MultiLevelRedisLoadingCache<K : Any, V : Any>(
  maxSize: Long,
  timeout: Duration,
  private val directRedisCache: DirectRedisCache<K, V>,
  private val block: suspend (K) -> V?
) : RedisLoadingCache<K, V> {
  private val cache: AsyncLoadingCache<K, V>

  init {
    cache = Caffeine.newBuilder()
      .maximumSize(maxSize)
      .expireAfterWrite(timeout)
      .buildAsync { key, _ -> mono { directRedisCache.get(key, block) }.toFuture() }
  }


  override suspend fun getIfPresent(key: K): V? {
    val future = cache.getIfPresent(key)
    if (future != null) {
      val value = future.toMono().awaitSingleOrNull()
      if (value != null) {
        return value
      }
    }
    val value = directRedisCache.getIfPresent(key) ?: return null
    cache.put(key, CompletableFuture.completedFuture(value))
    return value
  }

  override suspend fun get(key: K): V? {
    return cache.get(key).toMono().awaitSingleOrNull()
  }

  override suspend fun put(key: K, v: V) {
    cache.put(key, CompletableFuture.completedFuture(v))
    directRedisCache.put(key, v)
  }

  override suspend fun putAll(map: Map<K, V>) {
    map.forEach { (k, v) ->
      put(k, v)
    }
    directRedisCache.putAll(map)
  }

  override suspend fun invalidate(key: K) {
    throw UnsupportedOperationException("MultiLevelRedisLoadingCache unsupported invalidate")
  }

  override suspend fun invalidateAll(keys: Iterable<K>) {
    throw UnsupportedOperationException("MultiLevelRedisLoadingCache unsupported invalidateAll")
  }

}
