package cn.idealframework2.trace.coroutine

import cn.idealframework2.trace.OperationLog

/**
 * @author 宋志宗 on 2022/9/24
 */
@Suppress("unused")
object Operations {

  suspend fun details(details: String) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    operationLog.details = details
  }

  suspend inline fun details(block: () -> String) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    operationLog.details = block.invoke()
  }

  suspend fun failure(message: String) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    operationLog.isSuccess = false
    operationLog.message = message
  }

  suspend inline fun failure(block: () -> String) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    operationLog.isSuccess = false
    operationLog.message = block.invoke()
  }

  /** 设置变更前的信息 */
  suspend fun before(before: String) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    operationLog.before = before
  }

  /** 设置变更后的信息 */
  suspend fun after(after: String) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    operationLog.after = after
  }

  suspend fun log(): OperationLog? {
    return TraceContextHolder.awaitContext()?.operationLog
  }

  suspend inline fun log(block: (OperationLog) -> Unit) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    block.invoke(operationLog)
  }
}
