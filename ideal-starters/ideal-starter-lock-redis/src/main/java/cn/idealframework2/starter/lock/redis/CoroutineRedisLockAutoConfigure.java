package cn.idealframework2.starter.lock.redis;

import cn.idealframework2.autoconfigure.lock.LockProperties;
import cn.idealframework2.kotlin.KotlinModel;
import cn.idealframework2.lock.croroutine.GlobalLockFactory;
import cn.idealframework2.lock.croroutine.redis.RedisGlobalLockFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/11/14
 */
@ConditionalOnClass({ReactiveStringRedisTemplate.class, KotlinModel.class})
public class CoroutineRedisLockAutoConfigure {

  @Bean
  @Nullable
  public GlobalLockFactory globalLockFactory(
    @Nonnull LockProperties lockProperties,
    @Nullable ReactiveStringRedisTemplate redisTemplate) {
    if (redisTemplate == null) {
      return null;
    }
    String prefix = lockProperties.getPrefix();
    return new RedisGlobalLockFactory(prefix, redisTemplate);
  }
}
