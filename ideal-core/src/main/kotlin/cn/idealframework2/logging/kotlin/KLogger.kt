package cn.idealframework2.logging.kotlin

import org.slf4j.Logger
import org.slf4j.Marker

/**
 * @author 宋志宗 on 2022/11/21
 */
interface KLogger : Logger {

  /** The  logger executing logging */
  val underlyingLogger: Logger

  /** Lazy add a log message if isTraceEnabled is true */
  fun trace(msg: () -> Any?)

  /** Lazy add a log message if isDebugEnabled is true */
  fun debug(msg: () -> Any?)

  /** Lazy add a log message if isInfoEnabled is true */
  fun info(msg: () -> Any?)

  /** Lazy add a log message if isWarnEnabled is true */
  fun warn(msg: () -> Any?)

  /** Lazy add a log message if isErrorEnabled is true */
  fun error(msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isTraceEnabled is true */
  fun trace(t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isDebugEnabled is true */
  fun debug(t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isInfoEnabled is true */
  fun info(t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isWarnEnabled is true */
  fun warn(t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isErrorEnabled is true */
  fun error(t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isTraceEnabled is true */
  fun trace(marker: Marker?, msg: () -> Any?)

  /** Lazy add a log message if isDebugEnabled is true */
  fun debug(marker: Marker?, msg: () -> Any?)

  /** Lazy add a log message if isInfoEnabled is true */
  fun info(marker: Marker?, msg: () -> Any?)

  /** Lazy add a log message if isWarnEnabled is true */
  fun warn(marker: Marker?, msg: () -> Any?)

  /** Lazy add a log message if isErrorEnabled is true */
  fun error(marker: Marker?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isTraceEnabled is true */
  fun trace(marker: Marker?, t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isDebugEnabled is true */
  fun debug(marker: Marker?, t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isInfoEnabled is true */
  fun info(marker: Marker?, t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isWarnEnabled is true */
  fun warn(marker: Marker?, t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isErrorEnabled is true */
  fun error(marker: Marker?, t: Throwable?, msg: () -> Any?)

  /** Add a log message with all the supplied parameters along with method name */
  fun entry(vararg argArray: Any?)

  /** Add log message indicating exit of a method */
  fun exit()

  /** Add a log message with the return value of a method */
  fun <T> exit(result: T): T where T : Any?

  /** Add a log message indicating an exception will be thrown along with the stack trace. */
  fun <T> throwing(throwable: T): T where T : Throwable

  /** Add a log message indicating an exception is caught along with the stack trace. */
  fun <T> catching(throwable: T) where T : Throwable


  suspend fun <T : Any> withMDCContext(block: () -> T): T

  /**
   * Log a message at the DEBUG level.
   *
   * @param msg the message string to be logged
   */
  suspend fun mdcDebug(msg: String?)

  /**
   * Log a message at the DEBUG level according to the specified format
   * and argument.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the DEBUG level.
   *
   * @param format the format string
   * @param arg    the argument
   */
  suspend fun mdcDebug(format: String?, arg: Any?)

  /**
   * Log a message at the DEBUG level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the DEBUG level.
   *
   * @param format the format string
   * @param arg1   the first argument
   * @param arg2   the second argument
   */
  suspend fun mdcDebug(format: String?, arg1: Any?, arg2: Any?)

  /**
   * Log a message at the DEBUG level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous string concatenation when the logger
   * is disabled for the DEBUG level. However, this variant incurs the hidden
   * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
   * even if this logger is disabled for DEBUG. The variants taking
   * {@link #debug(String, Object) one} and {@link #debug(String, Object, Object) two}
   * arguments exist solely in order to avoid this hidden cost.
   *
   * @param format    the format string
   * @param arguments a list of 3 or more arguments
   */
  suspend fun mdcDebug(format: String?, vararg arguments: Any?)

  /**
   * Log an exception (throwable) at the DEBUG level with an
   * accompanying message.
   *
   * @param msg the message accompanying the exception
   * @param t   the exception (throwable) to log
   */
  suspend fun mdcDebug(msg: String?, t: Throwable)

  /**
   * Log a message at the INFO level.
   *
   * @param msg the message string to be logged
   */
  suspend fun mdcInfo(msg: String?)

  /**
   * Log a message at the INFO level according to the specified format
   * and argument.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the INFO level.
   *
   * @param format the format string
   * @param arg    the argument
   */
  suspend fun mdcInfo(format: String?, arg: Any?)

  /**
   * Log a message at the INFO level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the INFO level.
   *
   * @param format the format string
   * @param arg1   the first argument
   * @param arg2   the second argument
   */
  suspend fun mdcInfo(format: String?, arg1: Any?, arg2: Any?)

  /**
   * Log a message at the INFO level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous string concatenation when the logger
   * is disabled for the INFO level. However, this variant incurs the hidden
   * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
   * even if this logger is disabled for INFO. The variants taking
   * {@link #info(String, Object) one} and {@link #info(String, Object, Object) two}
   * arguments exist solely in order to avoid this hidden cost.
   *
   * @param format    the format string
   * @param arguments a list of 3 or more arguments
   */
  suspend fun mdcInfo(format: String?, vararg arguments: Any?)

  /**
   * Log an exception (throwable) at the INFO level with an
   * accompanying message.
   *
   * @param msg the message accompanying the exception
   * @param t   the exception (throwable) to log
   */
  suspend fun mdcInfo(msg: String?, t: Throwable)

  /**
   * Log a message at the WARN level.
   *
   * @param msg the message string to be logged
   */
  suspend fun mdcWarn(msg: String?)

  /**
   * Log a message at the WARN level according to the specified format
   * and argument.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the WARN level.
   *
   * @param format the format string
   * @param arg    the argument
   */
  suspend fun mdcWarn(format: String?, arg: Any?)

  /**
   * Log a message at the WARN level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the WARN level.
   *
   * @param format the format string
   * @param arg1   the first argument
   * @param arg2   the second argument
   */
  suspend fun mdcWarn(format: String?, arg1: Any?, arg2: Any?)

  /**
   * Log a message at the WARN level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous string concatenation when the logger
   * is disabled for the WARN level. However, this variant incurs the hidden
   * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
   * even if this logger is disabled for WARN. The variants taking
   * {@link #warn(String, Object) one} and {@link #warn(String, Object, Object) two}
   * arguments exist solely in order to avoid this hidden cost.
   *
   * @param format    the format string
   * @param arguments a list of 3 or more arguments
   */
  suspend fun mdcWarn(format: String?, vararg arguments: Any?)

  /**
   * Log an exception (throwable) at the WARN level with an
   * accompanying message.
   *
   * @param msg the message accompanying the exception
   * @param t   the exception (throwable) to log
   */
  suspend fun mdcWarn(msg: String?, t: Throwable)

  /**
   * Log a message at the ERROR level.
   *
   * @param msg the message string to be logged
   */
  suspend fun mdcError(msg: String?)

  /**
   * Log a message at the ERROR level according to the specified format
   * and argument.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the ERROR level.
   *
   * @param format the format string
   * @param arg    the argument
   */
  suspend fun mdcError(format: String?, arg: Any?)

  /**
   * Log a message at the ERROR level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the ERROR level.
   *
   * @param format the format string
   * @param arg1   the first argument
   * @param arg2   the second argument
   */
  suspend fun mdcError(format: String?, arg1: Any?, arg2: Any?)

  /**
   * Log a message at the ERROR level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous string concatenation when the logger
   * is disabled for the ERROR level. However, this variant incurs the hidden
   * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
   * even if this logger is disabled for ERROR. The variants taking
   * {@link #error(String, Object) one} and {@link #error(String, Object, Object) two}
   * arguments exist solely in order to avoid this hidden cost.
   *
   * @param format    the format string
   * @param arguments a list of 3 or more arguments
   */
  suspend fun mdcError(format: String?, vararg arguments: Any?)

  /**
   * Log an exception (throwable) at the ERROR level with an
   * accompanying message.
   *
   * @param msg the message accompanying the exception
   * @param t   the exception (throwable) to log
   */
  suspend fun mdcError(msg: String?, t: Throwable)

  /** Lazy add a log message if isTraceEnabled is true */
  suspend fun mdcTrace(msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isTraceEnabled is true */
  suspend fun mdcTrace(t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isTraceEnabled is true */
  suspend fun mdcTrace(marker: Marker?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isTraceEnabled is true */
  suspend fun mdcTrace(marker: Marker?, t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isDebugEnabled is true */
  suspend fun mdcDebug(msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isDebugEnabled is true */
  suspend fun mdcDebug(t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isDebugEnabled is true */
  suspend fun mdcDebug(marker: Marker?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isDebugEnabled is true */
  suspend fun mdcDebug(marker: Marker?, t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isInfoEnabled is true */
  suspend fun mdcInfo(msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isInfoEnabled is true */
  suspend fun mdcInfo(t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isInfoEnabled is true */
  suspend fun mdcInfo(marker: Marker?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isInfoEnabled is true */
  suspend fun mdcInfo(marker: Marker?, t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isWarnEnabled is true */
  suspend fun mdcWarn(msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isWarnEnabled is true */
  suspend fun mdcWarn(t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isWarnEnabled is true */
  suspend fun mdcWarn(marker: Marker?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isWarnEnabled is true */
  suspend fun mdcWarn(marker: Marker?, t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isErrorEnabled is true */
  suspend fun mdcError(msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isErrorEnabled is true */
  suspend fun mdcError(t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isErrorEnabled is true */
  suspend fun mdcError(marker: Marker?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isErrorEnabled is true */
  suspend fun mdcError(marker: Marker?, t: Throwable?, msg: () -> Any?)

  /** Add a log message with all the supplied parameters along with method name */
  suspend fun mdcEntry(vararg argArray: Any?)

  /** Add log message indicating exit of a method */
  suspend fun mdcExit()

  /** Add a log message with the return value of a method */
  suspend fun <T> mdcExit(result: T): T where T : Any?

  /** Add a log message indicating an exception will be thrown along with the stack trace. */
  suspend fun <T> mdcThrowing(throwable: T): T where T : Throwable

  /** Add a log message indicating an exception is caught along with the stack trace. */
  suspend fun <T> mdcCatching(throwable: T) where T : Throwable
}
