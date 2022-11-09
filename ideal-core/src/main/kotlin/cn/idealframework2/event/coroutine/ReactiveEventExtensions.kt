package cn.idealframework2.event.coroutine

import cn.idealframework2.event.EventSupplier
import cn.idealframework2.event.EventSuppliers
import cn.idealframework2.event.ReactiveEventPublisher
import kotlinx.coroutines.reactor.awaitSingleOrNull

suspend fun ReactiveEventPublisher.publishAndAwait(suppliers: Collection<EventSupplier>?) {
  this.publish(suppliers).awaitSingleOrNull()
}

suspend fun ReactiveEventPublisher.publishAndAwait(suppliers: EventSuppliers?) {
  this.publish(suppliers).awaitSingleOrNull()
}

suspend fun ReactiveEventPublisher.publishAndAwait(supplier: EventSupplier?) {
  this.publish(supplier).awaitSingleOrNull()
}
