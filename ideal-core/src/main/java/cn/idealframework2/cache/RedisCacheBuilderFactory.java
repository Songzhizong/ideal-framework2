package cn.idealframework2.cache;

import cn.idealframework2.cache.serialize.ValueSerializer;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/9/29
 */
@SuppressWarnings("unused")
public class RedisCacheBuilderFactory {
  @Nullable
  private final String prefix;
  @Nonnull
  private final StringRedisTemplate redisTemplate;

  public RedisCacheBuilderFactory(@Nullable String prefix,
                                  @Nonnull StringRedisTemplate redisTemplate) {
    this.prefix = prefix;
    this.redisTemplate = redisTemplate;
  }

  @Nonnull
  public <K, V> RedisCacheBuilder<K, V> newBuilder(@Nonnull ValueSerializer<V> valueSerializer) {
    return new RedisCacheBuilder<>(prefix, valueSerializer, redisTemplate);
  }
}
