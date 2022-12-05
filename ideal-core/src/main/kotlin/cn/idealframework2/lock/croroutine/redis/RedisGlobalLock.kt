package cn.idealframework2.lock.croroutine.redis

import cn.idealframework2.lock.croroutine.GlobalLock
import cn.idealframework2.spring.RedisTemplateUtils
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import java.time.Duration

/**
 * @author 宋志宗 on 2022/11/14
 */
class RedisGlobalLock(
  private val lockKey: String,
  internal var timeout: Duration,
  private val redisTemplate: ReactiveStringRedisTemplate
) : GlobalLock {

  override suspend fun tryLock(certificate: String): Boolean {
    return redisTemplate.opsForValue()
      .setIfAbsent(lockKey, certificate, timeout).awaitSingleOrNull() ?: false
  }

  override suspend fun renewal() {
    redisTemplate.expire(lockKey, timeout).awaitSingleOrNull()
  }

  override suspend fun unlock(certificate: String) {
    RedisTemplateUtils.unlock(redisTemplate, lockKey, certificate).awaitSingleOrNull()
  }

  override suspend fun forceUnlock() {
    redisTemplate.delete(lockKey).awaitSingleOrNull()
  }
}
