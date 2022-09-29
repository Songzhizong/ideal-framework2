package cn.idealframework2.trace.reactive;

import cn.idealframework2.trace.OperationLog;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/23
 */
public interface OperationLogStore {

  @Nonnull
  Mono<Boolean> save(@Nonnull OperationLog operationLog);
}
