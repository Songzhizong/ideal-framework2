package cn.idealframework2.event.annotation;

import javax.annotation.Nonnull;
import java.lang.annotation.*;

/**
 * @author 宋志宗 on 2022/11/4
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Event {


  /** 主题名称 */
  @Nonnull
  String topic();

  /** 交换区名称 */
  @Nonnull
  String exchange() default "";
}
