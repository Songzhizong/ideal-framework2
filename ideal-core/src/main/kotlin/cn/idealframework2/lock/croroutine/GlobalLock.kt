package cn.idealframework2.lock.croroutine

/**
 * @author 宋志宗 on 2022/11/14
 */
interface GlobalLock {

  suspend fun tryLock(certificate: String): Boolean

  suspend fun renewal()

  suspend fun unlock(certificate: String)

  suspend fun forceUnlock()
}
