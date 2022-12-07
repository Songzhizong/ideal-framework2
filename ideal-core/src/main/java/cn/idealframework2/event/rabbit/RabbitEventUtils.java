package cn.idealframework2.event.rabbit;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/12/6
 */
public class RabbitEventUtils {

  @Nonnull
  public static String defaultExchange() {
    return "event.exchange";
  }

  private RabbitEventUtils() {
  }
}
