package cn.idealframework2.starter.operation.webflux;

import cn.idealframework2.operation.reactive.OperationFilter;
import cn.idealframework2.operation.reactive.OperationLogStore;
import cn.idealframework2.operation.reactive.OperatorHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/9/22
 */
public class WebFluxOperationAutoConfigure {
  private static final Logger log = LoggerFactory.getLogger(WebFluxOperationAutoConfigure.class);

  @Value("${spring.application.name:}")
  private String applicationName;


  @Bean
  @Nullable
  public OperationFilter operationFilter(@Nullable OperatorHolder operatorHolder,
                                         @Nullable OperationLogStore operationLogStore,
                                         @Nonnull RequestMappingHandlerMapping requestMappingHandlerMapping) {
    if (operatorHolder == null) {
      log.error("无法记录操作日志, OperatorHolder 为空");
      return null;
    }
    if (operationLogStore == null) {
      log.error("无法记录操作日志, OperationLogStore 为空");
      return null;
    }
    return new OperationFilter(applicationName, operatorHolder, operationLogStore, requestMappingHandlerMapping);
  }
}
