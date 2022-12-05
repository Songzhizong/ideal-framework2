package cn.idealframework2.idempotent.coroutine.memory

import cn.idealframework2.idempotent.coroutine.IdempotentHandler
import com.github.benmanes.caffeine.cache.Cache

/**
 * @author 宋志宗 on 2022/12/6
 */
class CaffeineIdempotentHandler(
  private val namespace: String,
  private val cache: Cache<String, Boolean>
) : IdempotentHandler {

  override suspend fun idempotent(key: String): Boolean {
    var flag = false
    cache.get(genCacheKey(key)) {
      flag = true
      true
    }
    return flag
  }

  override suspend fun release(key: String) {
    cache.invalidate(genCacheKey(key))
  }

  private fun genCacheKey(key: String) = "$namespace$:$$key"
}
