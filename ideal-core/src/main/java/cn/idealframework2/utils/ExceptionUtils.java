package cn.idealframework2.utils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author 宋志宗 on 2022/7/18
 */
public final class ExceptionUtils {

  private ExceptionUtils() {
  }

  @Nonnull
  public static Throwable getRootCause(@Nonnull Throwable throwable) {
    Throwable cause = throwable.getCause();
    if (cause == null) {
      return throwable;
    }
    return getRootCause(cause);
  }

  @Nonnull
  public static String stackTrace(@Nonnull Throwable throwable) {
    try (StringWriter stringWriter = new StringWriter();
         PrintWriter printWriter = new PrintWriter(stringWriter)) {
      throwable.printStackTrace(printWriter);
      return stringWriter.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
