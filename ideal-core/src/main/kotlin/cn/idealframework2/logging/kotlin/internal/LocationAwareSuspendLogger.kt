package cn.idealframework2.logging.kotlin.internal

import cn.idealframework2.logging.kotlin.SuspendLogger
import cn.idealframework2.trace.TraceConstants
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.MDC
import org.slf4j.Marker
import org.slf4j.MarkerFactory
import org.slf4j.helpers.MessageFormatter
import org.slf4j.spi.LocationAwareLogger
import reactor.core.publisher.Mono

/**
 * @author 宋志宗 on 2022/11/17
 */
internal class LocationAwareSuspendLogger(
  override val underlyingLogger: LocationAwareLogger
) : SuspendLogger {
  @Suppress("SpellCheckingInspection")
  companion object {
    private val fqcn: String = LocationAwareSuspendLogger::class.java.name
    private val ENTRY = MarkerFactory.getMarker("ENTRY")
    private val EXIT = MarkerFactory.getMarker("EXIT")
    private val THROWING = MarkerFactory.getMarker("THROWING")
    private val CATCHING = MarkerFactory.getMarker("CATCHING")
    private const val EXITONLY = "exit"
    private const val EXITMESSAGE = "exit with ({})"
  }

  override suspend fun <T : Any> withMDCContext(block: () -> T): T {
    return tracingContext(block)!!
  }

  override suspend fun isDebugEnabled(): Boolean {
    return underlyingLogger.isDebugEnabled
  }

  override suspend fun isInfoEnabled(): Boolean {
    return underlyingLogger.isInfoEnabled
  }

  override suspend fun isWarnEnabled(): Boolean {
    return underlyingLogger.isWarnEnabled
  }

  override suspend fun isErrorEnabled(): Boolean {
    return underlyingLogger.isErrorEnabled
  }

  override suspend fun debug(msg: String?) {
    if (underlyingLogger.isDebugEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.DEBUG_INT, msg, null, null
      )
    }
  }

  override suspend fun debug(format: String?, arg: Any?) {
    if (underlyingLogger.isDebugEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.DEBUG_INT, format, arrayOf(arg), null
      )
    }
  }

  override suspend fun debug(format: String?, arg1: Any?, arg2: Any?) {
    if (underlyingLogger.isDebugEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.DEBUG_INT, format, arrayOf(arg1, arg2), null
      )
    }
  }

  override suspend fun debug(format: String?, vararg arguments: Any?) {
    if (underlyingLogger.isDebugEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.DEBUG_INT, format, arguments, null
      )
    }
  }

  override suspend fun debug(msg: String?, t: Throwable) {
    if (underlyingLogger.isDebugEnabled) tracingContext {
      underlyingLogger.log(null, fqcn, LocationAwareLogger.DEBUG_INT, msg, null, t)
    }
  }

  override suspend fun info(msg: String?) {
    if (underlyingLogger.isInfoEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.INFO_INT, msg, null, null
      )
    }
  }

  override suspend fun info(format: String?, arg: Any?) {
    if (underlyingLogger.isInfoEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.INFO_INT, format, arrayOf(arg), null
      )
    }
  }

  override suspend fun info(format: String?, arg1: Any?, arg2: Any?) {
    if (underlyingLogger.isInfoEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.INFO_INT, format, arrayOf(arg1, arg2), null
      )
    }
  }

  override suspend fun info(format: String?, vararg arguments: Any?) {
    if (underlyingLogger.isInfoEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.INFO_INT, format, arguments, null
      )
    }
  }

  override suspend fun info(msg: String?, t: Throwable) {
    if (underlyingLogger.isInfoEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.INFO_INT, msg, null, t
      )
    }
  }

  override suspend fun warn(msg: String?) {
    if (underlyingLogger.isWarnEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.WARN_INT, msg, null, null
      )
    }
  }

  override suspend fun warn(format: String?, arg: Any?) {
    if (underlyingLogger.isWarnEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.WARN_INT, format, arrayOf(arg), null
      )
    }
  }

  override suspend fun warn(format: String?, arg1: Any?, arg2: Any?) {
    if (underlyingLogger.isWarnEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.WARN_INT, format, arrayOf(arg1, arg2), null
      )
    }
  }

  override suspend fun warn(format: String?, vararg arguments: Any?) {
    if (underlyingLogger.isWarnEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.WARN_INT, format, arguments, null
      )
    }

  }

  override suspend fun warn(msg: String?, t: Throwable) {
    if (underlyingLogger.isWarnEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.WARN_INT, msg, null, t
      )
    }
  }

  override suspend fun error(msg: String?) {
    if (underlyingLogger.isErrorEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.ERROR_INT, msg, null, null
      )
    }
  }

  override suspend fun error(format: String?, arg: Any?) {
    if (underlyingLogger.isErrorEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.ERROR_INT, format, arrayOf(arg), null
      )
    }
  }

  override suspend fun error(format: String?, arg1: Any?, arg2: Any?) {
    if (underlyingLogger.isErrorEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.ERROR_INT, format, arrayOf(arg1, arg2), null
      )
    }
  }

  override suspend fun error(format: String?, vararg arguments: Any?) {
    if (underlyingLogger.isErrorEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.ERROR_INT, format, arguments, null
      )
    }
  }

  override suspend fun error(msg: String?, t: Throwable) {
    if (underlyingLogger.isErrorEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.ERROR_INT, msg, null, t
      )
    }
  }


  override suspend fun trace(msg: () -> Any?) {
    if (underlyingLogger.isTraceEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.TRACE_INT, msg.toStringSafe(), null, null
      )
    }
  }

  override suspend fun trace(t: Throwable?, msg: () -> Any?) {
    if (underlyingLogger.isTraceEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.TRACE_INT, msg.toStringSafe(), null, t
      )
    }
  }

  override suspend fun trace(marker: Marker?, msg: () -> Any?) {
    if (underlyingLogger.isTraceEnabled) tracingContext {
      underlyingLogger.log(
        marker, fqcn, LocationAwareLogger.TRACE_INT, msg.toStringSafe(), null, null
      )
    }
  }

  override suspend fun trace(marker: Marker?, t: Throwable?, msg: () -> Any?) {
    if (underlyingLogger.isTraceEnabled) tracingContext {
      underlyingLogger.log(
        marker, fqcn, LocationAwareLogger.TRACE_INT, msg.toStringSafe(), null, t
      )
    }
  }

  override suspend fun debug(msg: () -> Any?) {
    if (underlyingLogger.isDebugEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.DEBUG_INT, msg.toStringSafe(), null, null
      )
    }
  }

  override suspend fun debug(t: Throwable?, msg: () -> Any?) {
    if (underlyingLogger.isDebugEnabled) tracingContext {
      underlyingLogger.log(null, fqcn, LocationAwareLogger.DEBUG_INT, msg.toStringSafe(), null, t)
    }
  }

  override suspend fun debug(marker: Marker?, msg: () -> Any?) {
    if (underlyingLogger.isDebugEnabled) tracingContext {
      underlyingLogger.log(
        marker, fqcn, LocationAwareLogger.DEBUG_INT, msg.toStringSafe(), null, null
      )
    }
  }

  override suspend fun debug(marker: Marker?, t: Throwable?, msg: () -> Any?) {
    if (underlyingLogger.isDebugEnabled) tracingContext {
      underlyingLogger.log(
        marker, fqcn, LocationAwareLogger.DEBUG_INT, msg.toStringSafe(), null, t
      )
    }
  }

  override suspend fun info(msg: () -> Any?) {
    if (underlyingLogger.isInfoEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.INFO_INT, msg.toStringSafe(), null, null
      )
    }
  }

  override suspend fun info(t: Throwable?, msg: () -> Any?) {
    if (underlyingLogger.isInfoEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.INFO_INT, msg.toStringSafe(), null, t
      )
    }
  }

  override suspend fun info(marker: Marker?, msg: () -> Any?) {
    if (underlyingLogger.isInfoEnabled) tracingContext {
      underlyingLogger.log(
        marker, fqcn, LocationAwareLogger.INFO_INT, msg.toStringSafe(), null, null
      )
    }
  }

  override suspend fun info(marker: Marker?, t: Throwable?, msg: () -> Any?) {
    if (underlyingLogger.isInfoEnabled) tracingContext {
      underlyingLogger.log(
        marker, fqcn, LocationAwareLogger.INFO_INT, msg.toStringSafe(), null, t
      )
    }
  }

  override suspend fun warn(msg: () -> Any?) {
    if (underlyingLogger.isWarnEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.WARN_INT, msg.toStringSafe(), null, null
      )
    }
  }

  override suspend fun warn(t: Throwable?, msg: () -> Any?) {
    if (underlyingLogger.isWarnEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.WARN_INT, msg.toStringSafe(), null, t
      )
    }
  }

  override suspend fun warn(marker: Marker?, msg: () -> Any?) {
    if (underlyingLogger.isWarnEnabled) tracingContext {
      underlyingLogger.log(
        marker, fqcn, LocationAwareLogger.WARN_INT, msg.toStringSafe(), null, null
      )
    }
  }

  override suspend fun warn(marker: Marker?, t: Throwable?, msg: () -> Any?) {
    if (underlyingLogger.isWarnEnabled) tracingContext {
      underlyingLogger.log(
        marker, fqcn, LocationAwareLogger.WARN_INT, msg.toStringSafe(), null, t
      )
    }
  }

  override suspend fun error(msg: () -> Any?) {
    if (underlyingLogger.isErrorEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.ERROR_INT, msg.toStringSafe(), null, null
      )
    }
  }

  override suspend fun error(t: Throwable?, msg: () -> Any?) {
    if (underlyingLogger.isErrorEnabled) tracingContext {
      underlyingLogger.log(
        null, fqcn, LocationAwareLogger.ERROR_INT, msg.toStringSafe(), null, t
      )
    }
  }

  override suspend fun error(marker: Marker?, msg: () -> Any?) {
    if (underlyingLogger.isErrorEnabled) tracingContext {
      underlyingLogger.log(
        marker, fqcn, LocationAwareLogger.ERROR_INT, msg.toStringSafe(), null, null
      )
    }
  }

  override suspend fun error(marker: Marker?, t: Throwable?, msg: () -> Any?) {
    if (underlyingLogger.isErrorEnabled) tracingContext {
      underlyingLogger.log(
        marker, fqcn, LocationAwareLogger.ERROR_INT, msg.toStringSafe(), null, t
      )
    }
  }

  override suspend fun entry(vararg argArray: Any?) {
    if (underlyingLogger.isTraceEnabled(ENTRY)) {
      val tp = MessageFormatter.arrayFormat(buildMessagePattern(argArray.size), argArray)
      tracingContext {
        underlyingLogger.log(ENTRY, fqcn, LocationAwareLogger.TRACE_INT, tp.message, null, null)
      }
    }
  }

  override suspend fun exit() {
    if (underlyingLogger.isTraceEnabled(EXIT)) {
      tracingContext {
        underlyingLogger.log(EXIT, fqcn, LocationAwareLogger.TRACE_INT, EXITONLY, null, null)
      }
    }
  }

  override suspend fun <T> exit(result: T): T {
    if (underlyingLogger.isTraceEnabled(EXIT)) {
      val tp = MessageFormatter.format(EXITMESSAGE, result)
      tracingContext {
        underlyingLogger.log(
          EXIT, fqcn, LocationAwareLogger.TRACE_INT, tp.message, arrayOf<Any?>(result), tp.throwable
        )
      }
    }
    return result
  }

  override suspend fun <T : Throwable> throwing(throwable: T): T {
    tracingContext {
      underlyingLogger.log(
        THROWING, fqcn, LocationAwareLogger.ERROR_INT, "throwing", null, throwable
      )
    }
    throw throwable
  }

  override suspend fun <T : Throwable> catching(throwable: T) {
    if (underlyingLogger.isErrorEnabled) {
      tracingContext {
        underlyingLogger.log(
          CATCHING, fqcn, LocationAwareLogger.ERROR_INT, "catching", null, throwable
        )
      }
    }
  }

  private suspend fun <T> tracingContext(block: () -> T?): T? {
    val contextMap = Mono.deferContextual { cv ->
      val map = if (cv.isEmpty) {
        emptyMap()
      } else {
        val ctxMap = HashMap<String, String>()
        cv.forEach { t, u ->
          val key = t.toString()
          if (TraceConstants.CTX_KEY == key) {
            return@forEach
          }
          ctxMap[key] = u.toString()
        }
        ctxMap
      }
      Mono.just(map)
    }.awaitSingle()
    MDC.setContextMap(contextMap)
    val invoke = block.invoke()
    MDC.clear()
    return invoke
  }

  private fun buildMessagePattern(len: Int): String {
    return (1..len).joinToString(separator = ", ", prefix = "entry with (", postfix = ")") { "{}" }
  }
}
