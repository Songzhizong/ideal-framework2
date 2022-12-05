package cn.idealframework2.cache.coroutine

import cn.idealframework2.cache.serialize.ValueSerializer

/**
 * @author 宋志宗 on 2022/12/5
 */
interface CacheBuilderFactory {

  fun <K : Any, V : Any> newBuilder(valueSerializer: ValueSerializer<V>): CacheBuilder<K, V>
}
