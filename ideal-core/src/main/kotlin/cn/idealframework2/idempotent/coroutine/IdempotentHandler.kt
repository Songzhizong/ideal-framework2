package cn.idealframework2.idempotent.coroutine

/**
 * @author 宋志宗 on 2022/10/13
 */
interface IdempotentHandler {

  /**
   * 执行幂等判断
   *
   * @param key 幂等key
   * @return 是否已处理
   */
  suspend fun idempotent(key: String): Boolean

  /**
   * 释放幂等key
   *
   * @param key 幂等key
   */
  suspend fun release(key: String)
}
