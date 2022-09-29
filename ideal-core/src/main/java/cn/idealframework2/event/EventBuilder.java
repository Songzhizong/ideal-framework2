package cn.idealframework2.event;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/8/14
 */
public interface EventBuilder extends EventSupplier {

  /**
   * 构造事件对象
   *
   * @return 事件对象
   */
  @Nonnull
  Event build();

  /**
   * 获取事件对象
   *
   * @return 事件对象
   */
  @Nonnull
  @Override
  default Event get() {
    return build();
  }
}
