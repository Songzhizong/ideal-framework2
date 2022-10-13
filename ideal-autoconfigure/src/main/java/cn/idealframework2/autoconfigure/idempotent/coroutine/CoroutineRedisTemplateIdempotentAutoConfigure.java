package cn.idealframework2.autoconfigure.idempotent.coroutine;

import cn.idealframework2.autoconfigure.cache.CacheProperties;
import cn.idealframework2.idempotent.coroutine.IdempotentHandlerFactory;
import cn.idealframework2.idempotent.coroutine.RedisTemplateIdempotentHandlerFactory;
import cn.idealframework2.starter.model.idempotent.coroutine.CoroutineIdempotentModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/10/13
 */
@ConditionalOnClass({CoroutineIdempotentModel.class})
public class CoroutineRedisTemplateIdempotentAutoConfigure {

  @Bean
  public IdempotentHandlerFactory idempotentHandlerFactory(@Nonnull CacheProperties cacheProperties,
                                                           @Nonnull ReactiveStringRedisTemplate redisTemplate) {
    String prefix = cacheProperties.formatKey("idempotent:");
    return new RedisTemplateIdempotentHandlerFactory(prefix, redisTemplate);
  }
}
