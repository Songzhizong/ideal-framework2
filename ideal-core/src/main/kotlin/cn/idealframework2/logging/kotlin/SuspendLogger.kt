package cn.idealframework2.logging.kotlin

import org.slf4j.Logger
import org.slf4j.Marker

/**
 * @author 宋志宗 on 2022/11/17
 */
interface SuspendLogger {

  /** The  logger executing logging */
  val underlyingLogger: Logger

  suspend fun <T : Any> withMDCContext(block: () -> T): T

  /**
   * Is the logger instance enabled for the DEBUG level?
   *
   * @return True if this Logger is enabled for the DEBUG level,
   *         false otherwise.
   */
  fun isDebugEnabled(): Boolean

  /**
   * Is the logger instance enabled for the INFO level?
   *
   * @return True if this Logger is enabled for the INFO level,
   *         false otherwise.
   */
  fun isInfoEnabled(): Boolean

  /**
   * Is the logger instance enabled for the WARN level?
   *
   * @return True if this Logger is enabled for the WARN level,
   *         false otherwise.
   */
  fun isWarnEnabled(): Boolean

  /**
   * Is the logger instance enabled for the ERROR level?
   *
   * @return True if this Logger is enabled for the ERROR level,
   *         false otherwise.
   */
  fun isErrorEnabled(): Boolean

  /**
   * Log a message at the DEBUG level.
   *
   * @param msg the message string to be logged
   */
  suspend fun suspendDebug(msg: String?)

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
  suspend fun suspendDebug(format: String?, arg: Any?)

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
  suspend fun suspendDebug(format: String?, arg1: Any?, arg2: Any?)

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
  suspend fun suspendDebug(format: String?, vararg arguments: Any?)

  /**
   * Log an exception (throwable) at the DEBUG level with an
   * accompanying message.
   *
   * @param msg the message accompanying the exception
   * @param t   the exception (throwable) to log
   */
  suspend fun suspendDebug(msg: String?, t: Throwable)

  /**
   * Log a message at the INFO level.
   *
   * @param msg the message string to be logged
   */
  suspend fun suspendInfo(msg: String?)

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
  suspend fun suspendInfo(format: String?, arg: Any?)

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
  suspend fun suspendInfo(format: String?, arg1: Any?, arg2: Any?)

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
  suspend fun suspendInfo(format: String?, vararg arguments: Any?)

  /**
   * Log an exception (throwable) at the INFO level with an
   * accompanying message.
   *
   * @param msg the message accompanying the exception
   * @param t   the exception (throwable) to log
   */
  suspend fun suspendInfo(msg: String?, t: Throwable)

  /**
   * Log a message at the WARN level.
   *
   * @param msg the message string to be logged
   */
  suspend fun suspendWarn(msg: String?)

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
  suspend fun suspendWarn(format: String?, arg: Any?)

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
  suspend fun suspendWarn(format: String?, arg1: Any?, arg2: Any?)

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
  suspend fun suspendWarn(format: String?, vararg arguments: Any?)

  /**
   * Log an exception (throwable) at the WARN level with an
   * accompanying message.
   *
   * @param msg the message accompanying the exception
   * @param t   the exception (throwable) to log
   */
  suspend fun suspendWarn(msg: String?, t: Throwable)

  /**
   * Log a message at the ERROR level.
   *
   * @param msg the message string to be logged
   */
  suspend fun suspendError(msg: String?)

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
  suspend fun suspendError(format: String?, arg: Any?)

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
  suspend fun suspendError(format: String?, arg1: Any?, arg2: Any?)

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
  suspend fun suspendError(format: String?, vararg arguments: Any?)

  /**
   * Log an exception (throwable) at the ERROR level with an
   * accompanying message.
   *
   * @param msg the message accompanying the exception
   * @param t   the exception (throwable) to log
   */
  suspend fun suspendError(msg: String?, t: Throwable)

  /** Lazy add a log message if isTraceEnabled is true */
  suspend fun suspendTrace(msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isTraceEnabled is true */
  suspend fun suspendTrace(t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isTraceEnabled is true */
  suspend fun suspendTrace(marker: Marker?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isTraceEnabled is true */
  suspend fun suspendTrace(marker: Marker?, t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isDebugEnabled is true */
  suspend fun suspendDebug(msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isDebugEnabled is true */
  suspend fun suspendDebug(t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isDebugEnabled is true */
  suspend fun suspendDebug(marker: Marker?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isDebugEnabled is true */
  suspend fun suspendDebug(marker: Marker?, t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isInfoEnabled is true */
  suspend fun suspendInfo(msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isInfoEnabled is true */
  suspend fun suspendInfo(t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isInfoEnabled is true */
  suspend fun suspendInfo(marker: Marker?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isInfoEnabled is true */
  suspend fun suspendInfo(marker: Marker?, t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isWarnEnabled is true */
  suspend fun suspendWarn(msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isWarnEnabled is true */
  suspend fun suspendWarn(t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isWarnEnabled is true */
  suspend fun suspendWarn(marker: Marker?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isWarnEnabled is true */
  suspend fun suspendWarn(marker: Marker?, t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isErrorEnabled is true */
  suspend fun suspendError(msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isErrorEnabled is true */
  suspend fun suspendError(t: Throwable?, msg: () -> Any?)

  /** Lazy add a log message if isErrorEnabled is true */
  suspend fun suspendError(marker: Marker?, msg: () -> Any?)

  /** Lazy add a log message with throwable payload if isErrorEnabled is true */
  suspend fun suspendError(marker: Marker?, t: Throwable?, msg: () -> Any?)

  /** Add a log message with all the supplied parameters along with method name */
  suspend fun suspendEntry(vararg argArray: Any?)

  /** Add log message indicating exit of a method */
  suspend fun suspendExit()

  /** Add a log message with the return value of a method */
  suspend fun <T> suspendExit(result: T): T where T : Any?

  /** Add a log message indicating an exception will be thrown along with the stack trace. */
  suspend fun <T> suspendThrowing(throwable: T): T where T : Throwable

  /** Add a log message indicating an exception is caught along with the stack trace. */
  suspend fun <T> suspendCatching(throwable: T) where T : Throwable
}
