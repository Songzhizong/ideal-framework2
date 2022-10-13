package cn.idealframework2.idempotent.coroutine

import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import java.time.Duration

/**
 * 基于redisTemplate的幂等处理器工厂
 *
 * @author 宋志宗 on 2022/10/13
 */
class RedisTemplateIdempotentHandlerFactory(
  private val prefix: String,
  private val redisTemplate: ReactiveStringRedisTemplate
) : IdempotentHandlerFactory {

  override fun create(namespace: String, expire: Duration): IdempotentHandler {
    val finalPrefix = prefix + namespace
    return RedisTemplateIdempotentHandler(finalPrefix, expire, redisTemplate)
  }
}
