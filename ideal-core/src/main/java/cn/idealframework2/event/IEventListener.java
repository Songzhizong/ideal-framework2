package cn.idealframework2.event;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/11/4
 */
public interface IEventListener<E> {

  /**
   * 监听器名称
   *
   * @return 监听器名称
   */
  @Nonnull
  String listenerName();

  /**
   * 对事件进行处理
   *
   * @param event 事件对象
   */
  void execute(@Nonnull E event);
}
