package cn.idealframework2.autoconfigure.web;

import cn.idealframework2.json.JsonUtils;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/10/10
 */
public class MessageConverterProperties {
  /** 是否开启自定义消息转换器 */
  private boolean enableCustom = true;

  /** 序列化时是否忽略null */
  private boolean ignoreNull = false;

  /** 时间格式 */
  @Nonnull
  private String timePattern = JsonUtils.TIME_PATTERN;

  /** 日期格式 */
  @Nonnull
  private String datePattern = JsonUtils.DATE_PATTERN;

  /** 完整时间格式 */
  @Nonnull
  private String dateTimePattern = JsonUtils.DATE_TIME_PATTERN;

  public boolean isEnableCustom() {
    return enableCustom;
  }

  public MessageConverterProperties setEnableCustom(boolean enableCustom) {
    this.enableCustom = enableCustom;
    return this;
  }

  public boolean isIgnoreNull() {
    return ignoreNull;
  }

  public MessageConverterProperties setIgnoreNull(boolean ignoreNull) {
    this.ignoreNull = ignoreNull;
    return this;
  }

  @Nonnull
  public String getTimePattern() {
    return timePattern;
  }

  public MessageConverterProperties setTimePattern(@Nonnull String timePattern) {
    this.timePattern = timePattern;
    return this;
  }

  @Nonnull
  public String getDatePattern() {
    return datePattern;
  }

  public MessageConverterProperties setDatePattern(@Nonnull String datePattern) {
    this.datePattern = datePattern;
    return this;
  }

  @Nonnull
  public String getDateTimePattern() {
    return dateTimePattern;
  }

  public MessageConverterProperties setDateTimePattern(@Nonnull String dateTimePattern) {
    this.dateTimePattern = dateTimePattern;
    return this;
  }
}
