package cn.idealframework2.idempotent;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/10/13
 */
public interface IdempotentHandler {

  /**
   * 执行幂等判断
   *
   * @param key 幂等key
   * @return 是否已处理
   */
  boolean idempotent(@Nonnull String key);

  /**
   * 释放幂等key
   *
   * @param key 幂等key
   */
  void release(@Nonnull String key);
}
