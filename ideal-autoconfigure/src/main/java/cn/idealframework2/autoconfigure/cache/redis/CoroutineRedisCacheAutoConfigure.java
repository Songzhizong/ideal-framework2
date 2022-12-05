package cn.idealframework2.autoconfigure.cache.redis;

import cn.idealframework2.autoconfigure.cache.CacheProperties;
import cn.idealframework2.cache.coroutine.CacheBuilderFactory;
import cn.idealframework2.cache.coroutine.redis.RedisCacheBuilderFactory;
import cn.idealframework2.starter.model.cache.redis.RedisCacheModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/8/15
 */
@ConditionalOnClass(RedisCacheModel.class)
@ConditionalOnBean(ReactiveStringRedisTemplate.class)
public class CoroutineRedisCacheAutoConfigure {

  @Bean("coroutineCacheBuilderFactory")
  public CacheBuilderFactory redisCacheBuilderFactory(@Nonnull CacheProperties cacheProperties,
                                                      @Nonnull ReactiveStringRedisTemplate redisTemplate) {
    String prefix = cacheProperties.formattedPrefix();
    return new RedisCacheBuilderFactory(prefix, redisTemplate);
  }
}
