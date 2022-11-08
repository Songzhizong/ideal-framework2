package cn.idealframework2.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2022/11/8
 */
public class ObjectUtils {

  @Nullable
  public static <In, Out> Out map(@Nullable In in, @Nonnull Function<In, Out> function) {
    if (in == null) {
      return null;
    }
    return function.apply(in);
  }
}
