package cn.idealframework2.event;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/11/8
 */
public interface EventListenerRegistrar {

  /**
   * 注册事件监听器
   *
   * @param registry 事件监听注册中心
   * @author 宋志宗 on 2022/11/8
   */
  void registerEventListener(@Nonnull EventListenerRegistry registry);
}
