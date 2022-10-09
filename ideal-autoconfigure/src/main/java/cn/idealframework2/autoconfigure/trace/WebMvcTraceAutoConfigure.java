package cn.idealframework2.autoconfigure.trace;

import cn.idealframework2.lang.StringUtils;
import cn.idealframework2.starter.model.trace.TraceModel;
import cn.idealframework2.trace.block.OperationLogStore;
import cn.idealframework2.trace.block.OperatorHolder;
import cn.idealframework2.trace.block.TraceFilter;
import cn.idealframework2.trace.block.TraceInterceptor;
import jakarta.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/10/9
 */
@ConditionalOnClass({TraceModel.class, Filter.class, HandlerInterceptor.class})
public class WebMvcTraceAutoConfigure {
  private static final Logger log = LoggerFactory.getLogger(WebMvcTraceAutoConfigure.class);

  @Value("${spring.application.name:}")
  private String applicationName;

  @Bean
  public TraceFilter traceFilter() {
    return new TraceFilter();
  }

  @Bean
  public TraceInterceptor traceInterceptor(@Nonnull TraceProperties properties,
                                           @Nullable @Autowired(required = false)
                                           OperatorHolder operatorHolder,
                                           @Nullable @Autowired(required = false)
                                           OperationLogStore operationLogStore) {
    if (operatorHolder == null) {
      log.warn("未配置OperatorHolder, 无法记录操作日志");
    }
    if (operationLogStore == null) {
      log.warn("未配置OperationLogStore, 无法记录操作日志");
    }
    String system = properties.getSystem();
    if (StringUtils.isBlank(system) && StringUtils.isNotBlank(applicationName)) {
      system = applicationName;
    }
    return new TraceInterceptor(system, operatorHolder, operationLogStore);
  }
}
