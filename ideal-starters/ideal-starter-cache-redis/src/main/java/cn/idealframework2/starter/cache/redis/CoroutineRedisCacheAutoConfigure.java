package cn.idealframework2.starter.cache.redis;

import cn.idealframework2.autoconfigure.cache.CacheProperties;
import cn.idealframework2.cache.coroutine.CacheBuilderFactory;
import cn.idealframework2.cache.coroutine.redis.RedisCacheBuilderFactory;
import cn.idealframework2.kotlin.KotlinModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/8/15
 */
@ConditionalOnClass({KotlinModel.class, ReactiveStringRedisTemplate.class})
public class CoroutineRedisCacheAutoConfigure {

  @Nullable
  @Bean("coroutineCacheBuilderFactory")
  public CacheBuilderFactory cacheBuilderFactory(@Nonnull CacheProperties cacheProperties,
                                                 @Nullable ReactiveStringRedisTemplate redisTemplate) {
    if (redisTemplate == null) {
      return null;
    }
    String prefix = cacheProperties.formattedPrefix();
    return new RedisCacheBuilderFactory(prefix, redisTemplate);
  }
}
