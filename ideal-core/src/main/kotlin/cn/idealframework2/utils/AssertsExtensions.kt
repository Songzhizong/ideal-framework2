package cn.idealframework2.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <T> T?.requireNonnull(lazyMessage: () -> String): T {
  contract {
    returns() implies (this@requireNonnull != null)
  }
  if (this == null) {
    val message = lazyMessage()
    throw Asserts.AssertException(message)
  }
  return this
}

@OptIn(ExperimentalContracts::class)
inline fun <T : CharSequence> T?.requireNotBlank(lazyMessage: () -> String): T {
  contract {
    returns() implies (this@requireNotBlank != null)
  }
  if (this.isNullOrBlank()) {
    val message = lazyMessage()
    throw Asserts.AssertException(message)
  }
  return this
}

@OptIn(ExperimentalContracts::class)
inline fun <T : Collection<E>, E> T?.requireNotEmpty(lazyMessage: () -> String): T {
  contract {
    returns() implies (this@requireNotEmpty != null)
  }
  if (this.isNullOrEmpty()) {
    val message = lazyMessage()
    throw Asserts.AssertException(message)
  }
  return this
}

@OptIn(ExperimentalContracts::class)
inline fun <T : Map<K, V>, K, V> T?.requireNotEmpty(lazyMessage: () -> String): T {
  contract {
    returns() implies (this@requireNotEmpty != null)
  }
  if (this.isNullOrEmpty()) {
    val message = lazyMessage()
    throw Asserts.AssertException(message)
  }
  return this
}
