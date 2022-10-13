package cn.idealframework2.autoconfigure.idempotent.block;

import cn.idealframework2.autoconfigure.cache.CacheProperties;
import cn.idealframework2.idempotent.IdempotentHandlerFactory;
import cn.idealframework2.idempotent.RedisTemplateIdempotentHandlerFactory;
import cn.idealframework2.starter.model.idempotent.IdempotentModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/10/13
 */
@ConditionalOnClass({IdempotentModel.class})
public class RedisTemplateIdempotentAutoConfigure {

  @Bean
  public IdempotentHandlerFactory idempotentHandlerFactory(@Nonnull CacheProperties cacheProperties,
                                                           @Nonnull StringRedisTemplate redisTemplate) {
    String prefix = cacheProperties.formatKey("idempotent:");
    return new RedisTemplateIdempotentHandlerFactory(prefix, redisTemplate);
  }
}
