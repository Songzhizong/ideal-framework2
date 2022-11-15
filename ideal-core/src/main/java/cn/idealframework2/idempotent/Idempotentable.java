package cn.idealframework2.idempotent;

import javax.annotation.Nullable;

/**
 * 可幂等处理接口
 *
 * @author 宋志宗 on 2022/11/15
 */
@SuppressWarnings("SpellCheckingInspection")
public interface Idempotentable {


  /**
   * 获取幂等键
   *
   * @return 幂等键
   */
  @Nullable
  String idempotentKey();
}
