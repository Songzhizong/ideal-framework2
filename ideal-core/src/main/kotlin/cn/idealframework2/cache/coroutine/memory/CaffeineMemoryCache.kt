package cn.idealframework2.cache.coroutine.memory

import cn.idealframework2.cache.memory.MemoryCache
import java.util.function.Function

/**
 * @author 宋志宗 on 2022/12/5
 */
class CaffeineMemoryCache<K : Any, V : Any> : MemoryCache<K, V> {

  override fun getIfPresent(key: K): V? {
    TODO("Not yet implemented")
  }

  override fun get(key: K, function: Function<K, V>): V? {
    TODO("Not yet implemented")
  }

  override fun put(key: K, value: V) {
    TODO("Not yet implemented")
  }

  override fun putAll(map: MutableMap<K, V>) {
    TODO("Not yet implemented")
  }

  override fun invalidate(key: K) {
    TODO("Not yet implemented")
  }

  override fun invalidateAll(keys: MutableIterable<K>) {
    TODO("Not yet implemented")
  }
}
