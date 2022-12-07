package cn.idealframework2.operation.reactive;

import cn.idealframework2.operation.OperationLog;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/23
 */
public interface OperationLogStore {

  /**
   * 保存操作日志
   *
   * @param operationLog 操作日志
   * @return 执行结果
   */
  @Nonnull
  Mono<Boolean> save(@Nonnull OperationLog operationLog);
}
