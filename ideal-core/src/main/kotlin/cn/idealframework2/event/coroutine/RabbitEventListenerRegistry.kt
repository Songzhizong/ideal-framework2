package cn.idealframework2.event.coroutine

import cn.idealframework2.event.Event
import cn.idealframework2.event.EventListener
import cn.idealframework2.idempotent.coroutine.IdempotentHandler
import cn.idealframework2.json.JsonUtils
import cn.idealframework2.lang.StringUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.reactor.mono
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.config.SingletonBeanRegistry
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import reactor.core.Disposable
import reactor.rabbitmq.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap


/**
 * 事件监听器管理器
 *
 * @author 宋志宗 on 2022/4/2
 */
class RabbitEventListenerRegistry(
  private val defaultExchange: String,
  private val temporary: Boolean,
  private val queuePrefix: String,
  private val sender: Sender,
  private val receiver: Receiver,
  private val idempotentHandler: IdempotentHandler,
  private val singletonBeanRegistry: SingletonBeanRegistry
) : EventListenerRegistry {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(RabbitEventListener::class.java)
    private val registry = ConcurrentHashMap<String, RabbitEventListener<*>>()
  }

  @Suppress("UNCHECKED_CAST")
  override fun <E : Event> register(
    name: String,
    clazz: Class<E>,
    block: suspend CoroutineScope.(E) -> Unit
  ) {
    var exist = true
    val eventListener = registry.computeIfAbsent(name) {
      val annotation = clazz.getAnnotation(cn.idealframework2.event.annotation.Event::class.java)
        ?: throw RuntimeException("event 实现类: ${clazz.name} 缺少 @cn.idealframework2.event.annotation.Event 注解")
      var exchange = annotation.exchange
      if (StringUtils.isBlank(exchange)) {
        exchange = defaultExchange
      }
      val topic: String = annotation.topic

      log.info("register event listener: {}  ->  {}", name, topic)
      exist = false
      RabbitEventListener(
        exchange,
        topic,
        name,
        temporary,
        queuePrefix,
        sender,
        receiver,
        idempotentHandler,
        clazz,
        block
      )
    } as RabbitEventListener<E>
    if (exist) {
      val message = "监听器名称: $name 被重复注册"
      log.error(message)
      throw RuntimeException(message)
    }
    singletonBeanRegistry.registerSingleton(name, eventListener)
  }

  class RabbitEventListener<E : Event>(
    exchange: String,
    topic: String,
    private val name: String,
    temporary: Boolean,
    queuePrefix: String,
    sender: Sender,
    private val receiver: Receiver,
    private val idempotentHandler: IdempotentHandler,
    private val clazz: Class<E>,
    private val block: suspend CoroutineScope.(E) -> Unit
  ) : EventListener, DisposableBean, ApplicationRunner {
    private val finalQueueName: String
    private var disposable: Disposable? = null


    init {
      val queue = if (temporary) {
        finalQueueName =
          queuePrefix + "." + name + "." + UUID.randomUUID().toString().replace("-", "")
        QueueSpecification.queue(finalQueueName).durable(false).exclusive(false).autoDelete(true)
      } else {
        finalQueueName = "$queuePrefix.$name"
        QueueSpecification.queue(finalQueueName).durable(true).exclusive(false).autoDelete(false)
      }
      sender.declareQueue(queue).block()
      sender.bind(BindingSpecification.binding(exchange, topic, finalQueueName)).block()
    }

    private fun start() {
      val options = ConsumeOptions()
      disposable = receiver.consumeManualAck(finalQueueName, options)
        .flatMap { delivery ->
          mono {
            var ack = true
            try {
              val body = delivery.body
              val string = String(body, Charsets.UTF_8)
              val message = try {
                JsonUtils.parse(string, clazz)
              } catch (e: Exception) {
                log.info("反序列化事件消息出现异常 {} ", clazz.name, e)
                return@mono
              }
              val uuid = message.uuid
              val key = "$finalQueueName:$uuid"
              val tryLock = idempotentHandler.idempotent("$finalQueueName:$uuid")
              try {
                if (tryLock) {
                  block.invoke(this, message)
                }
              } catch (e: Exception) {
                ack = false
                try {
                  if (uuid.isNotBlank()) {
                    idempotentHandler.release(key)
                  }
                  log.warn("处理出现异常: ", e)
                  delay(1000)
                } catch (e: Exception) {
                  log.info("异常的后续处理出现异常: ", e)
                }
              }
            } finally {
              if (ack) {
                delivery.ack()
              } else {
                delivery.nack(true)
              }
            }
          }
        }.subscribe()
      log.info("start rabbit event listener: {}", name)
    }

    private fun stop() {
      if (disposable?.isDisposed == true) {
        return
      }
      disposable?.dispose()
    }

    override fun destroy() {
      this.stop()
    }

    override fun run(args: ApplicationArguments?) {
      this.start()
    }
  }
}
