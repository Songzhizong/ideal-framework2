package cn.idealframework2.trace.block;

import cn.idealframework2.trace.OperationLog;

import javax.annotation.Nonnull;

/**
 * 操作日志存储库
 *
 * @author 宋志宗 on 2022/10/9
 */
public interface OperationLogStore {

  /**
   * 保存操作日志
   *
   * @param operationLog 操作日志
   */
  void save(@Nonnull OperationLog operationLog);
}
