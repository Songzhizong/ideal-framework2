package cn.idealframework2.starter.cache.redis;

import cn.idealframework2.autoconfigure.cache.CacheProperties;
import cn.idealframework2.cache.CacheBuilderFactory;
import cn.idealframework2.cache.redis.RedisCacheBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/29
 */
public class RedisCacheAutoConfigure {

  @Bean
  public CacheBuilderFactory cacheBuilderFactory(@Nonnull CacheProperties cacheProperties,
                                                      @Nonnull StringRedisTemplate redisTemplate) {
    String prefix = cacheProperties.formattedPrefix();
    return new RedisCacheBuilderFactory(prefix, redisTemplate);
  }
}
