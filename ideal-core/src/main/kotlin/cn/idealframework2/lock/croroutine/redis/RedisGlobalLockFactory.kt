package cn.idealframework2.lock.croroutine.redis

import cn.idealframework2.lock.croroutine.GlobalLock
import cn.idealframework2.lock.croroutine.GlobalLockFactory
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import java.time.Duration

/**
 * @author 宋志宗 on 2022/11/14
 */
class RedisGlobalLockFactory(
  private val prefix: String,
  private val redisTemplate: ReactiveStringRedisTemplate
) : GlobalLockFactory {

  override fun get(lock: String, timeout: Duration): GlobalLock {
    val lockKey = if (prefix.isBlank()) {
      lock
    } else {
      "$prefix:$lock"
    }
    return RedisGlobalLock(lockKey, timeout, redisTemplate)
  }

}
