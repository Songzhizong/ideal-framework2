package cn.idealframework2.operation.block;

import cn.idealframework2.lang.StringUtils;
import cn.idealframework2.operation.Operation;
import cn.idealframework2.operation.OperationLog;
import cn.idealframework2.operation.Operator;
import cn.idealframework2.trace.block.TraceContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/12/7
 */
public class OperationInterceptor implements HandlerInterceptor {
  private static final String UNKNOWN = "UNKNOWN";

  @Nonnull
  private final String system;
  @Nullable
  private final OperatorHolder operatorHolder;
  @Nullable
  private final OperationLogStore operationLogStore;

  public OperationInterceptor(@Nonnull String system,
                              @Nullable OperatorHolder operatorHolder,
                              @Nullable OperationLogStore operationLogStore) {
    this.system = system;
    this.operatorHolder = operatorHolder;
    this.operationLogStore = operationLogStore;
  }

  @Override
  public boolean preHandle(@Nonnull HttpServletRequest request,
                           @Nonnull HttpServletResponse response,
                           @Nonnull Object handler) {
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
    String name = operation.name();
    if (StringUtils.isBlank(name)) {
      name = operation.value();
    }
    if (StringUtils.isBlank(name)) {
      name = handlerMethod.getMethod().getName();
    }
    String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
    OperationLog operationLog = new OperationLog();
    TraceContextHolder.current()
      .ifPresent(context -> operationLog.setTraceId(context.getTraceId()));
    operationLog.setSystem(system);
    operationLog.setPlatform(operator.getPlatform());
    operationLog.setTenantId(operator.getTenantId());
    operationLog.setName(name);
    operationLog.setPath(request.getRequestURI());
    operationLog.setUserId(operator.getUserId());
    operationLog.setOriginalIp(getOriginalIp(request));
    operationLog.setUserAgent(userAgent);
    operationLog.setOperationTime(System.currentTimeMillis());
    Operations.set(operationLog);
    return true;
  }

  @Override
  public void afterCompletion(@Nonnull HttpServletRequest request,
                              @Nonnull HttpServletResponse response,
                              @Nonnull Object handler, Exception ex) {
    OperationLog operationLog = Operations.get();
    if (operationLog == null) {
      return;
    }
    if (operationLogStore == null) {
      Operations.remove();
      return;
    }
    try {
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
      long operationTime = operationLog.getOperationTime();
      long survivalMillis = System.currentTimeMillis() - operationTime;
      operationLog.setConsuming(Math.toIntExact(survivalMillis));
      operationLogStore.save(operationLog);
    } finally {
      Operations.remove();
    }
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
}
