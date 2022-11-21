package cn.idealframework2.autoconfigure.trace;

import cn.idealframework2.lang.StringUtils;
import cn.idealframework2.starter.model.trace.coroutine.CoroutineTraceModel;
import cn.idealframework2.trace.reactive.OperationLogStore;
import cn.idealframework2.trace.reactive.OperatorHolder;
import cn.idealframework2.trace.reactive.TraceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.WebFilter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author 宋志宗 on 2022/9/22
 */
@ConditionalOnClass({CoroutineTraceModel.class, WebFilter.class})
public class WebFluxTraceAutoConfigure {
  private static final Logger log = LoggerFactory.getLogger(WebFluxTraceAutoConfigure.class);

  @Value("${spring.application.name:}")
  private String applicationName;

  @Bean
  public TraceFilter traceFilter(@Nonnull TraceProperties properties,
                                 @Nullable @Autowired(required = false)
                                 OperatorHolder operatorHolder,
                                 @Nullable @Autowired(required = false)
                                 OperationLogStore operationLogStore,
                                 @Nonnull @Qualifier("requestMappingHandlerMapping")
                                 RequestMappingHandlerMapping handlerMapping) {
    if (operatorHolder == null) {
      log.warn("未配置OperatorHolder, 无法记录操作日志");
    }
    if (operationLogStore == null) {
      log.warn("未配置OperationLogStore, 无法记录操作日志");
    }
    String system = properties.getSystem();
    Set<String> excludePatterns = properties.getExcludePatterns();
    if (StringUtils.isBlank(system) && StringUtils.isNotBlank(applicationName)) {
      system = applicationName;
    }
    return new TraceFilter(system, excludePatterns, operatorHolder, operationLogStore, handlerMapping);
  }
}
