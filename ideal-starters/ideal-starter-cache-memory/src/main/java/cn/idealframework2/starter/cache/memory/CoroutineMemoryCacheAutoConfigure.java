package cn.idealframework2.starter.cache.memory;

import cn.idealframework2.cache.coroutine.CacheBuilderFactory;
import cn.idealframework2.cache.coroutine.memory.CaffeineCacheBuilderFactory;
import cn.idealframework2.kotlin.KotlinModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * @author 宋志宗 on 2022/12/5
 */
@ConditionalOnClass({KotlinModel.class})
public class CoroutineMemoryCacheAutoConfigure {

  @Bean("coroutineCacheBuilderFactory")
  public CacheBuilderFactory cacheBuilderFactory() {
    return new CaffeineCacheBuilderFactory();
  }
}
