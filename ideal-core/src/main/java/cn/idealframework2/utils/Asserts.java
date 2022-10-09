package cn.idealframework2.utils;

import cn.idealframework2.exception.BadRequestException;
import cn.idealframework2.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serial;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author 宋志宗 on 2021/4/20
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class Asserts {
  private static final Logger log = LoggerFactory.getLogger(Asserts.class);

  @Nonnull
  public static CharSequence notBlank(@Nullable CharSequence charSequence,
                                      @Nullable String message) throws IllegalArgumentException {
    if (StringUtils.isBlank(charSequence)) {
      throw genAssertException(message);
    }
    return charSequence;
  }

  @Nonnull
  public static CharSequence notBlank(@Nullable CharSequence charSequence,
                                      @Nonnull Supplier<String> messageSupplier) throws IllegalArgumentException {
    if (StringUtils.isBlank(charSequence)) {
      String message = messageSupplier.get();
      throw genAssertException(message);
    }
    return charSequence;
  }

  @Nonnull
  public static CharSequence ifBlankThrow(@Nullable CharSequence charSequence,
                                          @Nonnull Supplier<? extends RuntimeException> supplier) {
    if (StringUtils.isBlank(charSequence)) {
      throw supplier.get();
    }
    return charSequence;
  }

  @Nonnull
  public static <T> T nonnull(@Nullable T t, @Nullable String message) {
    if (t == null) {
      throw genAssertException(message);
    }
    return t;
  }

  @Nonnull
  public static <T> T nonnull(@Nullable T t, @Nonnull Supplier<String> messageSupplier) {
    if (t == null) {
      String message = messageSupplier.get();
      throw genAssertException(message);
    }
    return t;
  }

  @Nonnull
  public static <T> T ifNullThrow(@Nullable T t,
                                  @Nonnull Supplier<? extends RuntimeException> supplier) {
    if (t == null) {
      throw supplier.get();
    }
    return t;
  }

  public static void equals(@Nullable Object o1, @Nullable Object o2, @Nullable String message) {
    if (!Objects.equals(o1, o2)) {
      throw genAssertException(message);
    }
  }

  public static void equals(long l1, long l2, @Nullable String message) {
    if (l1 != l2) {
      throw genAssertException(message);
    }
  }

  public static void equals(int i1, int i2, @Nullable String message) {
    if (i1 != i2) {
      throw genAssertException(message);
    }
  }

  public static void notEquals(@Nullable Object o1, @Nullable Object o2, @Nullable String message) {
    if (Objects.equals(o1, o2)) {
      throw genAssertException(message);
    }
  }

  public static void notEquals(long l1, long l2, @Nullable String message) {
    if (l1 == l2) {
      throw genAssertException(message);
    }
  }

  public static void notEquals(int i1, int i2, @Nullable String message) {
    if (i1 == i2) {
      throw genAssertException(message);
    }
  }


  public static void maxLength(@Nonnull CharSequence charSequence, int length, @Nullable String message) {
    if (charSequence.length() > length) {
      throw genAssertException(message);
    }
  }

  public static void assertTrue(boolean expression, @Nullable String message) {
    if (!expression) {
      throw genAssertException(message);
    }
  }

  public static void assertTrue(boolean expression, @Nonnull Supplier<String> messageSupplier) {
    if (!expression) {
      throw genAssertException(messageSupplier.get());
    }
  }

  public static void assertFalse(boolean expression, @Nullable String message) {
    if (expression) {
      throw genAssertException(message);
    }
  }

  public static void assertFalse(boolean expression, @Nonnull Supplier<String> messageSupplier) {
    if (expression) {
      throw genAssertException(messageSupplier.get());
    }
  }

  @Nonnull
  public static <C extends Collection<?>> C notEmpty(@Nullable C collection,
                                                     @Nullable String message) {
    if (collection == null || collection.isEmpty()) {
      throw genAssertException(message);
    }
    return collection;
  }

  @Nonnull
  public static <C extends Collection<?>> C notEmpty(@Nullable C collection,
                                                     @Nonnull Supplier<String> messageSupplier) {
    if (collection == null || collection.isEmpty()) {
      throw genAssertException(messageSupplier.get());
    }
    return collection;
  }

  @Nonnull
  public static <K, V> Map<K, V> notEmpty(@Nullable Map<K, V> map,
                                          @Nullable String message) {
    if (map == null || map.isEmpty()) {
      throw genAssertException(message);
    }
    return map;
  }


  public static void range(long value, long minimum, long maximum,
                           @Nullable String message) throws IllegalArgumentException {
    if (value < minimum || value > maximum) {
      throw genAssertException(message);
    }
  }

  private static void printLog(@Nullable String message) {
    StackTraceElement[] stackTrace = new Throwable().getStackTrace();
    int critical = 3;
    if (stackTrace.length > critical) {
      StackTraceElement stackTraceElement = stackTrace[critical];
      String className = stackTraceElement.getClassName();
      int lineNumber = stackTraceElement.getLineNumber();
      log.info(className + " " + lineNumber + " : " + message);
    }
  }

  @Nonnull
  private static AssertException genAssertException(@Nullable String message) {
    if (message == null) {
      return new AssertException();
    }
    printLog(message);
    return new AssertException(message);
  }

  public static class AssertException extends BadRequestException {

    @Serial
    private static final long serialVersionUID = 8716092433907021356L;

    public AssertException(@Nonnull String message) {
      super(message);
    }

    public AssertException() {
      super("");
    }
  }

  private Asserts() {
  }
}
