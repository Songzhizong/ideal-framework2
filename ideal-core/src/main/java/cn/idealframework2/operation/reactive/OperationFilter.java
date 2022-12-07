package cn.idealframework2.operation.reactive;

import cn.idealframework2.lang.StringUtils;
import cn.idealframework2.operation.Operation;
import cn.idealframework2.operation.OperationConstants;
import cn.idealframework2.operation.OperationLog;
import cn.idealframework2.operation.Operator;
import cn.idealframework2.spring.ExchangeUtils;
import cn.idealframework2.trace.TraceContext;
import cn.idealframework2.trace.reactive.TraceContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 宋志宗 on 2022/12/7
 */
public class OperationFilter implements Ordered, WebFilter {
  private static final Logger log = LoggerFactory.getLogger(OperationFilter.class);
  @Nonnull
  private final String system;
  @Nonnull
  private final OperatorHolder operatorHolder;
  @Nonnull
  private final OperationLogStore operationLogStore;
  @Nonnull
  private final RequestMappingHandlerMapping handlerMapping;

  public OperationFilter(@Nonnull String system,
                         @Nonnull OperatorHolder operatorHolder,
                         @Nonnull OperationLogStore operationLogStore,
                         @Nonnull RequestMappingHandlerMapping handlerMapping) {
    this.system = system;
    this.operatorHolder = operatorHolder;
    this.operationLogStore = operationLogStore;
    this.handlerMapping = handlerMapping;
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 2;
  }

  @Nonnull
  @Override
  public Mono<Void> filter(@Nonnull ServerWebExchange exchange, @Nonnull WebFilterChain chain) {
    Mono<Optional<HandlerMethod>> operationMono = handlerMapping.getHandler(exchange)
      .map(handler -> {
        if (handler instanceof HandlerMethod handlerMethod) {
          return Optional.of(handlerMethod);
        } else {
          return Optional.<HandlerMethod>empty();
        }
      }).switchIfEmpty(Mono.just(Optional.empty()));
    Mono<Optional<Operator>> operatorMono = operatorHolder.get()
      .map(Optional::ofNullable)
      .switchIfEmpty(Mono.just(Optional.empty()));
    Mono<Optional<TraceContext>> traceContextMono = TraceContextHolder.current()
      .defaultIfEmpty(Optional.empty());

    AtomicReference<String> exceptionMessageRef = new AtomicReference<>(null);
    AtomicReference<OperationLog> operationLogRef = new AtomicReference<>(null);

    return Mono.zip(operatorMono, operationMono, traceContextMono)
      .map(t -> {
        Operator operator = t.getT1().orElse(null);
        HandlerMethod handlerMethod = t.getT2().orElse(null);
        TraceContext traceContext = t.getT3().orElse(null);
        if (operator == null || handlerMethod == null) {
          return true;
        }
        OperationLog operationLog = initOperationLog(operator, handlerMethod, traceContext, exchange);
        operationLogRef.set(operationLog);
        return true;
      })
      .defaultIfEmpty(true)
      .flatMap(b ->
        chain.filter(exchange)
          .contextWrite(ctx -> {
            OperationLog value = operationLogRef.get();
            if (value == null) {
              return ctx;
            } else {
              return ctx.put(OperationConstants.OPERATION_KEY, value);
            }
          })
      )
      .doOnError(throwable -> {
        String msg = throwable.getMessage();
        if (StringUtils.isNotBlank(msg)) {
          exceptionMessageRef.set(msg);
        } else {
          String name = throwable.getClass().getName();
          exceptionMessageRef.set(name);
        }
      }).doFinally(t -> {
        OperationLog operationLog = operationLogRef.get();
        if (operationLog != null) {
          String msg = exceptionMessageRef.get();
          if (msg != null) {
            operationLog.setSuccess(false);
            operationLog.setMessage(msg);
          }
          long operationTime = operationLog.getOperationTime();
          long consuming = System.currentTimeMillis() - operationTime;
          operationLog.setConsuming(Math.toIntExact(consuming));
          //noinspection CallingSubscribeInNonBlockingScope
          operationLogStore.save(operationLog).subscribe();
        }
      });
  }

  @Nullable
  private OperationLog initOperationLog(@Nonnull Operator operator,
                                        @Nonnull HandlerMethod handlerMethod,
                                        @Nullable TraceContext traceContext,
                                        @Nonnull ServerWebExchange exchange) {
    Operation operation = handlerMethod.getMethodAnnotation(Operation.class);
    if (operation == null) {
      return null;
    }
    ServerHttpRequest request = exchange.getRequest();
    String requestPath = request.getURI().getPath();
    String name = operation.value();
    if (StringUtils.isBlank(name)) {
      name = operation.name();
    }
    if (StringUtils.isBlank(name)) {
      name = handlerMethod.getMethod().getName();
    }
    OperationLog operationLog = new OperationLog();
    if (traceContext != null) {
      operationLog.setTraceId(traceContext.getTraceId());
    }
    operationLog.setSystem(system);
    operationLog.setPlatform(operator.getPlatform());
    operationLog.setTenantId(operator.getTenantId());
    operationLog.setUserId(operator.getUserId());
    operationLog.setName(name);
    operationLog.setPath(requestPath);
    operationLog.setOriginalIp(ExchangeUtils.getRemoteAddress(exchange));
    String userAgent = ExchangeUtils.getUserAgent(exchange);
    if (userAgent != null) {
      operationLog.setUserAgent(userAgent);
    }
    operationLog.setOperationTime(System.currentTimeMillis());
    return operationLog;
  }
}
