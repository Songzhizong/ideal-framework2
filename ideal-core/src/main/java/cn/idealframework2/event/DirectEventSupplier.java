package cn.idealframework2.event;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/11/4
 */
public record DirectEventSupplier(
  @Nonnull Event event,
  @Nonnull String topic,
  @Nonnull String exchange
) {
}
