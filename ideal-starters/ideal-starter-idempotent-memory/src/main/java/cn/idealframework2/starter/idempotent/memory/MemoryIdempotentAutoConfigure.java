package cn.idealframework2.starter.idempotent.memory;

import cn.idealframework2.idempotent.IdempotentHandlerFactory;
import cn.idealframework2.idempotent.memory.CaffeineIdempotentHandlerFactory;
import org.springframework.context.annotation.Bean;

/**
 * @author 宋志宗 on 2022/10/13
 */
public class MemoryIdempotentAutoConfigure {

  @Bean("idempotentHandlerFactory")
  public IdempotentHandlerFactory idempotentHandlerFactory() {
    return new CaffeineIdempotentHandlerFactory();
  }
}
