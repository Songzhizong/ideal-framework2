package cn.idealframework2.idempotent.coroutine

import java.time.Duration

/**
 * @author 宋志宗 on 2022/10/13
 */
interface IdempotentHandlerFactory {

  /**
   * 创建幂等处理器
   *
   * @param namespace 名称空间
   * @param expire    过期时间
   * @return 幂等处理器
   */
  fun create(namespace: String, expire: Duration): IdempotentHandler
}
