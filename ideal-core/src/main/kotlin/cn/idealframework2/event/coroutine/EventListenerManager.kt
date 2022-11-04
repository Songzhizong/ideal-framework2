package cn.idealframework2.event.coroutine

import cn.idealframework2.event.Event
import cn.idealframework2.event.EventListener
import kotlinx.coroutines.CoroutineScope

/**
 * 事件监听器管理器
 *
 * @author 宋志宗 on 2022/4/6
 */
interface EventListenerManager {

  /**
   * 监听事件
   *
   * @param name  监听器名称
   * @param clazz 事件类型
   * @param block 处理逻辑
   * @author 宋志宗 on 2022/4/8
   */
  fun <E : Event> listen(
    name: String,
    clazz: Class<E>,
    block: suspend CoroutineScope.(E) -> Unit
  ): EventListener
}
