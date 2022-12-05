package cn.idealframework2.idempotent.memory;

import cn.idealframework2.idempotent.IdempotentHandler;
import cn.idealframework2.idempotent.IdempotentHandlerFactory;
import com.github.benmanes.caffeine.cache.Cache;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * @author 宋志宗 on 2022/12/6
 */
public class CaffeineIdempotentHandlerFactory implements IdempotentHandlerFactory {

  @Nonnull
  @Override
  public IdempotentHandler create(@Nonnull String namespace, @Nonnull Duration expire) {
    long millis = expire.toMillis();
    Cache<String, Boolean> cache = CaffeineIdempotentCacheFactory.get(millis);
    return new CaffeineIdempotentHandler(namespace, cache);
  }
}
