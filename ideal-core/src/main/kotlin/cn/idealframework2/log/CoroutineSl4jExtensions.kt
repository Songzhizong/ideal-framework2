package cn.idealframework2.log

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext

/**
 * @author 宋志宗 on 2022/11/17
 */
suspend fun <T> withMDCContext(block: suspend CoroutineScope.() -> T): T {
  return withContext(MDCContext(), block)
}
