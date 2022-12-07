package cn.idealframework2.trace.block;

import cn.idealframework2.lang.StringUtils;
import cn.idealframework2.trace.TraceConstants;
import cn.idealframework2.trace.TraceContext;
import cn.idealframework2.trace.TraceIdGenerator;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author 宋志宗 on 2022/10/9
 */
public class TraceFilter implements Ordered, Filter {
  private static final Logger log = LoggerFactory.getLogger(TraceFilter.class);

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 1;
  }

  @Override
  public void doFilter(@Nonnull ServletRequest request,
                       @Nonnull ServletResponse response,
                       @Nonnull FilterChain chain) throws IOException, ServletException {
    if (!(request instanceof HttpServletRequest httpServletRequest)) {
      chain.doFilter(request, response);
      return;
    }
    String requestPath = httpServletRequest.getRequestURI();
    String traceId = httpServletRequest.getHeader(TraceConstants.TRACE_ID_HEADER_NAME);
    String spanId = httpServletRequest.getHeader(TraceConstants.SPAN_ID_HEADER_NAME);
    // 初始化TraceContext
    TraceContext traceContext;
    boolean newTrace = false;
    if (StringUtils.isNotBlank(traceId) && StringUtils.isNotBlank(spanId)) {
      traceContext = new TraceContext(traceId, spanId);
    } else {
      newTrace = true;
      traceContext = new TraceContext(TraceIdGenerator.Holder.get().generate());
    }
    if (StringUtils.isNotBlank(traceId) && StringUtils.isBlank(spanId)) {
      log.warn("traceId不为空, 但是spanId为空");
    }
    TraceContextHolder.set(traceContext);
    httpServletRequest.setAttribute(TraceConstants.CTX_KEY, traceContext);
    MDC.put(TraceConstants.TRACE_ID_HEADER_NAME, traceContext.getTraceId());
    MDC.put(TraceConstants.SPAN_ID_HEADER_NAME, traceContext.getSpanId());
    String method = httpServletRequest.getMethod();
    log.info("{} {}", method, requestPath);
    if (response instanceof HttpServletResponse httpServletResponse) {
      if (newTrace) {
        httpServletResponse.setHeader(TraceConstants.TRACE_ID_HEADER_NAME, traceContext.getTraceId());
      }
    }
    try {
      chain.doFilter(request, response);
    } finally {
      TraceContextHolder.release();
    }

  }
}
