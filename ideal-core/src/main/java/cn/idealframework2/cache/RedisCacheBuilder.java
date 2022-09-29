package cn.idealframework2.cache;

import cn.idealframework2.cache.serialize.KeySerializer;
import cn.idealframework2.cache.serialize.StringKeySerializer;
import cn.idealframework2.cache.serialize.ValueSerializer;
import cn.idealframework2.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;

/**
 * @author 宋志宗 on 2022/9/29
 */
public class RedisCacheBuilder<K, V> {
  @Nullable
  private final String prefix;
  @Nonnull
  private final ValueSerializer<V> valueSerializer;
  @Nonnull
  private final StringRedisTemplate redisTemplate;

  private boolean multiLevel = false;
  @Nullable
  private Long memoryCacheSize = null;
  @Nullable
  private Duration memoryCacheTimeout = null;
  private boolean cacheNull = false;
  @Nonnull
  private Duration nullTimeout = Duration.ofSeconds(5);
  private boolean lock = false;
  @Nonnull
  private Duration lockTimeout = Duration.ofSeconds(30);
  @Nonnull
  private Duration waitLockTimeout = Duration.ofSeconds(2);
  private long timeoutSeconds = 2592000L;
  @Nullable
  private Long maxTimeoutSeconds = null;
  @Nonnull
  private KeySerializer<K> keySerializer = new StringKeySerializer<>();

  public RedisCacheBuilder(@Nullable String prefix,
                           @Nonnull ValueSerializer<V> valueSerializer,
                           @Nonnull StringRedisTemplate redisTemplate) {
    this.prefix = prefix;
    this.valueSerializer = valueSerializer;
    this.redisTemplate = redisTemplate;
  }

  /**
   * 缓存null值
   *
   * @param timeout null值的缓存超时时间
   */
  @Nonnull
  public RedisCacheBuilder<K, V> cacheNull(@Nonnull Duration timeout) {
    this.cacheNull = true;
    this.nullTimeout = timeout;
    return this;
  }

  /**
   * 开启多级缓存, 一级缓存为内存, 二级缓存为redis
   *
   * @param size    内存最大缓存数量
   * @param timeout 内存缓存的超时时间
   */
  @Nonnull
  public RedisCacheBuilder<K, V> multiLevel(long size, @Nonnull Duration timeout) {
    this.multiLevel = true;
    this.memoryCacheSize = size;
    this.memoryCacheTimeout = timeout;
    return this;
  }

  /**
   * 启用分布式锁
   *
   * @param lockTimeout      锁的超时时间
   * @param cacheNullTimeout 为了防止缓存穿透, 只有在允许缓存null的前提下才能开启分布式锁, 这个参数用于控制null值的缓存时间
   * @param waitTimeout      等待锁的超时时间, 如果超过这个时间依然没能读取到缓存, 则抛出{@link ReadCacheException}
   */
  @Nonnull
  public RedisCacheBuilder<K, V> enableLock(@Nonnull Duration lockTimeout,
                                            @Nonnull Duration cacheNullTimeout,
                                            @Nonnull Duration waitTimeout) {
    this.lock = true;
    this.lockTimeout = lockTimeout;
    this.waitLockTimeout = waitTimeout;
    this.cacheNull(cacheNullTimeout);
    return this;
  }

  /**
   * 设置缓存过期时间
   *
   * @param expireAfterWrite 写入后的过期时间
   */
  @Nonnull
  public RedisCacheBuilder<K, V> expireAfterWrite(@Nonnull Duration expireAfterWrite) {
    this.timeoutSeconds = Math.max(expireAfterWrite.toSeconds(), 1);
    return this;
  }

  /**
   * 设置缓存过期时间
   *
   * @param minTimeout 最小过期时间
   * @param maxTimeout 最大过期时间
   */
  @Nonnull
  public RedisCacheBuilder<K, V> expireAfterWrite(@Nonnull Duration minTimeout,
                                                  @Nonnull Duration maxTimeout) {
    this.timeoutSeconds = Math.max(minTimeout.toSeconds(), 1);
    this.maxTimeoutSeconds = Math.max(maxTimeout.toSeconds(), 1);
    return this;
  }

  @Nonnull
  public RedisCache<K, V> build(@Nonnull String namespace) {
    String redisPrefix = generateRedisPrefix(namespace);
    DirectRedisCache<K, V> directRedisCache = new DirectRedisCache<>(
      lock, cacheNull, redisPrefix,
      Math.toIntExact(waitLockTimeout.toMillis()),
      nullTimeout, lockTimeout, timeoutSeconds,
      maxTimeoutSeconds, keySerializer, valueSerializer, redisTemplate
    );
    if (!multiLevel) {
      return directRedisCache;
    }
    throw new UnsupportedOperationException("暂不支持多级缓存");
  }

  @Nonnull
  private String generateRedisPrefix(@Nonnull String namespace) {
    if (StringUtils.isBlank(prefix)) {
      return namespace;
    }
    if (prefix.endsWith(CacheUtils.CACHE_CONNECTOR)) {
      return prefix + namespace;
    }
    return prefix + CacheUtils.CACHE_CONNECTOR + namespace;
  }
}
