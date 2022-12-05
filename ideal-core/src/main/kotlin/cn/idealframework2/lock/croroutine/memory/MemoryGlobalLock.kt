package cn.idealframework2.lock.croroutine.memory

import cn.idealframework2.lock.croroutine.GlobalLock
import com.github.benmanes.caffeine.cache.Caffeine
import java.time.Duration

/**
 * @author 宋志宗 on 2022/12/5
 */
class MemoryGlobalLock(
  private val lockKey: String,
  private val timeoutMillis: Long
) : GlobalLock {
  companion object {
    private val cache = Caffeine.newBuilder()
      .expireAfterAccess(Duration.ofDays(30))
      .maximumSize(1000)
      .build<String, LockValue>()
  }

  override suspend fun tryLock(certificate: String): Boolean {
    val expireTimestamp = System.currentTimeMillis() + timeoutMillis
    synchronized(MemoryGlobalLock::class.java) {
      val value = cache.get(lockKey) {
        LockValue(certificate, expireTimestamp)
      }
      val flag = certificate == value.certificate
        || System.currentTimeMillis() > value.expireTimestamp
      if (flag) {
        cache.put(lockKey, LockValue(certificate, expireTimestamp))
      }
      return flag

    }
  }

  override suspend fun renewal() {
    val expireTimestamp = System.currentTimeMillis() + timeoutMillis
    synchronized(MemoryGlobalLock::class.java) {
      val value = cache.getIfPresent(lockKey) ?: return
      cache.put(lockKey, LockValue(value.certificate, expireTimestamp))
    }
  }

  override suspend fun unlock(certificate: String) {
    synchronized(MemoryGlobalLock::class.java) {
      val value = cache.getIfPresent(lockKey) ?: return
      if (value.certificate == certificate) {
        cache.invalidate(lockKey)
        return
      }
      if (System.currentTimeMillis() > value.expireTimestamp) {
        cache.invalidate(lockKey)
        return
      }
    }
  }

  override suspend fun forceUnlock() {
    cache.invalidate(lockKey)
  }


  data class LockValue(val certificate: String, val expireTimestamp: Long)
}
