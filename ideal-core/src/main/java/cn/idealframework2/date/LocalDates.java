package cn.idealframework2.date;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Locale;

/**
 * @author 宋志宗 on 2020/12/31
 */
public final class LocalDates {
  private LocalDates() {
  }

  private static ZoneOffset offset = ZoneOffset.of("+8");
  private static final Locale LOCALE = Locale.SIMPLIFIED_CHINESE;

  @Nonnull
  public static LocalDate parse(@Nonnull String localDateString,
                                @Nonnull String pattern) {
    return parse(localDateString, pattern, LOCALE);
  }

  @Nonnull
  public static LocalDate parse(@Nonnull String localDateString,
                                @Nonnull String pattern,
                                @Nonnull Locale locale) {
    return LocalDate.parse(localDateString, DateTimeFormatters.getFormatter(pattern, locale));
  }

  @Nonnull
  public static String format(@Nonnull LocalDate localDate,
                              @Nonnull String pattern) {
    return format(localDate, pattern, LOCALE);
  }

  @Nonnull
  public static String format(@Nonnull LocalDate localDate,
                              @Nonnull String pattern,
                              @Nonnull Locale locale) {
    return localDate.format(DateTimeFormatters.getFormatter(pattern, locale));
  }

  public static void setOffset(ZoneOffset offset) {
    LocalDates.offset = offset;
  }
}
