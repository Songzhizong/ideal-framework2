package cn.idealframework2.idempotent.redis;

import cn.idealframework2.idempotent.IdempotentHandler;
import cn.idealframework2.idempotent.IdempotentHandlerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * 基于redisTemplate的幂等处理器工厂
 *
 * @author 宋志宗 on 2022/10/13
 */
public class RedisTemplateIdempotentHandlerFactory implements IdempotentHandlerFactory {
  private final String prefix;
  private final StringRedisTemplate redisTemplate;

  public RedisTemplateIdempotentHandlerFactory(String prefix, StringRedisTemplate redisTemplate) {
    this.prefix = prefix;
    this.redisTemplate = redisTemplate;
  }

  @Nonnull
  @Override
  public IdempotentHandler create(@Nonnull String namespace, @Nonnull Duration expire) {
    String finalPrefix = prefix + namespace;
    return new RedisTemplateIdempotentHandler(finalPrefix, expire, redisTemplate);
  }
}
