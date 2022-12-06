package cn.idealframework2.event.coroutine.rabbit

import cn.idealframework2.event.coroutine.EventListenerRegistrar
import cn.idealframework2.event.coroutine.EventListenerRegistry
import cn.idealframework2.event.rabbit.RabbitEventListenerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.context.ApplicationContext

/**
 * @author 宋志宗 on 2022/12/6
 */
class CoroutineRabbitEventListenerRegistry(
  private val applicationContext: ApplicationContext,
  private val rabbitEventListenerManager: RabbitEventListenerManager
) : EventListenerRegistry, SmartInitializingSingleton {

  override fun <E> register(
    name: String,
    clazz: Class<E>,
    block: suspend CoroutineScope.(E) -> Unit
  ) {
    rabbitEventListenerManager.register(name, clazz) { event ->
      runBlocking { block.invoke(this, event) }
    }
  }

  override fun afterSingletonsInstantiated() {
    val beansOfType = applicationContext.getBeansOfType(EventListenerRegistrar::class.java)
    beansOfType.forEach { n, r -> r.registerEventListener(this) }
  }
}
