package cn.idealframework2.event;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/10/22
 */
public interface Event extends EventSupplier {

  /**
   * 获取事件的唯一id
   *
   * @return 事件唯一id
   */
  @Nonnull
  String getUuid();

  /**
   * 获取时间产生的时间
   *
   * @return 事件产生事件, 单位毫秒
   */
  long getEventTime();

  /**
   * 获取事件对象
   *
   * @return 事件对象
   */
  @Nonnull
  @Override
  default Event get() {
    return this;
  }
}
