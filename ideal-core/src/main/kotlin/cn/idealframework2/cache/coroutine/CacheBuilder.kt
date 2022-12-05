package cn.idealframework2.cache.coroutine

import cn.idealframework2.cache.serialize.KeySerializer
import java.time.Duration

/**
 * @author 宋志宗 on 2022/12/5
 */
interface CacheBuilder<K : Any, V : Any> {

  fun keySerializer(keySerializer: KeySerializer<K>): CacheBuilder<K, V>

  /**
   * 缓存null值
   *
   * @param timeout null值的缓存超时时间
   */
  fun cacheNull(timeout: Duration): CacheBuilder<K, V>

  /**
   * 开启多级缓存, 一级缓存为内存, 二级缓存为redis
   *
   * @param size    内存最大缓存数量
   * @param timeout 内存缓存的超时时间
   */
  fun multiLevel(
    size: Long = 1000L,
    timeout: Duration = Duration.ofSeconds(30)
  ): CacheBuilder<K, V>

  /**
   * 启用分布式锁
   *
   * @param lockTimeout      锁的超时时间
   * @param cacheNullTimeout 为了防止缓存穿透, 只有在允许缓存null的前提下才能开启分布式锁, 这个参数用于控制null值的缓存时间
   * @param waitTimeout      等待锁的超时时间, 如果超过这个时间依然没能读取到缓存, 则抛出[cn.idealframework2.cache.ReadCacheException]
   */
  fun enableLock(
    lockTimeout: Duration,
    cacheNullTimeout: Duration,
    waitTimeout: Duration = Duration.ofSeconds(2)
  ): CacheBuilder<K, V>

  fun expireAfterWrite(expireAfterWrite: Duration): CacheBuilder<K, V>

  fun expireAfterWrite(minTimeout: Duration, maxTimeout: Duration): CacheBuilder<K, V>

  fun build(namespace: String): Cache<K, V>
}
