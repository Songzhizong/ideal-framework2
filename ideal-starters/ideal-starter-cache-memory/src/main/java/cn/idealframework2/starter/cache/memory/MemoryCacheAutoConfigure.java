package cn.idealframework2.starter.cache.memory;

import cn.idealframework2.cache.CacheBuilderFactory;
import cn.idealframework2.cache.memory.CaffeineCacheBuilderFactory;
import org.springframework.context.annotation.Bean;

/**
 * @author 宋志宗 on 2022/12/5
 */
public class MemoryCacheAutoConfigure {

  @Bean("blockCacheBuilderFactory")
  public CacheBuilderFactory cacheBuilderFactory() {
    return new CaffeineCacheBuilderFactory();
  }
}
