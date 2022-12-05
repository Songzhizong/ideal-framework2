package cn.idealframework2.autoconfigure.cache.redis;

import cn.idealframework2.autoconfigure.cache.CacheProperties;
import cn.idealframework2.cache.CacheBuilderFactory;
import cn.idealframework2.cache.redis.RedisCacheBuilderFactory;
import cn.idealframework2.starter.model.cache.redis.RedisCacheModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/29
 */
@ConditionalOnClass(RedisCacheModel.class)
public class RedisCacheAutoConfigure {

  @Bean("redisCacheBuilderFactory")
  public CacheBuilderFactory redisCacheBuilderFactory(@Nonnull CacheProperties cacheProperties,
                                                      @Nonnull StringRedisTemplate redisTemplate) {
    String prefix = cacheProperties.formattedPrefix();
    return new RedisCacheBuilderFactory(prefix, redisTemplate);
  }
}
