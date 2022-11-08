package cn.idealframework2.event.coroutine

/**
 * @author 宋志宗 on 2022/11/8
 */
interface EventListenerRegistrar {

  /**
   * 注册事件监听器
   *
   * @param registry 事件监听注册中心
   * @author 宋志宗 on 2022/11/8
   */
  fun registerEventListener(registry: EventListenerRegistry)
}
