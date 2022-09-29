package cn.idealframework2.trace.reactive;

import cn.idealframework2.lang.StringUtils;
import cn.idealframework2.lang.Tuple;
import cn.idealframework2.spring.ExchangeUtils;
import cn.idealframework2.trace.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 宋志宗 on 2022/9/22
 */
public class TraceContextFilter implements Ordered, WebFilter {
  private static final Log log = LogFactory.getLog(TraceContextFilter.class);
  @Nonnull
  private final String system;
  @Nullable
  private final OperatorHolder operatorHolder;
  @Nullable
  private final OperationLogStore operationLogStore;
  @Nonnull
  private final RequestMappingHandlerMapping handlerMapping;

  public TraceContextFilter(@Nonnull String system,
                            @Nullable OperatorHolder operatorHolder,
                            @Nullable OperationLogStore operationLogStore,
                            @Nonnull RequestMappingHandlerMapping handlerMapping) {
    this.system = system;
    this.operatorHolder = operatorHolder;
    this.operationLogStore = operationLogStore;
    this.handlerMapping = handlerMapping;
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 1;
  }

  @Nonnull
  @Override
  public Mono<Void> filter(@Nonnull ServerWebExchange exchange, @Nonnull WebFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    HttpHeaders headers = request.getHeaders();
    final String traceId = headers.getFirst(TraceConstants.TRACE_ID_HEADER_NAME);
    final String spanId = headers.getFirst(TraceConstants.SPAN_ID_HEADER_NAME);

    TraceContext traceContext;
    if (StringUtils.isNotBlank(traceId) && StringUtils.isNotBlank(spanId)) {
      traceContext = new TraceContext(traceId, spanId);
    } else {
      traceContext = new TraceContext(TraceIdGenerator.Holder.get().generate());
    }
    TraceExchangeUtils.putTraceContext(exchange, traceContext);
    String logPrefix = traceContext.getLogPrefix();
    if (StringUtils.isNotBlank(traceId) && StringUtils.isBlank(spanId)) {
      log.warn(logPrefix + "traceId不为空, 但是spanId为空");
    }

    HttpMethod method = request.getMethod();
    String requestPath = request.getURI().getPath();
    log.info(logPrefix + method + " " + requestPath);

    AtomicBoolean success = new AtomicBoolean(true);
    AtomicReference<String> message = new AtomicReference<>("success");
    return handlerMapping.getHandler(exchange)
        .switchIfEmpty(chain.filter(exchange))
        .flatMap(handler -> {
          if (operatorHolder == null) {
            return Mono.just(Tuple.of(handler, null));
          }
          return operatorHolder.get()
              .map(operator -> Tuple.of(handler, operator))
              .switchIfEmpty(Mono.just(Tuple.of(handler, null)));
        })
        // 操作日志
        .flatMap(tuple -> {
          Object handler = tuple.getFirst();
          Object second = tuple.getSecond();
          if (operatorHolder != null
              && operationLogStore != null
              && handler instanceof HandlerMethod handlerMethod
              && second instanceof Operator operator) {
            Operation operation = handlerMethod.getMethodAnnotation(Operation.class);
            if (operation == null) {
              return chain.filter(exchange);
            }
            String name = operation.name();
            if (StringUtils.isBlank(name)) {
              name = operation.value();
            }
            if (StringUtils.isBlank(name)) {
              name = handlerMethod.getMethod().getName();
            }
            OperationLog operationLog = new OperationLog();
            operationLog.setTraceId(traceContext.getTraceId());
            operationLog.setSystem(system);
            operationLog.setPlatform(operator.getPlatform());
            operationLog.setTenantId(operator.getTenantId());
            operationLog.setUserId(operator.getUserId());
            operationLog.setName(name);
//          operationLog.setDetails();
            operationLog.setPath(requestPath);
            operationLog.setOriginalIp(ExchangeUtils.getRemoteAddress(exchange));
            String userAgent = ExchangeUtils.getUserAgent(exchange);
            if (userAgent != null) {
              operationLog.setUserAgent(userAgent);
            }
            operationLog.setOperationTime(traceContext.getCreateMillis());
            traceContext.setOperationLog(operationLog);
          }
          return chain.filter(exchange);
        })
        .doOnError(throwable -> {
          success.set(false);
          String msg = throwable.getMessage();
          if (StringUtils.isNotBlank(msg)) {
            message.set(msg);
          } else {
            String name = throwable.getClass().getName();
            message.set(name);
          }
        })
        .contextWrite(ctx -> ctx.put(TraceConstants.CTX_KEY, traceContext))
        // 输出trace日志
        .doFinally(t -> {
          ServerHttpResponse response = exchange.getResponse();
          HttpStatusCode statusCode = response.getStatusCode();
          long survivalMillis = traceContext.getSurvivalMillis();
          if (statusCode == null) {
            log.info(logPrefix + method + " " + requestPath +
                " | consuming: " + survivalMillis + "ms");
          } else {
            int status = statusCode.value();
            log.info(logPrefix + status +
                " " + method + " " + requestPath + " | consuming: " + survivalMillis + "ms");
          }
          OperationLog operationLog = traceContext.getOperationLog();
          if (operationLog != null) {
            operationLog.setConsuming((int) survivalMillis);
            operationLog.setSuccess(success.get());
            operationLog.setMessage(message.get());
            if (operationLogStore != null) {
              //noinspection CallingSubscribeInNonBlockingScope
              operationLogStore.save(operationLog).subscribe();
            }
          }
        });
  }
}
