package cn.idealframework2.cache;

import cn.idealframework2.lang.StringUtils;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/8/25
 */
public class CacheUtils {
  public static final String NULL_VALUE = "::$$::_null_::$$::";
  public static final String CACHE_CONNECTOR = ":";

  public static boolean isNull(@Nullable Object value) {
    if (value == null) {
      return true;
    }
    if (value instanceof CharSequence charSequence) {
      if (StringUtils.isBlank(charSequence)) {
        return true;
      }
      //noinspection RedundantIfStatement
      if (StringUtils.equals(NULL_VALUE, charSequence)) {
        return true;
      }
    }
    return false;
  }
}
