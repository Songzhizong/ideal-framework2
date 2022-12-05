package cn.idealframework2.idempotent.memory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author 宋志宗 on 2022/12/6
 */
public class CaffeineIdempotentCacheFactory {
  private static final ConcurrentMap<Long, Cache<String, Boolean>> MAP = new ConcurrentHashMap<>();

  @Nonnull
  public static Cache<String, Boolean> get(long timeoutMillis) {
    return MAP.computeIfAbsent(timeoutMillis,
      k -> Caffeine.newBuilder()
        .expireAfterAccess(Duration.ofMillis(timeoutMillis))
        .maximumSize(10_000)
        .build()
    );
  }
}
