package cn.idealframework2.starter.idempotent.memory;

import cn.idealframework2.idempotent.coroutine.IdempotentHandlerFactory;
import cn.idealframework2.idempotent.coroutine.memory.CaffeineIdempotentHandlerFactory;
import cn.idealframework2.kotlin.KotlinModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * @author 宋志宗 on 2022/12/6
 */
@ConditionalOnClass({KotlinModel.class})
public class CoroutineMemoryIdempotentAutoConfigure {

  @Bean("coroutineIdempotentHandlerFactory")
  public IdempotentHandlerFactory idempotentHandlerFactory() {
    return new CaffeineIdempotentHandlerFactory();
  }
}
