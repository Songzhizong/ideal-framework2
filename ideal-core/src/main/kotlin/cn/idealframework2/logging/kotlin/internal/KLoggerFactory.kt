package cn.idealframework2.logging.kotlin.internal

import cn.idealframework2.logging.kotlin.SuspendLogger
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.spi.LocationAwareLogger

/**
 * @author 宋志宗 on 2022/11/17
 */
internal object KLoggerFactory {

  /** get logger by explicit name */
  internal fun suspendLogger(name: String): SuspendLogger = wrapSuspendKLogger(jLogger(name))

  /** get logger by explicit name */
  internal fun suspendLogger(clazz: Class<*>): SuspendLogger = wrapSuspendKLogger(jLogger(clazz))

  /** get logger for the method, assuming it was declared at the logger file/class */
  internal fun suspendLogger(func: () -> Unit): SuspendLogger =
    suspendLogger(KLoggerNameResolver.name(func))

  /** get a java logger by name */
  private fun jLogger(name: String): Logger = LoggerFactory.getLogger(name)

  private fun jLogger(clazz: Class<*>): Logger = LoggerFactory.getLogger(clazz)

  /** wrap java logger based on location awareness */
  internal fun wrapSuspendKLogger(jLogger: Logger): SuspendLogger =
    if (jLogger is LocationAwareLogger) {
      LocationAwareSuspendLogger(jLogger)
    } else {
      throw UnsupportedOperationException("Unsupported Logger type: ${jLogger.javaClass.name}")
//      LocationIgnorantKLogger(jLogger)
    }
}
