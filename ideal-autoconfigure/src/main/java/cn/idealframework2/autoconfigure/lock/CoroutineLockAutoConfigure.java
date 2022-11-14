package cn.idealframework2.autoconfigure.lock;

import cn.idealframework2.lock.croroutine.GlobalLockFactory;
import cn.idealframework2.lock.croroutine.GlobalLockFactoryImpl;
import cn.idealframework2.starter.model.lock.coroutine.CoroutineLockModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/11/14
 */
@ConditionalOnClass(CoroutineLockModel.class)
public class CoroutineLockAutoConfigure {

  @Bean
  public GlobalLockFactory globalLockFactory(
    @Nonnull LockProperties lockProperties,
    @Nonnull ReactiveStringRedisTemplate redisTemplate) {
    String prefix = lockProperties.getPrefix();
    return new GlobalLockFactoryImpl(prefix, redisTemplate);
  }
}
