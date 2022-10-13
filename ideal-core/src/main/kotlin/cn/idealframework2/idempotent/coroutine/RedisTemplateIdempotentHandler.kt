package cn.idealframework2.idempotent.coroutine

import cn.idealframework2.spring.RedisTemplateUtils
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import java.time.Duration
import java.util.*

/**
 * 基于RedisTemplate的幂等处理器
 *
 * @author 宋志宗 on 2022/10/13
 */
class RedisTemplateIdempotentHandler(
  private val prefix: String,
  private val expire: Duration,
  private val redisTemplate: ReactiveStringRedisTemplate
) : IdempotentHandler {
  private val lockValue = UUID.randomUUID().toString().replace("-", "")

  override suspend fun idempotent(key: String): Boolean {
    val redisKey = "$prefix:$key"
    return redisTemplate.opsForValue()
      .setIfAbsent(redisKey, lockValue, expire).awaitSingleOrNull() == true
  }

  override suspend fun release(key: String) {
    val redisKey = "$prefix:$key"
    RedisTemplateUtils.unlock(redisTemplate, redisKey, lockValue).awaitSingleOrNull()
  }
}
