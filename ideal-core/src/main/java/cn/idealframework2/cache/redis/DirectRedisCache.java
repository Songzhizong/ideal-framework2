package cn.idealframework2.cache.redis;

import cn.idealframework2.cache.CacheUtils;
import cn.idealframework2.cache.ReadCacheException;
import cn.idealframework2.cache.serialize.KeySerializer;
import cn.idealframework2.cache.serialize.ValueSerializer;
import cn.idealframework2.lang.StringUtils;
import cn.idealframework2.spring.RedisTemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2022/9/29
 */
public class DirectRedisCache<K, V> implements RedisCache<K, V> {
  private static final Logger log = LoggerFactory.getLogger(DirectRedisCache.class);
  private final String lockValue = UUID.randomUUID().toString().replace("-", "");
  /** 缓存前缀 */
  private final String cachePrefix;
  /** 执行回退函数时是否启用分布式锁 */
  private final boolean lock;
  /** 等待分布式锁的超时时间, 超过这个时间依然没能获取缓存的话则会抛出读取缓存异常 */
  private final int waitLockTimeoutMills;
  /** 是否缓存null值 */
  private final boolean cacheNull;
  /** null值的缓存过期时间 */
  private final Duration nullTimeout;
  /** 分布式锁的过期时间 */
  private final Duration lockTimeout;
  /** 缓存过期时间的秒值 */
  private final long timeoutSeconds;
  /** 缓存过期时间 */
  private final Duration timeout;
  /** 缓存最大过期时间, 如果这个值不为null, 则在默认过期时间和这个值之间取随机数作为缓存的过期时间 */
  @Nullable
  private final Long maxTimeoutSeconds;
  protected final KeySerializer<K> keySerializer;
  private final ValueSerializer<V> valueSerializer;
  private final StringRedisTemplate redisTemplate;

  public DirectRedisCache(boolean lock,
                          boolean cacheNull,
                          @Nonnull String redisPrefix,
                          int waitLockTimeoutMills,
                          @Nonnull Duration nullTimeout,
                          @Nonnull Duration lockTimeout,
                          long timeoutSeconds,
                          @Nullable Long maxTimeoutSeconds,
                          @Nonnull KeySerializer<K> keySerializer,
                          @Nonnull ValueSerializer<V> valueSerializer,
                          @Nonnull StringRedisTemplate redisTemplate) {
    this.lock = lock;
    this.cacheNull = cacheNull;
    this.waitLockTimeoutMills = waitLockTimeoutMills;
    this.lockTimeout = lockTimeout;
    this.timeoutSeconds = timeoutSeconds;
    this.timeout = Duration.ofSeconds(timeoutSeconds);
    this.maxTimeoutSeconds = maxTimeoutSeconds;
    this.keySerializer = keySerializer;
    this.valueSerializer = valueSerializer;
    this.nullTimeout = nullTimeout;
    this.redisTemplate = redisTemplate;
    if (StringUtils.isBlank(redisPrefix)) {
      this.cachePrefix = "";
    } else {
      if (redisPrefix.endsWith(CacheUtils.CACHE_CONNECTOR)) {
        this.cachePrefix = redisPrefix;
      } else {
        this.cachePrefix = redisPrefix + CacheUtils.CACHE_CONNECTOR;
      }
    }
  }

  @Nullable
  @Override
  public V getIfPresent(@Nonnull K key) {
    String redisKey = redisKey(key);
    String value = redisTemplate.opsForValue().get(redisKey);
    if (CacheUtils.isNull(value)) {
      return null;
    }
    return valueSerializer.deserialize(value);
  }

  @Nullable
  @Override
  public V get(@Nonnull K key, @Nonnull Function<K, V> function) {
    return get(key, function, null);
  }

  @Nullable
  private V get(@Nonnull K key, @Nonnull Function<K, V> function, @Nullable Long startLockTimestamp) {
    String redisKey = redisKey(key);
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    String value = ops.get(redisKey);
    if (value != null) {
      if (CacheUtils.isNull(value)) {
        return null;
      }
      return valueSerializer.deserialize(value);
    }
    String lockKey = null;
    try {
      if (lock) {
        if (startLockTimestamp == null) {
          startLockTimestamp = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - startLockTimestamp > waitLockTimeoutMills) {
          log.warn("等待缓存锁超时: " + redisKey);
          throw new ReadCacheException("读取缓存超时");
        }
        lockKey = lockKey(key);
        Boolean tryLock = ops.setIfAbsent(lockKey, lockValue, lockTimeout);
        if (tryLock == null || !tryLock) {
          try {
            TimeUnit.MILLISECONDS.sleep(10);
          } catch (InterruptedException e) {
            // ignore
          }
          return get(key, function, startLockTimestamp);
        }
      }
      V invoke = function.apply(key);
      if (invoke != null) {
        String serialize = valueSerializer.serialize(invoke);
        Duration timeout = calculateTimeout();
        ops.set(redisKey, serialize, timeout);
      } else if (cacheNull) {
        String nullValue = CacheUtils.NULL_VALUE;
        ops.set(redisKey, nullValue, nullTimeout);
      }
      return invoke;
    } finally {
      if (lockKey != null) {
        RedisTemplateUtils.unlock(redisTemplate, lockKey, lockValue);
      }
    }
  }

  @Override
  public void put(@Nonnull K key, @Nonnull V value) {
    String redisKey = redisKey(key);
    String serialize = valueSerializer.serialize(value);
    Duration timeout = calculateTimeout();
    redisTemplate.opsForValue().set(redisKey, serialize, timeout);
  }

  @Override
  public void putAll(@Nonnull Map<K, V> map) {
    map.forEach(this::put);
  }

  @Override
  public void invalidate(@Nonnull K key) {
    String redisKey = redisKey(key);
    redisTemplate.opsForValue().getAndDelete(redisKey);
  }

  @Override
  public void invalidateAll(@Nonnull Iterable<K> keys) {
    keys.forEach(this::invalidate);
  }

  @Nonnull
  private String redisKey(@Nonnull K key) {
    String serialize = keySerializer.serialize(key);
    return cachePrefix + serialize;
  }

  @Nonnull
  private String lockKey(@Nonnull K key) {
    String serialize = keySerializer.serialize(key);
    return "cache_lock:" + cachePrefix + serialize;
  }

  @Nonnull
  private Duration calculateTimeout() {
    if (maxTimeoutSeconds == null || maxTimeoutSeconds < timeoutSeconds) {
      return timeout;
    }
    long random = ThreadLocalRandom.current().nextLong(timeoutSeconds, maxTimeoutSeconds);
    return Duration.ofSeconds(random);
  }
}
