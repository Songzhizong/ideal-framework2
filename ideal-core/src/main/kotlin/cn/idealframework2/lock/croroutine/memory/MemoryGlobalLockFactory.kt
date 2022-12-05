package cn.idealframework2.lock.croroutine.memory

import cn.idealframework2.lock.croroutine.GlobalLock
import cn.idealframework2.lock.croroutine.GlobalLockFactory
import java.time.Duration

/**
 * @author 宋志宗 on 2022/12/5
 */
class MemoryGlobalLockFactory : GlobalLockFactory {

  override fun get(lock: String, timeout: Duration): GlobalLock {
    return MemoryGlobalLock(lock, timeout.toMillis())
  }
}
