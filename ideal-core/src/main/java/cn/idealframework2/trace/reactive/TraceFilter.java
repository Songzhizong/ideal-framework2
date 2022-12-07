package cn.idealframework2.trace.reactive;

import cn.idealframework2.lang.StringUtils;
import cn.idealframework2.spring.PathMatchers;
import cn.idealframework2.trace.TraceConstants;
import cn.idealframework2.trace.TraceContext;
import cn.idealframework2.trace.TraceIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 宋志宗 on 2022/9/22
 */
public class TraceFilter implements Ordered, WebFilter {
  private static final Logger log = LoggerFactory.getLogger(TraceFilter.class);
  @Nonnull
  private final Set<String> excludePaths = new HashSet<>();
  @Nonnull
  private final Set<String> excludePatterns = new HashSet<>();

  public TraceFilter(@Nonnull Set<String> excludePatterns) {
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
    boolean newTraceContext = false;
    // 初始化TraceContext
    TraceContext traceContext;
    if (StringUtils.isNotBlank(traceId) && StringUtils.isNotBlank(spanId)) {
      traceContext = new TraceContext(traceId, spanId);
    } else {
      newTraceContext = true;
      traceContext = new TraceContext(TraceIdGenerator.Holder.get().generate());
    }
    MDC.put(TraceConstants.TRACE_ID_HEADER_NAME, traceContext.getTraceId());
    MDC.put(TraceConstants.SPAN_ID_HEADER_NAME, traceContext.getSpanId());
    TraceExchangeUtils.putTraceContext(exchange, traceContext);
    if (StringUtils.isNotBlank(traceId) && StringUtils.isBlank(spanId)) {
      log.warn("traceId不为空, 但是spanId为空");
    }
    // 为响应头附加traceId
    if (newTraceContext) {
      HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
      responseHeaders.set(TraceConstants.TRACE_ID_HEADER_NAME, traceContext.getTraceId());
    }

    String method = request.getMethod().name();
    log.info(method + " " + requestPath);
    return chain.filter(exchange)
      .contextWrite(ctx ->
        ctx.put(TraceConstants.CTX_KEY, traceContext)
          .put(TraceConstants.TRACE_ID_HEADER_NAME, traceContext.getTraceId())
          .put(TraceConstants.SPAN_ID_HEADER_NAME, traceContext.getSpanId())
      )
      .doFinally(t -> doFinally(exchange, traceContext));
  }

  private void doFinally(@Nonnull ServerWebExchange exchange,
                         @Nonnull TraceContext traceContext) {
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
    MDC.clear();
  }
}
