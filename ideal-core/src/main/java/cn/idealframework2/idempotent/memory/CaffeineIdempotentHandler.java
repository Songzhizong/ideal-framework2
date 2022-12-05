package cn.idealframework2.idempotent.memory;

import cn.idealframework2.idempotent.IdempotentHandler;
import com.github.benmanes.caffeine.cache.Cache;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/12/6
 */
public class CaffeineIdempotentHandler implements IdempotentHandler {

  @Nonnull
  private final String namespace;
  @Nonnull
  private final Cache<String, Boolean> cache;

  public CaffeineIdempotentHandler(@Nonnull String namespace,
                                   @Nonnull Cache<String, Boolean> cache) {
    this.namespace = namespace;
    this.cache = cache;
  }

  @Override
  public boolean idempotent(@Nonnull String key) {
    boolean[] flag = {false};
    cache.get(genCacheKey(key), k -> {
      flag[0] = true;
      return true;
    });
    return flag[0];
  }

  @Override
  public void release(@Nonnull String key) {
    cache.invalidate(genCacheKey(key));
  }

  @Nonnull
  private String genCacheKey(@Nonnull String key) {
    return namespace + "$:$" + key;
  }
}
