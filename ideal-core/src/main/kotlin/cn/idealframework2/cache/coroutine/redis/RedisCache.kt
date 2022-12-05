package cn.idealframework2.cache.coroutine.redis

import cn.idealframework2.cache.coroutine.Cache

/**
 * @author 宋志宗 on 2022/8/15
 */
interface RedisCache<K : Any, V : Any> : Cache<K, V>
