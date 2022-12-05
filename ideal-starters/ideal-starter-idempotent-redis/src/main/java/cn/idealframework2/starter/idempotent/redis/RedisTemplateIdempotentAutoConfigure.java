package cn.idealframework2.starter.idempotent.redis;

import cn.idealframework2.autoconfigure.cache.CacheProperties;
import cn.idealframework2.idempotent.IdempotentHandlerFactory;
import cn.idealframework2.idempotent.redis.RedisTemplateIdempotentHandlerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/10/13
 */
public class RedisTemplateIdempotentAutoConfigure {

  @Bean("idempotentHandlerFactory")
  public IdempotentHandlerFactory idempotentHandlerFactory(@Nonnull CacheProperties cacheProperties,
                                                           @Nonnull StringRedisTemplate redisTemplate) {
    String prefix = cacheProperties.formatKey("idempotent:");
    return new RedisTemplateIdempotentHandlerFactory(prefix, redisTemplate);
  }
}
