package cn.idealframework2.trace.reactive;

import cn.idealframework2.lang.StringUtils;
import cn.idealframework2.lang.Tuple;
import cn.idealframework2.spring.ExchangeUtils;
import cn.idealframework2.spring.PathMatchers;
import cn.idealframework2.trace.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 宋志宗 on 2022/9/22
 */
public class TraceFilter implements Ordered, WebFilter {
  private static final Logger log = LoggerFactory.getLogger(TraceFilter.class);
  @Nonnull
  private final String system;
  @Nonnull
  private final Set<String> excludePaths = new HashSet<>();
  @Nonnull
  private final Set<String> excludePatterns = new HashSet<>();
  @Nullable
  private final OperatorHolder operatorHolder;
  @Nullable
  private final OperationLogStore operationLogStore;
  @Nonnull
  private final RequestMappingHandlerMapping handlerMapping;

  public TraceFilter(@Nonnull String system,
                     @Nonnull Set<String> excludePatterns,
                     @Nullable OperatorHolder operatorHolder,
                     @Nullable OperationLogStore operationLogStore,
                     @Nonnull RequestMappingHandlerMapping handlerMapping) {
    this.system = system;
    this.operatorHolder = operatorHolder;
    this.operationLogStore = operationLogStore;
    this.handlerMapping = handlerMapping;
    for (String excludePattern : excludePatterns) {
      if (PathMatchers.isPattern(excludePattern)) {
        this.excludePatterns.add(excludePattern);
      } else {
        excludePaths.add(excludePattern);
      }
    }
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 1;
  }

  @SuppressWarnings("DuplicatedCode")
  @Nonnull
  @Override
  public Mono<Void> filter(@Nonnull ServerWebExchange exchange, @Nonnull WebFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    String requestPath = request.getURI().getPath();
    if (excludePaths.contains(requestPath)) {
      return chain.filter(exchange);
    }
    for (String excludePattern : excludePatterns) {
      if (PathMatchers.match(excludePattern, requestPath)) {
        return chain.filter(exchange);
      }
    }
    HttpHeaders headers = request.getHeaders();
    final String traceId = headers.getFirst(TraceConstants.TRACE_ID_HEADER_NAME);
    final String spanId = headers.getFirst(TraceConstants.SPAN_ID_HEADER_NAME);
    // 初始化TraceContext
    TraceContext traceContext;
    if (StringUtils.isNotBlank(traceId) && StringUtils.isNotBlank(spanId)) {
      traceContext = new TraceContext(traceId, spanId);
    } else {
      traceContext = new TraceContext(TraceIdGenerator.Holder.get().generate());
    }
    MDC.put(TraceConstants.TRACE_ID_HEADER_NAME, traceContext.getTraceId());
    MDC.put(TraceConstants.SPAN_ID_HEADER_NAME, traceContext.getSpanId());
    TraceExchangeUtils.putTraceContext(exchange, traceContext);
    if (StringUtils.isNotBlank(traceId) && StringUtils.isBlank(spanId)) {
      log.warn("traceId不为空, 但是spanId为空");
    }
    // 为响应头附加traceId
    exchange.getResponse().getHeaders()
      .set(TraceConstants.TRACE_ID_HEADER_NAME, traceContext.getTraceId());

    String method = request.getMethod().name();
    log.info(method + " " + requestPath);

    AtomicBoolean success = new AtomicBoolean(true);
    AtomicReference<String> message = new AtomicReference<>("success");
    return handlerMapping.getHandler(exchange)
      .switchIfEmpty(chain.filter(exchange))
      // 获取HandlerMethod和操作人信息
      .flatMap(handler -> {
        if (operatorHolder == null) {
          return Mono.just(Tuple.<Object, Operator>of(handler, null));
        }
        return operatorHolder.get()
          .map(operator -> Tuple.of(handler, operator))
          .switchIfEmpty(Mono.just(Tuple.of(handler, null)));
      })
      // 初始化操作日志
      .map(tuple -> initOperationLog(exchange, traceContext, tuple))
      // 执行过滤
      .flatMap(b -> chain.filter(exchange))
      // 异常处理
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
      .contextWrite(ctx ->
        ctx.put(TraceConstants.CTX_KEY, traceContext)
          .put(TraceConstants.TRACE_ID_HEADER_NAME, traceContext.getTraceId())
          .put(TraceConstants.SPAN_ID_HEADER_NAME, traceContext.getSpanId())
      )
      .doFinally(t -> doFinally(exchange, traceContext, success.get(), message.get()));
  }

  private boolean initOperationLog(@Nonnull ServerWebExchange exchange,
                                   @Nonnull TraceContext traceContext,
                                   @Nonnull Tuple<Object, Operator> tuple) {
    Object handler = tuple.getFirst();
    Operator operator = tuple.getSecond();
    if (operatorHolder != null
      && operationLogStore != null
      && operator != null
      && handler instanceof HandlerMethod handlerMethod) {
      Operation operation = handlerMethod.getMethodAnnotation(Operation.class);
      if (operation == null) {
        return true;
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
      operationLog.setTraceId(traceContext.getTraceId());
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
      operationLog.setOperationTime(traceContext.getCreateMillis());
      traceContext.setOperationLog(operationLog);
    }
    return true;
  }

  private void doFinally(@Nonnull ServerWebExchange exchange,
                         @Nonnull TraceContext traceContext,
                         boolean success, @Nonnull String message) {
    MDC.put(TraceConstants.TRACE_ID_HEADER_NAME, traceContext.getTraceId());
    MDC.put(TraceConstants.SPAN_ID_HEADER_NAME, traceContext.getSpanId());
    long survivalMillis = traceContext.getSurvivalMillis();
    if (log.isInfoEnabled()) {
      ServerHttpRequest request = exchange.getRequest();
      ServerHttpResponse response = exchange.getResponse();
      String method = request.getMethod().name();
      String requestPath = request.getURI().getPath();
      HttpStatusCode statusCode = response.getStatusCode();
      // 输出trace耗时日志
      if (statusCode == null) {
        log.info("{} {} | consuming: {}ms", method, requestPath, survivalMillis);
      } else {
        int status = statusCode.value();
        if (statusCode instanceof HttpStatus httpStatus) {
          String reasonPhrase = httpStatus.getReasonPhrase();
          log.info("{} {} {} {} | consuming: {}ms", status, reasonPhrase, method, requestPath, survivalMillis);
        } else {
          log.info("{} {} {} | consuming: {}ms", status, method, requestPath, survivalMillis);
        }
      }
    }
    // 保存操作日志
    OperationLog operationLog = traceContext.getOperationLog();
    if (operationLog != null) {
      operationLog.setConsuming((int) survivalMillis);
      operationLog.setSuccess(success);
      operationLog.setMessage(message);
      if (operationLogStore != null) {
        operationLogStore.save(operationLog)
          .contextWrite(ctx ->
            ctx.put(TraceConstants.CTX_KEY, traceContext)
              .put(TraceConstants.TRACE_ID_HEADER_NAME, traceContext.getTraceId())
              .put(TraceConstants.SPAN_ID_HEADER_NAME, traceContext.getSpanId())
          ).subscribe();
      }
    }
    MDC.clear();
  }
}
