package cn.idealframework2.autoconfigure.web.webflux;

import cn.idealframework2.json.JsonUtils;
import cn.idealframework2.lang.StringUtils;
import cn.idealframework2.spring.ExchangeUtils;
import cn.idealframework2.transmission.Result;
import cn.idealframework2.utils.ExceptionUtils;
import com.mongodb.MongoCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;

/**
 * @author 宋志宗 on 2022/10/10
 */
@ConditionalOnClass({WebFluxConfigurer.class, MongoCommandException.class, UncategorizedMongoDbException.class})
@ConditionalOnExpression("${ideal-web.enable-unified-exception-handler:true}")
public class SpringMongoWebExceptionHandler implements Ordered, ErrorWebExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(SpringMongoWebExceptionHandler.class);
  private static final HttpHeaders HTTP_HEADERS = new HttpHeaders();

  static {
    HTTP_HEADERS.set("Content-Type", "application/json;charset=utf-8");
  }

  @Override
  public int getOrder() {
    return -101;
  }

  @Nonnull
  @Override
  public Mono<Void> handle(@Nonnull ServerWebExchange exchange, @Nonnull Throwable throwable) {
    //noinspection DuplicatedCode
    HttpStatusCode httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    Result<Object> res = null;

    if (throwable instanceof UncategorizedMongoDbException exception) {
      httpStatus = HttpStatus.BAD_REQUEST;
      Throwable rootCause = ExceptionUtils.getRootCause(exception);
      String message = rootCause.getMessage();
      if (StringUtils.isBlank(message)) {
        message = rootCause.getClass().getName();
      }
      log.info("UncategorizedMongoDbException {}", message);
      res = Result.failure(message);
    }

    if (throwable instanceof MongoCommandException exception) {
      httpStatus = HttpStatus.BAD_REQUEST;
      Throwable rootCause = ExceptionUtils.getRootCause(exception);
      String message = rootCause.getMessage();
      if (StringUtils.isBlank(message)) {
        message = rootCause.getClass().getName();
      }
      log.info("MongoCommandException {}", message);
      res = Result.failure(message);
    }

    //noinspection DuplicatedCode
    if (res == null) {
      return Mono.error(throwable);
    }
    String jsonString = JsonUtils.toJsonString(res);
    byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
    return ExchangeUtils.writeResponse(exchange, httpStatus, HTTP_HEADERS, bytes);
  }

}
