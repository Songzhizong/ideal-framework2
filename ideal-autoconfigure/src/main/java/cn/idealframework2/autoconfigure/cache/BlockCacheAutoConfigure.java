package cn.idealframework2.autoconfigure.cache;

import cn.idealframework2.cache.RedisCacheBuilderFactory;
import cn.idealframework2.starter.model.cache.CacheModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/29
 */
@ConditionalOnClass(CacheModel.class)
public class BlockCacheAutoConfigure {


  @Bean("redisCacheBuilderFactory")
  public RedisCacheBuilderFactory redisCacheBuilderFactory(@Nonnull CacheProperties cacheProperties,
                                                           @Nonnull StringRedisTemplate redisTemplate) {
    String prefix = cacheProperties.formattedPrefix();
    return new RedisCacheBuilderFactory(prefix, redisTemplate);
  }
}
