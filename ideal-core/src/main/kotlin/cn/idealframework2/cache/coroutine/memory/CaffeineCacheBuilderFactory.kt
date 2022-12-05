package cn.idealframework2.cache.coroutine.memory

import cn.idealframework2.cache.coroutine.CacheBuilder
import cn.idealframework2.cache.coroutine.CacheBuilderFactory
import cn.idealframework2.cache.serialize.ValueSerializer

/**
 * @author 宋志宗 on 2022/12/5
 */
class CaffeineCacheBuilderFactory : CacheBuilderFactory {

  override fun <K : Any, V : Any> newBuilder(valueSerializer: ValueSerializer<V>): CacheBuilder<K, V> {
    return CaffeineCacheBuilder()
  }
}
