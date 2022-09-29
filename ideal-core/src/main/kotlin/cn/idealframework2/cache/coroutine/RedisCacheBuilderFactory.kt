package cn.idealframework2.cache.coroutine

import cn.idealframework2.cache.serialize.ValueSerializer
import org.springframework.data.redis.core.ReactiveStringRedisTemplate

/**
 * @author 宋志宗 on 2022/8/15
 */
class RedisCacheBuilderFactory(
  private val prefix: String?,
  private val redisTemplate: ReactiveStringRedisTemplate
) {

  fun <K : Any, V : Any> newBuilder(valueSerializer: ValueSerializer<V>): RedisCacheBuilder<K, V> {
    return RedisCacheBuilder(prefix, valueSerializer, redisTemplate)
  }
}
