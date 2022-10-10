package cn.idealframework2.autoconfigure.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author 宋志宗 on 2022/10/10
 */
@ConfigurationProperties("ideal-web")
public class WebProperties {
  /** 是否启动统一异常处理 */
  private boolean enableUnifiedExceptionHandler = true;

  /** 消息转换器配置 */
  @NestedConfigurationProperty
  private MessageConverterProperties messageConverter = new MessageConverterProperties();

  public boolean isEnableUnifiedExceptionHandler() {
    return enableUnifiedExceptionHandler;
  }

  public WebProperties setEnableUnifiedExceptionHandler(boolean enableUnifiedExceptionHandler) {
    this.enableUnifiedExceptionHandler = enableUnifiedExceptionHandler;
    return this;
  }

  public MessageConverterProperties getMessageConverter() {
    return messageConverter;
  }

  public WebProperties setMessageConverter(MessageConverterProperties messageConverter) {
    this.messageConverter = messageConverter;
    return this;
  }
}
