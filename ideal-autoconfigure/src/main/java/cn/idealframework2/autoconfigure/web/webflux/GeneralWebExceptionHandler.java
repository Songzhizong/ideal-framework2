package cn.idealframework2.autoconfigure.web.webflux;

import cn.idealframework2.exception.VisibleException;
import cn.idealframework2.json.JsonFormatException;
import cn.idealframework2.json.JsonParseException;
import cn.idealframework2.json.JsonUtils;
import cn.idealframework2.spring.ExchangeUtils;
import cn.idealframework2.trace.TraceContext;
import cn.idealframework2.trace.reactive.TraceExchangeUtils;
import cn.idealframework2.transmission.Result;
import cn.idealframework2.utils.ExceptionUtils;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2022/10/10
 */
@ConditionalOnClass({WebFluxConfigurer.class})
@ConditionalOnExpression("${ideal-web.enable-unified-exception-handler:true}")
public class GeneralWebExceptionHandler implements Ordered, ErrorWebExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GeneralWebExceptionHandler.class);
  private static final HttpHeaders HTTP_HEADERS = new HttpHeaders();

  static {
    HTTP_HEADERS.set("Content-Type", "application/json;charset=utf-8");
  }

  @Override
  public int getOrder() {
    return -100;
  }

  @Nonnull
  @Override
  public Mono<Void> handle(@Nonnull ServerWebExchange exchange, @Nonnull Throwable throwable) {
    //noinspection DuplicatedCode
    HttpStatusCode httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    Result<Object> res = null;
    String logPrefix = "";
    Optional<TraceContext> optional = TraceExchangeUtils.getTraceContext(exchange);
    if (optional.isPresent()) {
      TraceContext context = optional.get();
      logPrefix = context.getLogPrefix();
    }

    if (throwable instanceof VisibleException exception) {
      int status = exception.getHttpStatus();
      httpStatus = HttpStatus.valueOf(status);
      res = Result.exception(throwable);
    }

    // json序列化异常
    if (throwable instanceof JsonFormatException) {
      log.info("{}JsonFormatException: ", logPrefix, throwable);
      res = Result.exception(throwable);
    }

    // json解析异常
    if (throwable instanceof JsonParseException) {
      log.info("{}JsonParseException: ", logPrefix, throwable);
      res = Result.exception(throwable);
    }

    // 参数校验不通过异常处理
    if (throwable instanceof MethodArgumentNotValidException exception) {
      httpStatus = HttpStatus.BAD_REQUEST;
      String message = exception.getBindingResult().getFieldErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.joining(", "));
      log.info("{}MethodArgumentNotValidException {}", logPrefix, message);
      res = Result.failure(message);
    }

    // 参数校验不通过异常处理
    if (throwable instanceof BindException exception) {
      httpStatus = HttpStatus.BAD_REQUEST;
      String message = exception.getBindingResult().getFieldErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.joining(", "));
      log.info("{}BindException {}", logPrefix, message);
      res = Result.failure(message);
    }

    if (throwable instanceof HttpMessageNotReadableException exception) {
      httpStatus = HttpStatus.BAD_REQUEST;
      Throwable rootCause = exception.getRootCause();
      String originalMessage;
      if (rootCause instanceof InvalidFormatException) {
        originalMessage = ((InvalidFormatException) rootCause).getOriginalMessage();
      } else {
        Throwable cause = ExceptionUtils.getRootCause(exception);
        originalMessage = cause.getMessage();
      }
      if (originalMessage == null) {
        originalMessage = "HttpMessageNotReadableException";
      }
      log.info("{}HttpMessageNotReadableException: {}", logPrefix, originalMessage);
      res = Result.failure(originalMessage);
      String prefix = "Cannot coerce empty String";
      if (originalMessage.startsWith(prefix)) {
        res.setMessage("枚举类型值为空时请传null,而非空白字符串");
      }
    }

    if (throwable instanceof IllegalArgumentException exception) {
      httpStatus = HttpStatus.BAD_REQUEST;
      String message = exception.getLocalizedMessage();
      if (message == null) {
        message = "illegal argument";
      }
      log.info("{}", logPrefix, exception);
      res = Result.failure(message);
    }

    if (throwable instanceof IllegalStateException exception) {
      httpStatus = HttpStatus.BAD_REQUEST;
      Throwable cause = ExceptionUtils.getRootCause(exception);
      String message = cause.getMessage();
      if (message == null) {
        message = "illegal status";
      }
      log.info("{}", logPrefix, exception);
      res = Result.failure(message);
    }

    if (throwable instanceof MethodArgumentTypeMismatchException exception) {
      httpStatus = HttpStatus.BAD_REQUEST;
      Throwable cause = ExceptionUtils.getRootCause(exception);
      String message = cause.getMessage();
      if (message == null) {
        message = exception.getClass().getName();
      }
      log.info("{}MethodArgumentTypeMismatchException, {}", logPrefix, message);
      res = Result.failure(message);
    }

    if (throwable instanceof ServerWebInputException exception) {
      httpStatus = HttpStatus.BAD_REQUEST;
      String message = exception.getReason();
      Throwable cause = exception.getCause();
      if (cause != null) {
        message = ExceptionUtils.getRootCause(exception).getMessage();
      }
      log.info("{}ServerWebInputException {}", logPrefix, message);
      res = Result.failure(message);
    }

    if (throwable instanceof org.springframework.web.server.ResponseStatusException exception) {
      httpStatus = exception.getStatusCode();
      String message = exception.getMessage();
      res = Result.failure(message);
      String uri = exchange.getRequest().getURI().getPath();
      log.info("{}{} {}", logPrefix, message, uri, throwable);
    }

    //noinspection DuplicatedCode
    if (res == null) {
      String message = ExceptionUtils.getRootCause(throwable).getMessage();
      if (message == null) {
        message = throwable.getClass().getSimpleName();
      }
      log.warn("{}未针对处理的异常: ", logPrefix, throwable);
      res = Result.failure(message);
    }
    String jsonString = JsonUtils.toJsonString(res);
    byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
    return ExchangeUtils.writeResponse(exchange, httpStatus, HTTP_HEADERS, bytes);
  }
}
