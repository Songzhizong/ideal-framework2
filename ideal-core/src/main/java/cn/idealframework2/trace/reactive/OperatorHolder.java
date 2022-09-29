package cn.idealframework2.trace.reactive;

import cn.idealframework2.trace.Operator;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/23
 */
public interface OperatorHolder {

  /**
   * 获取当前操作人信息, 可能为 Mono.empty()
   *
   * @return 操作人信息
   */
  @Nonnull
  Mono<Operator> get();
}
