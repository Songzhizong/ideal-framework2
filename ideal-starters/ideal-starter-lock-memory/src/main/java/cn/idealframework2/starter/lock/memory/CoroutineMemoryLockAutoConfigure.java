package cn.idealframework2.starter.lock.memory;

import cn.idealframework2.kotlin.KotlinModel;
import cn.idealframework2.lock.croroutine.GlobalLockFactory;
import cn.idealframework2.lock.croroutine.memory.MemoryGlobalLockFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * @author 宋志宗 on 2022/12/5
 */
@ConditionalOnClass({KotlinModel.class})
public class CoroutineMemoryLockAutoConfigure {

  @Bean
  public GlobalLockFactory globalLockFactory() {
    return new MemoryGlobalLockFactory();
  }
}
