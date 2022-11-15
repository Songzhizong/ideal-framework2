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
   * @return 如果返回true则代表可以进行后续的业务处理; 如果返回false则代表之前已经完成了处理,应该直接跳过
   */
  boolean idempotent(@Nonnull String key);

  /**
   * 释放幂等key
   *
   * @param key 幂等key
   */
  void release(@Nonnull String key);
}
