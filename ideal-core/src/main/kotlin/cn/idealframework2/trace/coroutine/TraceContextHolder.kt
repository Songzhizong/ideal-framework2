package cn.idealframework2.trace.coroutine

import cn.idealframework2.trace.TraceContext
import cn.idealframework2.trace.reactive.TraceContextHolder
import kotlinx.coroutines.reactor.awaitSingle

/**
 * @author 宋志宗 on 2022/9/22
 */
object TraceContextHolder {

  suspend fun awaitContext(): TraceContext? {
    return TraceContextHolder.current().awaitSingle().orElse(null)
  }
}
