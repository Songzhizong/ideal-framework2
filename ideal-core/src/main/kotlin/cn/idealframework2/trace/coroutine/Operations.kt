package cn.idealframework2.trace.coroutine

import cn.idealframework2.trace.OperationLog

/**
 * @author 宋志宗 on 2022/9/24
 */
object Operations {

  suspend fun details(details: String) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    operationLog.details = details
  }

  suspend fun failure(message: String) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    operationLog.isSuccess = false
    operationLog.message = message
  }

  suspend fun before(before: String) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    operationLog.before = before
  }

  suspend fun after(after: String) {
    val context = TraceContextHolder.awaitContext() ?: return
    val operationLog = context.operationLog ?: return
    operationLog.after = after
  }

  suspend fun log(): OperationLog? {
    return TraceContextHolder.awaitContext()?.operationLog
  }
}
