package cn.idealframework2.trace.block;

import cn.idealframework2.lang.StringUtils;
import cn.idealframework2.spring.PathMatchers;
import cn.idealframework2.trace.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author 宋志宗 on 2022/10/9
 */
public class TraceInterceptor implements HandlerInterceptor {
  private static final Logger log = LoggerFactory.getLogger(TraceInterceptor.class);
  private static final String UNKNOWN = "UNKNOWN";

  @Nonnull
  private final String system;
  @Nonnull
  private final Set<String> excludePatterns;
  @Nullable
  private final OperatorHolder operatorHolder;
  @Nullable
  private final OperationLogStore operationLogStore;

  public TraceInterceptor(@Nonnull String system,
                          @Nonnull Set<String> excludePatterns,
                          @Nullable OperatorHolder operatorHolder,
                          @Nullable OperationLogStore operationLogStore) {
    this.system = system;
    this.excludePatterns = excludePatterns;
    this.operatorHolder = operatorHolder;
    this.operationLogStore = operationLogStore;
  }


  @SuppressWarnings("DuplicatedCode")
  @Override
  public boolean preHandle(@Nonnull HttpServletRequest request,
                           @Nonnull HttpServletResponse response,
                           @Nonnull Object handler) {
    String requestPath = request.getRequestURI();
    for (String excludePattern : excludePatterns) {
      if (PathMatchers.match(excludePattern, requestPath)) {
        return true;
      }
    }
    if (operatorHolder == null) {
      return true;
    }
    if (!(handler instanceof HandlerMethod handlerMethod)) {
      return true;
    }
    Operation operation = handlerMethod.getMethodAnnotation(Operation.class);
    if (operation == null) {
      return true;
    }
    Operator operator = operatorHolder.get();
    if (operator == null) {
      return true;
    }
    Object attribute = request.getAttribute(TraceConstants.CTX_KEY);
    if (!(attribute instanceof TraceContext context)) {
      return true;
    }
    String name = operation.name();
    if (StringUtils.isBlank(name)) {
      name = operation.value();
    }
    if (StringUtils.isBlank(name)) {
      name = handlerMethod.getMethod().getName();
    }
    String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
    OperationLog operationLog = new OperationLog();
    operationLog.setTraceId(context.getTraceId());
    operationLog.setSystem(system);
    operationLog.setPlatform(operator.getPlatform());
    operationLog.setTenantId(operator.getTenantId());
    operationLog.setName(name);
    operationLog.setPath(requestPath);
    operationLog.setUserId(operator.getUserId());
    operationLog.setOriginalIp(getOriginalIp(request));
    operationLog.setUserAgent(userAgent);
    operationLog.setOperationTime(context.getCreateMillis());
    context.setOperationLog(operationLog);
    return true;
  }

  @Nonnull
  @SuppressWarnings("DuplicatedCode")
  private String getOriginalIp(@Nonnull HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (StringUtils.isNotBlank(ip)) {
      int index = ip.indexOf(',');
      if (index > -1) {
        String substring = ip.substring(0, index);
        if (!StringUtils.equalsIgnoreCase(substring, UNKNOWN)) {
          return substring;
        }
      } else {
        return ip;
      }
    }
    String remoteAddr = request.getRemoteAddr();
    if (StringUtils.isNotBlank(remoteAddr)) {
      return remoteAddr;
    }
    return "";
  }

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
      String method = request.getMethod();
      String requestPath = request.getRequestURI();
      long survivalMillis = context.getSurvivalMillis();
      int status = response.getStatus();
      log.info("{} {} {} | consuming {}ms", status, method, requestPath, survivalMillis);
      OperationLog operationLog = context.getOperationLog();
      if (operationLog != null && operationLogStore != null) {
        if (ex != null) {
          operationLog.setSuccess(false);
          String msg = ex.getMessage();
          if (StringUtils.isNotBlank(msg)) {
            operationLog.setMessage(msg);
          } else {
            String name = ex.getClass().getName();
            operationLog.setMessage(name);
          }
        }
        operationLog.setConsuming(Math.toIntExact(survivalMillis));
        operationLogStore.save(operationLog);
      }
    } finally {
      request.removeAttribute(TraceConstants.CTX_KEY);
    }
  }
}
