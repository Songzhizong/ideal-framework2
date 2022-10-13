package cn.idealframework2.idempotent;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * @author 宋志宗 on 2022/10/13
 */
public interface IdempotentHandlerFactory {

  /**
   * 创建幂等处理器
   *
   * @param namespace 名称空间
   * @param expire    过期时间
   * @return 幂等处理器
   */
  @Nonnull
  IdempotentHandler create(@Nonnull String namespace, @Nonnull Duration expire);
}
