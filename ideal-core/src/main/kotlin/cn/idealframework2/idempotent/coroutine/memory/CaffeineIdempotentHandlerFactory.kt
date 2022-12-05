package cn.idealframework2.idempotent.coroutine.memory

import cn.idealframework2.idempotent.coroutine.IdempotentHandler
import cn.idealframework2.idempotent.coroutine.IdempotentHandlerFactory
import cn.idealframework2.idempotent.memory.CaffeineIdempotentCacheFactory
import java.time.Duration

/**
 * @author 宋志宗 on 2022/12/6
 */
class CaffeineIdempotentHandlerFactory : IdempotentHandlerFactory {

  override fun create(namespace: String, expire: Duration): IdempotentHandler {
    val millis = expire.toMillis()
    val cache = CaffeineIdempotentCacheFactory.get(millis)
    return CaffeineIdempotentHandler(namespace, cache)
  }
}
