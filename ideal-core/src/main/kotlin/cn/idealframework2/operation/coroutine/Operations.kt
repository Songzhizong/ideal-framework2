package cn.idealframework2.operation.coroutine

import cn.idealframework2.operation.OperationLog
import cn.idealframework2.operation.reactive.Operations
import kotlinx.coroutines.reactor.awaitSingle

/**
 * @author 宋志宗 on 2022/9/24
 */
@Suppress("unused")
object Operations {

  suspend fun awaitOperationLog(): OperationLog? {
    return Operations.current().awaitSingle().orElse(null)
  }

  suspend inline fun message(block: () -> String) {
    val operationLog = awaitOperationLog() ?: return
    operationLog.message = block.invoke()
  }

  suspend inline fun details(block: () -> String) {
    val operationLog = awaitOperationLog() ?: return
    operationLog.details = block.invoke()
  }

  suspend inline fun failure(block: () -> String) {
    val operationLog = awaitOperationLog() ?: return
    operationLog.isSuccess = false
    operationLog.message = block.invoke()
  }

  /** 设置变更前的信息 */
  suspend inline fun before(block: () -> String) {
    val operationLog = awaitOperationLog() ?: return
    operationLog.before = block.invoke()
  }

  /** 设置变更后的信息 */
  suspend inline fun after(block: () -> String) {
    val operationLog = awaitOperationLog() ?: return
    operationLog.after = block.invoke()
  }

  suspend inline fun log(block: (OperationLog) -> Unit) {
    val operationLog = awaitOperationLog() ?: return
    block.invoke(operationLog)
  }
}
