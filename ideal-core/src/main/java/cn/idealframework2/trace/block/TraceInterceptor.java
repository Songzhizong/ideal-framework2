package cn.idealframework2.trace.block;

import cn.idealframework2.trace.TraceConstants;
import cn.idealframework2.trace.TraceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/10/9
 */
public class TraceInterceptor implements HandlerInterceptor {
  private static final Logger log = LoggerFactory.getLogger(TraceInterceptor.class);

  @Override
  public void afterCompletion(@Nonnull HttpServletRequest request,
                              @Nonnull HttpServletResponse response,
                              @Nonnull Object handler, @Nullable Exception ex) throws Exception {
    Object attribute = request.getAttribute(TraceConstants.CTX_KEY);
    if (!(attribute instanceof TraceContext context)) {
      HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
      return;
    }
    try {
      long survivalMillis = context.getSurvivalMillis();
      if (log.isInfoEnabled()) {
        int status = response.getStatus();
        String method = request.getMethod();
        String requestPath = request.getRequestURI();
        HttpStatusCode statusCode = HttpStatusCode.valueOf(status);
        if (statusCode instanceof HttpStatus httpStatus) {
          String reasonPhrase = httpStatus.getReasonPhrase();
          log.info("{} {} {} {} | consuming: {}ms", status, reasonPhrase, method, requestPath, survivalMillis);
        } else {
          log.info("{} {} {} | consuming {}ms", status, method, requestPath, survivalMillis);
        }
      }
    } finally {
      request.removeAttribute(TraceConstants.CTX_KEY);
    }
  }
}
