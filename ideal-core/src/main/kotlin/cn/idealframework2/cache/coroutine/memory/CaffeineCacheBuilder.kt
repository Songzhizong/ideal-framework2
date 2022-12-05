package cn.idealframework2.cache.coroutine.memory

import cn.idealframework2.cache.coroutine.Cache
import cn.idealframework2.cache.coroutine.CacheBuilder
import cn.idealframework2.cache.serialize.KeySerializer
import com.github.benmanes.caffeine.cache.Caffeine
import java.time.Duration
import java.util.concurrent.ThreadLocalRandom

/**
 * @author 宋志宗 on 2022/12/5
 */
class CaffeineCacheBuilder<K : Any, V : Any> : CacheBuilder<K, V> {
  private var nullCacheTimeout: Duration? = null
  private var cacheTimeout = Duration.ofDays(30)


  override fun keySerializer(keySerializer: KeySerializer<K>): CacheBuilder<K, V> {
    return this
  }

  override fun cacheNull(timeout: Duration): CacheBuilder<K, V> {
    this.nullCacheTimeout = timeout
    return this
  }

  override fun multiLevel(size: Long, timeout: Duration): CacheBuilder<K, V> {
    return this
  }

  override fun enableLock(
    lockTimeout: Duration,
    cacheNullTimeout: Duration,
    waitTimeout: Duration
  ): CacheBuilder<K, V> {
    return this
  }

  override fun expireAfterWrite(expireAfterWrite: Duration): CacheBuilder<K, V> {
    this.cacheTimeout = expireAfterWrite
    return this
  }

  override fun expireAfterWrite(minTimeout: Duration, maxTimeout: Duration): CacheBuilder<K, V> {
    val minMillis = minTimeout.toMillis()
    val maxMillis = maxTimeout.toMillis()
    val nextLong = ThreadLocalRandom.current().nextLong(minMillis, maxMillis)
    this.cacheTimeout = Duration.ofMillis(nextLong)
    return this
  }

  override fun build(namespace: String): Cache<K, V> {
    val cache = Caffeine
      .newBuilder()
      .expireAfterWrite(cacheTimeout)
      .build<K, V>()
    val nullCache = if (nullCacheTimeout == null) {
      null
    } else {
      Caffeine
        .newBuilder()
        .expireAfterWrite(nullCacheTimeout)
        .build<K, Boolean>()
    }
    return CaffeineMemoryCache(cache, nullCache)
  }
}
