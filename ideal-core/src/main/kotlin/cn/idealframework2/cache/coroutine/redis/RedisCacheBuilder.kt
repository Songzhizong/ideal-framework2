package cn.idealframework2.cache.coroutine.redis

import cn.idealframework2.cache.CacheUtils
import cn.idealframework2.cache.coroutine.CacheBuilder
import cn.idealframework2.cache.serialize.KeySerializer
import cn.idealframework2.cache.serialize.StringKeySerializer
import cn.idealframework2.cache.serialize.ValueSerializer
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import java.time.Duration
import kotlin.math.max

/**
 * @author 宋志宗 on 2022/8/15
 */
class RedisCacheBuilder<K : Any, V : Any>(
  private val prefix: String?,
  private val valueSerializer: ValueSerializer<V>,
  private val redisTemplate: ReactiveStringRedisTemplate
) : CacheBuilder<K, V> {
  private var multiLevel = false
  private var memoryCacheSize: Long? = null
  private var memoryCacheTimeout: Duration? = null
  private var cacheNull = false
  private var nullTimeout: Duration? = null
  private var lock = false
  private var lockTimeout: Duration? = null
  private var waitLockTimeout = Duration.ofSeconds(2)
  private var timeoutSeconds = 2592000L
  private var maxTimeoutSeconds: Long? = null
  private var keySerializer: KeySerializer<K> = StringKeySerializer()

  override fun keySerializer(keySerializer: KeySerializer<K>): RedisCacheBuilder<K, V> {
    this.keySerializer = keySerializer
    return this
  }

  /**
   * 缓存null值
   *
   * @param timeout null值的缓存超时时间
   */
  @Suppress("MemberVisibilityCanBePrivate")
  override fun cacheNull(timeout: Duration): RedisCacheBuilder<K, V> {
    this.cacheNull = true
    this.nullTimeout = timeout
    return this
  }

  /**
   * 开启多级缓存, 一级缓存为内存, 二级缓存为redis
   *
   * @param size    内存最大缓存数量
   * @param timeout 内存缓存的超时时间
   */
  override fun multiLevel(size: Long, timeout: Duration): RedisCacheBuilder<K, V> {
    this.multiLevel = true
    this.memoryCacheSize = size
    this.memoryCacheTimeout = timeout
    return this
  }

  /**
   * 启用分布式锁
   *
   * @param lockTimeout      锁的超时时间
   * @param cacheNullTimeout 为了防止缓存穿透, 只有在允许缓存null的前提下才能开启分布式锁, 这个参数用于控制null值的缓存时间
   * @param waitTimeout      等待锁的超时时间, 如果超过这个时间依然没能读取到缓存, 则抛出[cn.idealframework2.cache.ReadCacheException]
   */
  override fun enableLock(
    lockTimeout: Duration,
    cacheNullTimeout: Duration,
    waitTimeout: Duration
  ): RedisCacheBuilder<K, V> {
    this.lock = true
    this.lockTimeout = lockTimeout
    this.waitLockTimeout = waitTimeout
    this.cacheNull(cacheNullTimeout)
    return this
  }

  override fun expireAfterWrite(expireAfterWrite: Duration): RedisCacheBuilder<K, V> {
    this.timeoutSeconds = max(expireAfterWrite.toSeconds(), 1)
    return this
  }

  override fun expireAfterWrite(
    minTimeout: Duration,
    maxTimeout: Duration
  ): RedisCacheBuilder<K, V> {
    this.timeoutSeconds = max(minTimeout.toSeconds(), 1)
    this.maxTimeoutSeconds = max(maxTimeout.toSeconds(), 1)
    return this
  }


  /**
   * 构建redis缓存
   *
   * @author 宋志宗 on 2022/8/15
   */
  override fun build(namespace: String): RedisCache<K, V> {
    val redisPrefix = generateRedisPrefix(namespace)
    val directRedisCache = DirectRedisCache(
      redisPrefix, lock, waitLockTimeout.toMillis(), cacheNull, keySerializer, valueSerializer,
      nullTimeout ?: Duration.ofSeconds(30),
      lockTimeout ?: Duration.ofSeconds(30),
      timeoutSeconds, maxTimeoutSeconds, redisTemplate
    )
    if (!multiLevel) {
      return directRedisCache
    }
    return MultiLevelRedisCache(
      memoryCacheSize ?: 1000,
      memoryCacheTimeout ?: Duration.ofSeconds(30),
      directRedisCache
    )
  }

  private fun generateRedisPrefix(namespace: String): String {
    val prefix = if (this.prefix.isNullOrBlank()) {
      ""
    } else if (this.prefix.endsWith(CacheUtils.CACHE_CONNECTOR)) {
      this.prefix
    } else {
      "${this.prefix}${CacheUtils.CACHE_CONNECTOR}"
    }
    return "$prefix$namespace"
  }
}
