package cn.idealframework2.autoconfigure.web.webflux;

import cn.idealframework2.json.JsonUtils;
import cn.idealframework2.lang.StringUtils;
import cn.idealframework2.spring.ExchangeUtils;
import cn.idealframework2.trace.TraceContext;
import cn.idealframework2.trace.reactive.TraceExchangeUtils;
import cn.idealframework2.transmission.Result;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author 宋志宗 on 2022/10/10
 */
@ConditionalOnClass({WebFluxConfigurer.class, DuplicateKeyException.class})
@ConditionalOnExpression("${ideal-web.enable-unified-exception-handler:true}")
public class SpringTxWebExceptionHandler implements Ordered, ErrorWebExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(SpringTxWebExceptionHandler.class);
  private static final HttpHeaders HTTP_HEADERS = new HttpHeaders();

  static {
    HTTP_HEADERS.set("Content-Type", "application/json;charset=utf-8");
  }

  @Override
  public int getOrder() {
    return -2;
  }

  @Nonnull
  @Override
  public Mono<Void> handle(@Nonnull ServerWebExchange exchange, @Nonnull Throwable throwable) {
    //noinspection DuplicatedCode
    HttpStatusCode httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    Result<Object> res = null;
    String logPrefix = "";
    String traceId = null;
    Optional<TraceContext> optional = TraceExchangeUtils.getTraceContext(exchange);
    if (optional.isPresent()) {
      TraceContext context = optional.get();
      logPrefix = context.getLogPrefix();
      traceId = context.getTraceId();
    }

    if (throwable instanceof DuplicateKeyException exception) {
      String message = getDuplicateMessage(exception);
      log.info("{}DuplicateKeyException {}", logPrefix, message);
      res = Result.failure(message);
    }

    //noinspection DuplicatedCode
    if (res == null) {
      return Mono.empty();
    }
    res.setTraceId(traceId);
    String jsonString = JsonUtils.toJsonString(res);
    byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
    return ExchangeUtils.writeResponse(exchange, httpStatus, HTTP_HEADERS, bytes);
  }

  @Nonnull
  private String getDuplicateMessage(@Nonnull DuplicateKeyException e) {
    Throwable cause = e.getCause();
    if (cause instanceof MongoWriteException mongoWriteException) {
      WriteError writeError = mongoWriteException.getError();

      String message = writeError.getMessage();
      if (StringUtils.isNotBlank(message)) {
        int index = message.lastIndexOf("key:");
        if (index < 0) {
          return message;
        }
        return "违反唯一性约束 " + message.substring(index + 4).replace("\"", "");
      }
    }
    String defaultMessage = e.getMessage();
    if (defaultMessage == null) {
      defaultMessage = "";
    }
    return "违反唯一性约束 " + defaultMessage;
  }
}
