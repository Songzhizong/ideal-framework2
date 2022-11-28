package cn.idealframework2.autoconfigure.web.webmvc;

import cn.idealframework2.starter.model.webmvc.WebmvcModel;
import cn.idealframework2.transmission.BasicResult;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;
import java.sql.SQLException;

/**
 * @author 宋志宗 on 2022/10/10
 */
@SuppressWarnings("DuplicatedCode")
@Order(-101)
@ControllerAdvice
@ConditionalOnClass({
  WebMvcConfigurer.class,
  DataIntegrityViolationException.class,
  ConstraintViolationException.class,
  ObjectOptimisticLockingFailureException.class,
  WebmvcModel.class
})
@ConditionalOnExpression("${ideal-web.enable-unified-exception-handler:true}")
public class SpringJdbcExceptionHandlerAdvice {
  private static final Logger log = LoggerFactory.getLogger(SpringJdbcExceptionHandlerAdvice.class);
  private static final MultiValueMap<String, String> RESPONSE_HEADERS = new LinkedMultiValueMap<>();

  static {
    RESPONSE_HEADERS.set("Content-Type", "application/json;charset=utf-8");
  }


  /**
   * jdbc异常
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Object> dataIntegrityViolationExceptionHandler(
    @Nonnull DataIntegrityViolationException exception) {
    String message = exception.getMessage();
    Throwable cause = exception.getCause();
    if (cause instanceof ConstraintViolationException ex) {
      String constraintName = ex.getConstraintName();
      SQLException sqlException = ex.getSQLException();
      int errorCode = sqlException.getErrorCode();
      // 数据重复,唯一索引冲突
      if (errorCode == 1062) {
        message = constraintName + "已存在";
      }
    }
    if (message == null) {
      message = exception.getClass().getSimpleName();
    }
    log.info(message);
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setMessage(message);
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.BAD_REQUEST);
  }

  /**
   * 乐观锁冲突
   */
  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  public ResponseEntity<Object> objectOptimisticLockingFailureExceptionHandler(
    @Nonnull ObjectOptimisticLockingFailureException e) {
    log.info("乐观锁冲突: " + e.getMessage());
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setMessage("Object optimistic locking failure.");
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.CONFLICT);
  }

  @Nonnull
  @ExceptionHandler(InvalidDataAccessApiUsageException.class)
  public ResponseEntity<Object> invalidDataAccessApiUsageExceptionHandler(
    @Nonnull InvalidDataAccessApiUsageException ex) {
    String message = ex.getMessage();
    Throwable cause = ex.getCause();
    if (cause != null) {
      String message1 = cause.getMessage();
      if (message1 != null) {
        message = message1;
      }
    }
    if (message == null) {
      message = "InvalidDataAccessApiUsageException";
    }
    log.info("InvalidDataAccessApiUsageException: ", ex);
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setMessage(message);
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.BAD_REQUEST);
  }
}
