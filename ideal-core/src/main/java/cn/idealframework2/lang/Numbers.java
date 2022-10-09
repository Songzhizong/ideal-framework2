package cn.idealframework2.lang;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

/**
 * @author 宋志宗 on 2021/12/29
 */
@SuppressWarnings("unused")
public final class Numbers {
  private static final Pattern IS_NUMBER_PATTERN = Pattern.compile("^[-+]?0*\\d{1,19}$");

  private Numbers() {
  }

  public static boolean isNumber(@Nullable String str) {
    if (StringUtils.isBlank(str)) {
      return false;
    }
    boolean isNumber = IS_NUMBER_PATTERN.matcher(str).matches();
    if (isNumber) {
      try {
        Long.parseLong(str);
      } catch (NumberFormatException e) {
        isNumber = false;
      }
    }
    return isNumber;
  }
}
