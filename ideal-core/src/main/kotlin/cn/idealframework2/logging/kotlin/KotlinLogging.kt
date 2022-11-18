package cn.idealframework2.logging.kotlin

import cn.idealframework2.logging.kotlin.internal.KLoggerFactory
import org.slf4j.Logger

/**
 * @author 宋志宗 on 2022/11/17
 */
object KotlinLogging {

  fun suspendLogger(func: () -> Unit): SuspendLogger = KLoggerFactory.suspendLogger(func)

  fun suspendLogger(name: String): SuspendLogger = KLoggerFactory.suspendLogger(name)

  fun suspendLogger(clazz: Class<*>): SuspendLogger = KLoggerFactory.suspendLogger(clazz)

  fun suspendLogger(underlyingLogger: Logger): SuspendLogger =
    KLoggerFactory.wrapSuspendKLogger(underlyingLogger)
}
