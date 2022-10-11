package cn.idealframework2.autoconfigure.web.webmvc;

import cn.idealframework2.transmission.BasicResult;
import io.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/10/10
 */
@SuppressWarnings("DuplicatedCode")
@Order(Ordered.LOWEST_PRECEDENCE - 2)
@ControllerAdvice
@ConditionalOnClass({WebMvcConfigurer.class, ReadTimeoutException.class})
@ConditionalOnExpression("${ideal-web.enable-unified-exception-handler:true}")
public class NettyExceptionHandlerAdvice {
  private static final Logger log = LoggerFactory.getLogger(NettyExceptionHandlerAdvice.class);
  private static final MultiValueMap<String, String> RESPONSE_HEADERS = new LinkedMultiValueMap<>();

  static {
    RESPONSE_HEADERS.set("Content-Type", "application/json;charset=utf-8");
  }

  /**
   * http请求超时
   */
  @ExceptionHandler(ReadTimeoutException.class)
  public ResponseEntity<Object> readTimeoutExceptionHandler(@Nonnull ReadTimeoutException exception) {
    log.warn("http请求超时: ", exception);
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setMessage("请求超时");
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
