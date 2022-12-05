package cn.idealframework2.cache.coroutine.redis

import cn.idealframework2.cache.CacheUtils
import cn.idealframework2.cache.ReadCacheException
import cn.idealframework2.cache.redis.DirectRedisCache
import cn.idealframework2.cache.serialize.KeySerializer
import cn.idealframework2.cache.serialize.ValueSerializer
import cn.idealframework2.spring.RedisTemplateUtils
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import java.time.Duration
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * @author 宋志宗 on 2022/8/15
 */
class DirectRedisCache<K : Any, V : Any>(
  redisPrefix: String,
  /** 执行回退函数时是否启用分布式锁 */
  private val lock: Boolean,
  /** 等待分布式锁的超时时间, 超过这个时间依然没能获取缓存的话则会抛出读取缓存异常 */
  private val waitLockTimeoutMills: Long,
  /** 是否缓存null值 */
  private val cacheNull: Boolean,
  internal val keySerializer: KeySerializer<K>,
  private val valueSerializer: ValueSerializer<V>,
  /** null值的缓存过期时间 */
  private val nullTimeout: Duration,
  /** 分布式锁的过期时间 */
  private val lockTimeout: Duration,
  /** 缓存过期时间的秒值 */
  private val timeoutSeconds: Long,
  /** 缓存最大过期时间, 如果这个值不为null, 则在默认过期时间和这个值之间取随机数作为缓存的过期时间 */
  private val maxTimeoutSeconds: Long?,
  private val redisTemplate: ReactiveStringRedisTemplate
) : RedisCache<K, V> {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(DirectRedisCache::class.java)
  }

  private val uuid = UUID.randomUUID().toString().replace("-", "")
  private val timeout = Duration.ofSeconds(timeoutSeconds)
  private val finalPrefix: String = if (redisPrefix.isBlank()) {
    ""
  } else if (redisPrefix.endsWith(CacheUtils.CACHE_CONNECTOR)) {
    redisPrefix
  } else {
    "$redisPrefix:"
  }

  override suspend fun getIfPresent(key: K): V? {
    val redisKey = redisKey(keySerializer.serialize(key))
    val value = redisTemplate.opsForValue().get(redisKey).awaitSingleOrNull()
    if (CacheUtils.isNull(value)) {
      return null
    }
    return valueSerializer.deserialize(value!!)
  }

  override suspend fun get(key: K, block: suspend (K) -> V?): V? {
    return get(key, block, null)
  }

  private suspend fun get(key: K, block: suspend (K) -> V?, startLockTimestamp: Long?): V? {
    val redisKey = redisKey(keySerializer.serialize(key))
    val ops = redisTemplate.opsForValue()
    val value = ops.get(redisKey).awaitSingleOrNull()
    if (value != null) {
      if (CacheUtils.isNull(value)) {
        return null
      }
      return valueSerializer.deserialize(value)
    }
    var lockKey: String? = null
    try {
      if (lock) {
        var finalStartLockTimestamp = startLockTimestamp
        if (finalStartLockTimestamp == null) {
          finalStartLockTimestamp = System.currentTimeMillis()
        } else if (System.currentTimeMillis() - finalStartLockTimestamp > waitLockTimeoutMills) {
          log.warn("等待缓存锁超时: $redisKey")
          throw ReadCacheException("读取缓存超时")
        }
        lockKey = lockKey(redisKey)
        val tryLock = ops.setIfAbsent(lockKey, uuid, lockTimeout)
          .awaitSingleOrNull() ?: false
        if (!tryLock) {
          delay(10)
          return get(key, block, finalStartLockTimestamp)
        }
      }
      val invoke = block.invoke(key)
      if (invoke != null) {
        val serialize = valueSerializer.serialize(invoke)
        val timeout = calculateTimeout()
        ops.set(redisKey, serialize, timeout).awaitSingleOrNull()
      } else if (cacheNull) {
        val nullValue = CacheUtils.NULL_VALUE
        ops.set(redisKey, nullValue, nullTimeout).awaitSingleOrNull()
      }
      return invoke
    } finally {
      if (lockKey != null) {
        RedisTemplateUtils.unlock(redisTemplate, lockKey, uuid).awaitSingleOrNull()
      }
    }
  }

  override suspend fun put(key: K, value: V) {
    val redisKey = redisKey(keySerializer.serialize(key))
    val serialize = valueSerializer.serialize(value)
    val timeout = calculateTimeout()
    redisTemplate.opsForValue().set(redisKey, serialize, timeout).awaitSingleOrNull()
  }

  override suspend fun putAll(map: Map<K, V>) {
    coroutineScope {
      map.map { (k, v) -> async { put(k, v) } }.forEach { it.await() }
    }
  }

  override suspend fun invalidate(key: K) {
    val redisKey = redisKey(keySerializer.serialize(key))
    redisTemplate.opsForValue().delete(redisKey).awaitSingleOrNull()
  }

  override suspend fun invalidateAll(keys: Iterable<K>) {
    coroutineScope {
      keys.map { key -> async { invalidate(key) } }.forEach { it.await() }
    }
  }

  private fun redisKey(key: String) = "$finalPrefix$key"

  private fun lockKey(redisKey: String): String = "cache_lock${CacheUtils.CACHE_CONNECTOR}$redisKey"

  private fun calculateTimeout(): Duration {
    return if (maxTimeoutSeconds == null || maxTimeoutSeconds < timeoutSeconds) {
      timeout
    } else {
      val random = ThreadLocalRandom.current().nextLong(timeoutSeconds, maxTimeoutSeconds)
      Duration.ofSeconds(random)
    }
  }
}
