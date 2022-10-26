package cn.idealframework2.autoconfigure.web.webmvc;

import cn.idealframework2.exception.VisibleException;
import cn.idealframework2.json.JsonFormatException;
import cn.idealframework2.json.JsonParseException;
import cn.idealframework2.transmission.BasicResult;
import cn.idealframework2.transmission.Result;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2022/10/10
 */
@SuppressWarnings({"DuplicatedCode"})
@Order(-100)
@ControllerAdvice
@ConditionalOnClass({WebMvcConfigurer.class})
@ConditionalOnExpression("${ideal-web.enable-unified-exception-handler:true}")
public class GeneralExceptionHandlerAdvice {
  private static final Logger log = LoggerFactory.getLogger(GeneralExceptionHandlerAdvice.class);
  private static final MultiValueMap<String, String> RESPONSE_HEADERS = new LinkedMultiValueMap<>();

  static {
    RESPONSE_HEADERS.set("Content-Type", "application/json;charset=utf-8");
  }

  @Nonnull
  @ExceptionHandler(VisibleException.class)
  public ResponseEntity<Object> visibleExceptionHandler(
    @Nonnull VisibleException ex) {
    String message = ex.getMessage();
    int httpStatus = ex.getHttpStatus();
    log.info(message);
    Result<Object> body = Result.exception(ex);
    body.setBizCode(ex.getBizCode());
    return new ResponseEntity<>(body, RESPONSE_HEADERS, HttpStatusCode.valueOf(httpStatus));
  }

  @Nonnull
  @ExceptionHandler(JsonFormatException.class)
  public ResponseEntity<Object> jsonFormatExceptionHandler(@Nonnull JsonFormatException ex) {
    log.info("JsonFormatException: ", ex);
    Result<Object> body = Result.exception(ex);
    return new ResponseEntity<>(body, RESPONSE_HEADERS, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Nonnull
  @ExceptionHandler(JsonParseException.class)
  public ResponseEntity<Object> jsonParseExceptionHandler(@Nonnull JsonParseException ex) {
    log.info("JsonParseException: ", ex);
    Result<Object> body = Result.exception(ex);
    return new ResponseEntity<>(body, RESPONSE_HEADERS, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * 参数校验不通过异常处理
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> methodArgumentNotValidExceptionHandler(
    @Nonnull MethodArgumentNotValidException exception) {
    String message = exception.getBindingResult().getFieldErrors().stream()
      .map(DefaultMessageSourceResolvable::getDefaultMessage)
      .collect(Collectors.joining(", "));
    log.info("@Valid fail : " + message);
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setMessage(message);
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.BAD_REQUEST);
  }


  /**
   * 参数校验不通过异常处理
   */
  @ExceptionHandler(BindException.class)
  public ResponseEntity<Object> bindExceptionHandler(@Nonnull BindException exception) {
    String message = exception.getBindingResult().getFieldErrors().stream()
      .map(DefaultMessageSourceResolvable::getDefaultMessage)
      .collect(Collectors.joining(", "));
    log.info("@Valid fail : " + message);
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setMessage(message);
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> httpMessageNotReadableExceptionHandler(
    @Nonnull HttpMessageNotReadableException exception) {
    Throwable rootCause = exception.getRootCause();
    String originalMessage;
    if (rootCause instanceof InvalidFormatException) {
      originalMessage = ((InvalidFormatException) rootCause).getOriginalMessage();
    } else if (rootCause != null && rootCause.getMessage() != null) {
      originalMessage = rootCause.getMessage();
    } else {
      originalMessage = exception.getMessage();
    }
    if (originalMessage == null) {
      originalMessage = "HttpMessageNotReadableException";
    }
    log.info(originalMessage);
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setMessage(originalMessage);
    String prefix = "Cannot coerce empty String";
    if (originalMessage.startsWith(prefix)) {
      res.setMessage("枚举类型值为空时请传null,而非空白字符串");
    }
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.BAD_REQUEST);
  }

  @Nonnull
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> illegalArgumentExceptionExceptionHandler(
    @Nonnull IllegalArgumentException ex) {
    String message = ex.getLocalizedMessage();
    if (message == null) {
      message = "illegal argument";
    }
    log.info("IllegalArgument ", ex);
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setMessage(message);
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.BAD_REQUEST);
  }

  @Nonnull
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<Object> illegalStatusExceptionExceptionHandler(
    @Nonnull IllegalStateException ex) {
    String message = ex.getMessage();
    if (message == null) {
      message = "illegal status";
    }
    log.info("", ex);
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setMessage(message);
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.BAD_REQUEST);
  }

  @Nonnull
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Object> httpRequestMethodNotSupportedExceptionHandler(
    @Nonnull HttpRequestMethodNotSupportedException ex) {
    String message = ex.getMessage();
    if (message == null) {
      message = ex.getClass().getName();
    }
    log.info(message);
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setMessage(message);
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @Nonnull
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Object> methodArgumentTypeMismatchExceptionHandler(
    @Nonnull MethodArgumentTypeMismatchException ex) {
    String message = ex.getMessage();
    if (message == null) {
      message = ex.getClass().getName();
    }
    log.info(message);
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setMessage(message);
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.BAD_REQUEST);
  }


  /**
   * 服务器无法处理请求附带的媒体格式
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<Object> httpMediaTypeNotSupportedExceptionHandler(
    @Nonnull HttpMediaTypeNotSupportedException exception) {
    String message = exception.getMessage();
    log.info(message);
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setMessage(message);
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  /**
   * 处理其他异常信息
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> globalExceptionHandler(Exception exception) {
    log.warn("Exception -> ", exception);
    String message;
    if (exception.getMessage() != null) {
      message = exception.getMessage();
    } else {
      message = exception.getClass().getSimpleName();
    }
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setMessage(message);
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
