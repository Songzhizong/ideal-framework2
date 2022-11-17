package cn.idealframework2.autoconfigure.trace;

import cn.idealframework2.lang.StringUtils;
import cn.idealframework2.starter.model.trace.coroutine.CoroutineTraceModel;
import cn.idealframework2.trace.reactive.OperationLogStore;
import cn.idealframework2.trace.reactive.OperatorHolder;
import cn.idealframework2.trace.reactive.TraceFilter;
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
    String system = properties.getSystem();
    Set<String> excludePatterns = properties.getExcludePatterns();
    if (StringUtils.isBlank(system) && StringUtils.isNotBlank(applicationName)) {
      system = applicationName;
    }
    return new TraceFilter(system, excludePatterns, operatorHolder, operationLogStore, handlerMapping);
  }
}
