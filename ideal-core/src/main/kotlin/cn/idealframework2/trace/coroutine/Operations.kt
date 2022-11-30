package cn.idealframework2.trace.coroutine

import cn.idealframework2.trace.OperationLog

/**
 * @author 宋志宗 on 2022/9/24
 */
@Suppress("unused")
object Operations {

  suspend inline fun details(block: () -> String) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    operationLog.details = block.invoke()
  }

  suspend inline fun failure(block: () -> String) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    operationLog.isSuccess = false
    operationLog.message = block.invoke()
  }

  /** 设置变更前的信息 */
  suspend inline fun before(block: () -> String) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    operationLog.before = block.invoke()
  }

  /** 设置变更后的信息 */
  suspend inline fun after(block: () -> String) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    operationLog.after = block.invoke()
  }

  suspend inline fun log(block: (OperationLog) -> Unit) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    block.invoke(operationLog)
  }
}
