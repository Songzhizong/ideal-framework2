//package cn.idealframework2.logging.kotlin.internal
//
//import cn.idealframework2.logging.kotlin.SuspendLogger
//import cn.idealframework2.trace.TraceConstants
//import kotlinx.coroutines.reactor.awaitSingle
//import org.slf4j.MDC
//import org.slf4j.Marker
//import org.slf4j.MarkerFactory
//import org.slf4j.helpers.MessageFormatter
//import org.slf4j.spi.LocationAwareLogger
//import reactor.core.publisher.Mono
//
///**
// * @author 宋志宗 on 2022/11/17
// */
//internal class LocationAwareSuspendLogger(
//  override val underlyingLogger: LocationAwareLogger
//) : SuspendLogger {
//  @Suppress("SpellCheckingInspection")
//  companion object {
//    private val fqcn: String = LocationAwareSuspendLogger::class.java.name
//    private val ENTRY = MarkerFactory.getMarker("ENTRY")
//    private val EXIT = MarkerFactory.getMarker("EXIT")
//    private val THROWING = MarkerFactory.getMarker("THROWING")
//    private val CATCHING = MarkerFactory.getMarker("CATCHING")
//    private const val EXITONLY = "exit"
//    private const val EXITMESSAGE = "exit with ({})"
//  }
//
//  override suspend fun <T : Any> withMDCContext(block: () -> T): T {
//    return tracingContext(block)!!
//  }
//
//  override fun isDebugEnabled(): Boolean {
//    return underlyingLogger.isDebugEnabled
//  }
//
//  override fun isInfoEnabled(): Boolean {
//    return underlyingLogger.isInfoEnabled
//  }
//
//  override fun isWarnEnabled(): Boolean {
//    return underlyingLogger.isWarnEnabled
//  }
//
//  override fun isErrorEnabled(): Boolean {
//    return underlyingLogger.isErrorEnabled
//  }
//
//  override suspend fun suspendDebug(msg: String?) {
//    if (underlyingLogger.isDebugEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.DEBUG_INT, msg, null, null
//      )
//    }
//  }
//
//  override suspend fun suspendDebug(format: String?, arg: Any?) {
//    if (underlyingLogger.isDebugEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.DEBUG_INT, format, arrayOf(arg), null
//      )
//    }
//  }
//
//  override suspend fun suspendDebug(format: String?, arg1: Any?, arg2: Any?) {
//    if (underlyingLogger.isDebugEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.DEBUG_INT, format, arrayOf(arg1, arg2), null
//      )
//    }
//  }
//
//  override suspend fun suspendDebug(format: String?, vararg arguments: Any?) {
//    if (underlyingLogger.isDebugEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.DEBUG_INT, format, arguments, null
//      )
//    }
//  }
//
//  override suspend fun suspendDebug(msg: String?, t: Throwable) {
//    if (underlyingLogger.isDebugEnabled) tracingContext {
//      underlyingLogger.log(null, fqcn, LocationAwareLogger.DEBUG_INT, msg, null, t)
//    }
//  }
//
//  override suspend fun suspendInfo(msg: String?) {
//    if (underlyingLogger.isInfoEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.INFO_INT, msg, null, null
//      )
//    }
//  }
//
//  override suspend fun suspendInfo(format: String?, arg: Any?) {
//    if (underlyingLogger.isInfoEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.INFO_INT, format, arrayOf(arg), null
//      )
//    }
//  }
//
//  override suspend fun suspendInfo(format: String?, arg1: Any?, arg2: Any?) {
//    if (underlyingLogger.isInfoEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.INFO_INT, format, arrayOf(arg1, arg2), null
//      )
//    }
//  }
//
//  override suspend fun suspendInfo(format: String?, vararg arguments: Any?) {
//    if (underlyingLogger.isInfoEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.INFO_INT, format, arguments, null
//      )
//    }
//  }
//
//  override suspend fun suspendInfo(msg: String?, t: Throwable) {
//    if (underlyingLogger.isInfoEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.INFO_INT, msg, null, t
//      )
//    }
//  }
//
//  override suspend fun suspendWarn(msg: String?) {
//    if (underlyingLogger.isWarnEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.WARN_INT, msg, null, null
//      )
//    }
//  }
//
//  override suspend fun suspendWarn(format: String?, arg: Any?) {
//    if (underlyingLogger.isWarnEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.WARN_INT, format, arrayOf(arg), null
//      )
//    }
//  }
//
//  override suspend fun suspendWarn(format: String?, arg1: Any?, arg2: Any?) {
//    if (underlyingLogger.isWarnEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.WARN_INT, format, arrayOf(arg1, arg2), null
//      )
//    }
//  }
//
//  override suspend fun suspendWarn(format: String?, vararg arguments: Any?) {
//    if (underlyingLogger.isWarnEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.WARN_INT, format, arguments, null
//      )
//    }
//
//  }
//
//  override suspend fun suspendWarn(msg: String?, t: Throwable) {
//    if (underlyingLogger.isWarnEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.WARN_INT, msg, null, t
//      )
//    }
//  }
//
//  override suspend fun suspendError(msg: String?) {
//    if (underlyingLogger.isErrorEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.ERROR_INT, msg, null, null
//      )
//    }
//  }
//
//  override suspend fun suspendError(format: String?, arg: Any?) {
//    if (underlyingLogger.isErrorEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.ERROR_INT, format, arrayOf(arg), null
//      )
//    }
//  }
//
//  override suspend fun suspendError(format: String?, arg1: Any?, arg2: Any?) {
//    if (underlyingLogger.isErrorEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.ERROR_INT, format, arrayOf(arg1, arg2), null
//      )
//    }
//  }
//
//  override suspend fun suspendError(format: String?, vararg arguments: Any?) {
//    if (underlyingLogger.isErrorEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.ERROR_INT, format, arguments, null
//      )
//    }
//  }
//
//  override suspend fun suspendError(msg: String?, t: Throwable) {
//    if (underlyingLogger.isErrorEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.ERROR_INT, msg, null, t
//      )
//    }
//  }
//
//
//  override suspend fun suspendTrace(msg: () -> Any?) {
//    if (underlyingLogger.isTraceEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.TRACE_INT, msg.toStringSafe(), null, null
//      )
//    }
//  }
//
//  override suspend fun suspendTrace(t: Throwable?, msg: () -> Any?) {
//    if (underlyingLogger.isTraceEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.TRACE_INT, msg.toStringSafe(), null, t
//      )
//    }
//  }
//
//  override suspend fun suspendTrace(marker: Marker?, msg: () -> Any?) {
//    if (underlyingLogger.isTraceEnabled) tracingContext {
//      underlyingLogger.log(
//        marker, fqcn, LocationAwareLogger.TRACE_INT, msg.toStringSafe(), null, null
//      )
//    }
//  }
//
//  override suspend fun suspendTrace(marker: Marker?, t: Throwable?, msg: () -> Any?) {
//    if (underlyingLogger.isTraceEnabled) tracingContext {
//      underlyingLogger.log(
//        marker, fqcn, LocationAwareLogger.TRACE_INT, msg.toStringSafe(), null, t
//      )
//    }
//  }
//
//  override suspend fun suspendDebug(msg: () -> Any?) {
//    if (underlyingLogger.isDebugEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.DEBUG_INT, msg.toStringSafe(), null, null
//      )
//    }
//  }
//
//  override suspend fun suspendDebug(t: Throwable?, msg: () -> Any?) {
//    if (underlyingLogger.isDebugEnabled) tracingContext {
//      underlyingLogger.log(null, fqcn, LocationAwareLogger.DEBUG_INT, msg.toStringSafe(), null, t)
//    }
//  }
//
//  override suspend fun suspendDebug(marker: Marker?, msg: () -> Any?) {
//    if (underlyingLogger.isDebugEnabled) tracingContext {
//      underlyingLogger.log(
//        marker, fqcn, LocationAwareLogger.DEBUG_INT, msg.toStringSafe(), null, null
//      )
//    }
//  }
//
//  override suspend fun suspendDebug(marker: Marker?, t: Throwable?, msg: () -> Any?) {
//    if (underlyingLogger.isDebugEnabled) tracingContext {
//      underlyingLogger.log(
//        marker, fqcn, LocationAwareLogger.DEBUG_INT, msg.toStringSafe(), null, t
//      )
//    }
//  }
//
//  override suspend fun suspendInfo(msg: () -> Any?) {
//    if (underlyingLogger.isInfoEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.INFO_INT, msg.toStringSafe(), null, null
//      )
//    }
//  }
//
//  override suspend fun suspendInfo(t: Throwable?, msg: () -> Any?) {
//    if (underlyingLogger.isInfoEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.INFO_INT, msg.toStringSafe(), null, t
//      )
//    }
//  }
//
//  override suspend fun suspendInfo(marker: Marker?, msg: () -> Any?) {
//    if (underlyingLogger.isInfoEnabled) tracingContext {
//      underlyingLogger.log(
//        marker, fqcn, LocationAwareLogger.INFO_INT, msg.toStringSafe(), null, null
//      )
//    }
//  }
//
//  override suspend fun suspendInfo(marker: Marker?, t: Throwable?, msg: () -> Any?) {
//    if (underlyingLogger.isInfoEnabled) tracingContext {
//      underlyingLogger.log(
//        marker, fqcn, LocationAwareLogger.INFO_INT, msg.toStringSafe(), null, t
//      )
//    }
//  }
//
//  override suspend fun suspendWarn(msg: () -> Any?) {
//    if (underlyingLogger.isWarnEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.WARN_INT, msg.toStringSafe(), null, null
//      )
//    }
//  }
//
//  override suspend fun suspendWarn(t: Throwable?, msg: () -> Any?) {
//    if (underlyingLogger.isWarnEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.WARN_INT, msg.toStringSafe(), null, t
//      )
//    }
//  }
//
//  override suspend fun suspendWarn(marker: Marker?, msg: () -> Any?) {
//    if (underlyingLogger.isWarnEnabled) tracingContext {
//      underlyingLogger.log(
//        marker, fqcn, LocationAwareLogger.WARN_INT, msg.toStringSafe(), null, null
//      )
//    }
//  }
//
//  override suspend fun suspendWarn(marker: Marker?, t: Throwable?, msg: () -> Any?) {
//    if (underlyingLogger.isWarnEnabled) tracingContext {
//      underlyingLogger.log(
//        marker, fqcn, LocationAwareLogger.WARN_INT, msg.toStringSafe(), null, t
//      )
//    }
//  }
//
//  override suspend fun suspendError(msg: () -> Any?) {
//    if (underlyingLogger.isErrorEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.ERROR_INT, msg.toStringSafe(), null, null
//      )
//    }
//  }
//
//  override suspend fun suspendError(t: Throwable?, msg: () -> Any?) {
//    if (underlyingLogger.isErrorEnabled) tracingContext {
//      underlyingLogger.log(
//        null, fqcn, LocationAwareLogger.ERROR_INT, msg.toStringSafe(), null, t
//      )
//    }
//  }
//
//  override suspend fun suspendError(marker: Marker?, msg: () -> Any?) {
//    if (underlyingLogger.isErrorEnabled) tracingContext {
//      underlyingLogger.log(
//        marker, fqcn, LocationAwareLogger.ERROR_INT, msg.toStringSafe(), null, null
//      )
//    }
//  }
//
//  override suspend fun suspendError(marker: Marker?, t: Throwable?, msg: () -> Any?) {
//    if (underlyingLogger.isErrorEnabled) tracingContext {
//      underlyingLogger.log(
//        marker, fqcn, LocationAwareLogger.ERROR_INT, msg.toStringSafe(), null, t
//      )
//    }
//  }
//
//  override suspend fun suspendEntry(vararg argArray: Any?) {
//    if (underlyingLogger.isTraceEnabled(ENTRY)) {
//      val tp = MessageFormatter.arrayFormat(buildMessagePattern(argArray.size), argArray)
//      tracingContext {
//        underlyingLogger.log(ENTRY, fqcn, LocationAwareLogger.TRACE_INT, tp.message, null, null)
//      }
//    }
//  }
//
//  override suspend fun suspendExit() {
//    if (underlyingLogger.isTraceEnabled(EXIT)) {
//      tracingContext {
//        underlyingLogger.log(EXIT, fqcn, LocationAwareLogger.TRACE_INT, EXITONLY, null, null)
//      }
//    }
//  }
//
//  override suspend fun <T> suspendExit(result: T): T {
//    if (underlyingLogger.isTraceEnabled(EXIT)) {
//      val tp = MessageFormatter.format(EXITMESSAGE, result)
//      tracingContext {
//        underlyingLogger.log(
//          EXIT, fqcn, LocationAwareLogger.TRACE_INT, tp.message, arrayOf<Any?>(result), tp.throwable
//        )
//      }
//    }
//    return result
//  }
//
//  override suspend fun <T : Throwable> suspendThrowing(throwable: T): T {
//    tracingContext {
//      underlyingLogger.log(
//        THROWING, fqcn, LocationAwareLogger.ERROR_INT, "throwing", null, throwable
//      )
//    }
//    throw throwable
//  }
//
//  override suspend fun <T : Throwable> suspendCatching(throwable: T) {
//    if (underlyingLogger.isErrorEnabled) {
//      tracingContext {
//        underlyingLogger.log(
//          CATCHING, fqcn, LocationAwareLogger.ERROR_INT, "catching", null, throwable
//        )
//      }
//    }
//  }
//
//  private suspend fun <T> tracingContext(block: () -> T?): T? {
//    val contextMap = Mono.deferContextual { cv ->
//      val map = if (cv.isEmpty) {
//        emptyMap()
//      } else {
//        val ctxMap = HashMap<String, String>()
//        cv.forEach { t, u ->
//          val key = t.toString()
//          if (TraceConstants.CTX_KEY == key) {
//            return@forEach
//          }
//          ctxMap[key] = u.toString()
//        }
//        ctxMap
//      }
//      Mono.just(map)
//    }.awaitSingle()
//    MDC.setContextMap(contextMap)
//    val invoke = block.invoke()
//    MDC.clear()
//    return invoke
//  }
//
//  private fun buildMessagePattern(len: Int): String {
//    return (1..len).joinToString(separator = ", ", prefix = "entry with (", postfix = ")") { "{}" }
//  }
//}
