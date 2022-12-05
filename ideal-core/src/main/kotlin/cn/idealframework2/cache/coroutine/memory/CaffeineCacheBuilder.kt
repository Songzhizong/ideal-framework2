package cn.idealframework2.cache.coroutine.memory

import cn.idealframework2.cache.coroutine.Cache
import cn.idealframework2.cache.coroutine.CacheBuilder
import cn.idealframework2.cache.serialize.KeySerializer
import java.time.Duration

/**
 * @author 宋志宗 on 2022/12/5
 */
class CaffeineCacheBuilder<K : Any, V : Any> : CacheBuilder<K, V> {
  override fun keySerializer(keySerializer: KeySerializer<K>): CacheBuilder<K, V> {
    TODO("Not yet implemented")
  }

  override fun cacheNull(timeout: Duration): CacheBuilder<K, V> {
    TODO("Not yet implemented")
  }

  override fun multiLevel(size: Long, timeout: Duration): CacheBuilder<K, V> {
    TODO("Not yet implemented")
  }

  override fun enableLock(
    lockTimeout: Duration,
    cacheNullTimeout: Duration,
    waitTimeout: Duration
  ): CacheBuilder<K, V> {
    TODO("Not yet implemented")
  }

  override fun expireAfterWrite(expireAfterWrite: Duration): CacheBuilder<K, V> {
    TODO("Not yet implemented")
  }

  override fun expireAfterWrite(minTimeout: Duration, maxTimeout: Duration): CacheBuilder<K, V> {
    TODO("Not yet implemented")
  }

  override fun build(namespace: String): Cache<K, V> {
    TODO("Not yet implemented")
  }
}
