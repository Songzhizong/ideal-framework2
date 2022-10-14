package cn.idealframework2.date;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @author 宋志宗 on 2020/8/30
 */
@SuppressWarnings("unused")
public final class DateTimes {
  private DateTimes() {
  }

  private static final Locale LOCALE = Locale.SIMPLIFIED_CHINESE;

  /** 2021-07-27T09:34:11.305Z */
  @SuppressWarnings("SpellCheckingInspection")
  public static final String TZ_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  public static final DateTimeFormatter TZ_DATE_TIME_FORMATTER
    = DateTimeFormatter.ofPattern(TZ_DATE_TIME, LOCALE);
  /** 2020-12-12 */
  public static final String YYYY_MM_DD = "yyyy-MM-dd";
  public static final DateTimeFormatter YYYY_MM_DD_FORMATTER
    = DateTimeFormatter.ofPattern(YYYY_MM_DD, LOCALE);
  /** 2020-12-12 19 */
  public static final String YYYY_MM_DD_HH = "yyyy-MM-dd HH";
  public static final DateTimeFormatter YYYY_MM_DD_HH_FORMATTER
    = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH, LOCALE);
  /** 2020-12-12 19:21 */
  public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
  public static final DateTimeFormatter YYYY_MM_DD_HH_MM_FORMATTER
    = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM, LOCALE);
  /** 2020-12-12 19:21:56 */
  public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
  public static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS_FORMATTER
    = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS, LOCALE);
  /** 2020-12-12 19:21:56.555 */
  public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS_SSS_FORMATTER
    = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS_SSS, LOCALE);
  /** 12-12 19:21:56 */
  public static final String MM_DD_HH_MM_SS = "MM-dd HH:mm:ss";
  public static final DateTimeFormatter MM_DD_HH_MM_SS_FORMATTER
    = DateTimeFormatter.ofPattern(MM_DD_HH_MM_SS, LOCALE);
  /** 12-12 19 */
  public static final String MM_DD_HH = "MM-dd HH";
  public static final DateTimeFormatter MM_DD_HH_FORMATTER
    = DateTimeFormatter.ofPattern(MM_DD_HH, LOCALE);
  /** 19:21:56 */
  public static final String HH_MM_SS = "HH:mm:ss";
  public static final DateTimeFormatter HH_MM_SS_FORMATTER
    = DateTimeFormatter.ofPattern(HH_MM_SS, LOCALE);
  /** 19:21 */
  public static final String HH_MM = "HH:mm";
  public static final DateTimeFormatter HH_MM_FORMATTER
    = DateTimeFormatter.ofPattern(HH_MM, LOCALE);

  private static ZoneOffset offset = ZoneOffset.of("+8");

  @Nonnull
  public static String format(@Nonnull LocalDateTime localDateTime, @Nonnull String pattern) {
    return format(localDateTime, pattern, LOCALE);
  }

  @Nonnull
  public static String format(@Nonnull LocalDateTime localDateTime,
                              @Nonnull String pattern, @Nonnull Locale locale) {
    return localDateTime.format(DateTimeFormatters.getFormatter(pattern, locale));
  }

  @Nonnull
  public static LocalDateTime parse(@Nonnull String dateTimeString, @Nonnull String pattern) {
    return parse(dateTimeString, pattern, LOCALE);
  }

  @Nonnull
  public static LocalDateTime parse(@Nonnull String dateTimeString,
                                    @Nonnull String pattern, @Nonnull Locale locale) {
    return LocalDateTime.parse(dateTimeString, DateTimeFormatters.getFormatter(pattern, locale));
  }

  @Nonnull
  public static LocalDateTime parse(long timestamp) {
    return parse(timestamp, offset);
  }

  @Nonnull
  public static LocalDateTime parse(long timestamp, ZoneOffset zoneOffset) {
    Instant instant = Instant.ofEpochMilli(timestamp);
    return LocalDateTime.ofInstant(instant, zoneOffset);
  }

  public static long getTimestamp(LocalDateTime localDateTime) {
    return getTimestamp(localDateTime, offset);
  }

  public static long getTimestamp(@Nonnull LocalDateTime localDateTime,
                                  @Nonnull ZoneOffset zoneOffset) {
    return localDateTime.toInstant(zoneOffset).toEpochMilli();
  }

  @Nonnull
  public static LocalDateTime now() {
    return now(offset);
  }

  @Nonnull
  public static LocalDateTime now(@Nonnull ZoneOffset zoneOffset) {
    return LocalDateTime.now(zoneOffset);
  }

  @Nonnull
  public static LocalDateTime ofDate(@Nonnull Date date) {
    Instant instant = date.toInstant();
    ZoneId zoneId = ZoneId.systemDefault();
    return instant.atZone(zoneId).toLocalDateTime();
  }

  /**
   * 获取年份
   *
   * @param localDateTime LocalDateTime
   * @return 2021
   */
  public static int getYear(@Nonnull LocalDateTime localDateTime) {
    return localDateTime.getYear();
  }

  /**
   * 获取年月
   *
   * @param localDateTime LocalDateTime
   * @return 202103
   */
  public static int getYearMonth(@Nonnull LocalDateTime localDateTime) {
    int year = localDateTime.getYear();
    int month = localDateTime.getMonthValue();
    return (year * 100) + month;
  }

  /**
   * 获取年月日
   *
   * @param localDateTime LocalDateTime
   * @return 20210308
   */
  public static int getYearMonthDay(@Nonnull LocalDateTime localDateTime) {
    int year = localDateTime.getYear();
    int month = localDateTime.getMonthValue();
    int day = localDateTime.getDayOfMonth();
    return (year * 10000) + (month * 100) + day;
  }


  /**
   * 根据起止时间计算出经历的时分秒信息
   *
   * @param start 起始时间戳
   * @param end   截止时间戳
   * @return 时分秒字符串: 1小时25分钟31.136秒
   */
  @Nonnull
  public static String calculateTimeDifference(long start, long end) {
    if (start == end) {
      return "0";
    }
    long time = Math.abs(end - start);
    long h = time / (1000 * 60 * 60);
    long m = time % (1000 * 60 * 60) / (1000 * 60);
    long s = time % (1000 * 60 * 60) % (1000 * 60) / 1000;
    long ms = time % (1000 * 60 * 60) % (1000 * 60) % 1000;
    StringBuilder sb = new StringBuilder();
    if (h != 0) {
      sb.append(h).append("小时");
    }
    if (m != 0) {
      sb.append(m).append("分钟");
    }
    sb.append(s).append(".").append(ms).append("秒");
    return sb.toString();
  }

  public static void setOffset(ZoneOffset offset) {
    DateTimes.offset = offset;
  }

  public static ZoneOffset getOffset() {
    return offset;
  }
}
