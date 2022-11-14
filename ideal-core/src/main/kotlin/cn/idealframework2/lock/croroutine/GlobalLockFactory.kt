package cn.idealframework2.lock.croroutine

import java.time.Duration

/**
 * @author 宋志宗 on 2022/11/14
 */
interface GlobalLockFactory {

  /**
   * 获取全局锁
   *
   * @param lock    锁的全局唯一标识
   * @param timeout 锁的超时时间
   * @author 宋志宗 on 2022/11/14
   */
  fun get(lock: String, timeout: Duration): GlobalLock
}
