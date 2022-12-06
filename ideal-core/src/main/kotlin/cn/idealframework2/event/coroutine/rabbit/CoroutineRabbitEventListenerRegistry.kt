package cn.idealframework2.event.coroutine.rabbit

import cn.idealframework2.event.coroutine.EventListenerRegistry
import cn.idealframework2.event.rabbit.RabbitEventListenerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

/**
 * @author 宋志宗 on 2022/12/6
 */
class CoroutineRabbitEventListenerRegistry(
  private val rabbitEventListenerManager: RabbitEventListenerManager
) : EventListenerRegistry {

  override fun <E> register(
    name: String,
    clazz: Class<E>,
    block: suspend CoroutineScope.(E) -> Unit
  ) {
    rabbitEventListenerManager.register(name, clazz) { event ->
      runBlocking { block.invoke(this, event) }
    }
  }

}
