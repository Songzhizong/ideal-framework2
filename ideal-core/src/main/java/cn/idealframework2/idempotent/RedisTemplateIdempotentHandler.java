package cn.idealframework2.idempotent;

import cn.idealframework2.spring.RedisTemplateUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.UUID;

/**
 * 基于RedisTemplate的幂等处理器
 *
 * @author 宋志宗 on 2022/10/13
 */
public class RedisTemplateIdempotentHandler implements IdempotentHandler {
  private final String lockValue = UUID.randomUUID().toString().replace("-", "");
  private final String prefix;
  private final Duration expire;
  private final StringRedisTemplate redisTemplate;

  public RedisTemplateIdempotentHandler(@Nonnull String prefix,
                                        @Nonnull Duration expire,
                                        @Nonnull StringRedisTemplate redisTemplate) {
    this.prefix = prefix;
    this.expire = expire;
    this.redisTemplate = redisTemplate;
  }

  @Override
  public boolean idempotent(@Nonnull String key) {
    String redisKey = prefix + ":" + key;
    return RedisTemplateUtils.tryLock(redisTemplate, redisKey, lockValue, expire);
  }

  @Override
  public void release(@Nonnull String key) {
    String redisKey = prefix + ":" + key;
    RedisTemplateUtils.unlock(redisTemplate, redisKey, lockValue);
  }
}
