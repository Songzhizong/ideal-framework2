package cn.idealframework2.operation.reactive;

import cn.idealframework2.exception.InternalServerException;
import cn.idealframework2.operation.OperationConstants;
import cn.idealframework2.operation.OperationLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author 宋志宗 on 2022/12/7
 */
public final class Operations {
  private static final Logger log = LoggerFactory.getLogger(Operations.class);

  @Nonnull
  public static Mono<Optional<OperationLog>> current() {
    return Mono.deferContextual(ctx -> {
      boolean hasKey = ctx.hasKey(OperationConstants.OPERATION_KEY);
      if (!hasKey) {
        return Mono.just(Optional.empty());
      }
      Object o = ctx.get(OperationConstants.OPERATION_KEY);
      if (o instanceof OperationLog operationLog) {
        return Mono.just(Optional.of(operationLog));
      }
      String className = o.getClass().getName();
      log.error("从上下文获取到的OperationLog值实际类型为: " + className);
      return Mono.error(new InternalServerException("value not instanceof OperationLog"));
    });
  }

  private Operations() {
  }
}
