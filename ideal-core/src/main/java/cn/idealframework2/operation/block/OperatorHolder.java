package cn.idealframework2.operation.block;

import cn.idealframework2.operation.Operator;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/10/9
 */
public interface OperatorHolder {

  /**
   * 获取当前操作人信息
   *
   * @return 操作人信息
   */
  @Nullable
  Operator get();
}
