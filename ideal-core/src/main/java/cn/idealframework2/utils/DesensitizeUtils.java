package cn.idealframework2.utils;

import cn.idealframework2.lang.StringUtils;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/6/7
 */
public class DesensitizeUtils {

  private DesensitizeUtils() {
  }

  /**
   * 将字符串中的一部分替换为指定字符
   *
   * @param source       原字符串
   * @param prefixLength 左侧原始数据长度
   * @param suffixLength 右侧原始数据长度
   * @param c            替换符
   * @return 脱敏后的字符串
   * @author 宋志宗 on 2018-11-15 22:53
   */
  @Nullable
  public static String desensitize(@Nullable String source,
                                   int prefixLength, int suffixLength, char c) {
    if (StringUtils.isBlank(source)) {
      return source;
    }
    int length = source.length();
    if (length > (prefixLength + suffixLength)) {
      char[] chars = source.toCharArray();
      int start = length - suffixLength - 1;
      int termination = prefixLength - 1;
      for (int i = start; i > termination; i--) {
        chars[i] = c;
      }
      return String.valueOf(chars);
    } else {
      return source;
    }
  }


  /**
   * 将字符串中的一部分替换为指定字符
   *
   * @param source       原字符串
   * @param prefixLength 左侧原始数据长度
   * @param suffixLength 右侧原始数据长度
   * @return 脱敏后的字符串
   * @author 宋志宗 on 2018-11-15 22:53
   */
  public static String desensitize(String source, int prefixLength, int suffixLength) {
    return desensitize(source, prefixLength, suffixLength, '*');
  }

  /**
   * 保留手机号前后各两位,剩余部分替换为 *
   *
   * @param phone 手机号
   * @return 脱敏后的手机号
   * @author 宋志宗 on 2018-11-15 22:53
   */
  @Nullable
  public static String desensitizePhone(@Nullable String phone) {
    return desensitize(phone, 3, 4);
  }

  /**
   * 保留身份证号前后各三位,剩余部分替换为 *
   *
   * @param idCard 身份证号码
   * @return 脱敏后的手机号
   * @author 宋志宗 on 2018-11-15 22:53
   */
  @Nullable
  public static String desensitizeIdCard(@Nullable String idCard) {
    return desensitize(idCard, 3, 3);
  }

  /**
   * 将邮箱中的一部分替换为 *
   *
   * @param email 邮箱
   * @return 替换后的邮箱
   * @author 宋志宗 on 2018-11-15 22:53
   */
  @Nullable
  public static String desensitizeEmail(@Nullable String email) {
    final int viewCount = 4;
    if (StringUtils.isBlank(email)) {
      return null;
    }
    int length = email.length();
    int index = email.lastIndexOf("@");
    if (index > viewCount) {
      return desensitize(email, viewCount, length - index);
    } else {
      return desensitize(email, 0, length - index);
    }
  }

  @Nullable
  @SuppressWarnings("AlibabaUndefineMagicConstant")
  public static String desensitizeName(@Nullable String name) {
    if (StringUtils.isBlank(name)) {
      return name;
    }
    int length = name.length();
    if (length == 1) {
      return "*";
    }
    if (length == 2) {
      return String.valueOf(name.charAt(0)) + '*';
    }
    return desensitize(name, 1, 1, '*');
  }

}
