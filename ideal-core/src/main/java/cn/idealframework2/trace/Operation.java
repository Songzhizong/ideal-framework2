package cn.idealframework2.trace;

import javax.annotation.Nonnull;
import java.lang.annotation.*;

/**
 * 操作注解
 *
 * @author 宋志宗 on 2022/9/23
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Operation {

  /** 操作名称 = name */
  String value() default "";

  /** 操作名称 = value */
  @Nonnull
  String name() default "";

  /** 系统名称 */
  @Nonnull
  String system() default "";
}
