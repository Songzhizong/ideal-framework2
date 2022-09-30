package cn.idealframework2.utils;

import cn.idealframework2.exception.BadRequestException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 宋志宗 on 2022/9/30
 */
public class ExceptionUtilsTest {

  @Test
  public void getRootCause() {
    BadRequestException exception = new BadRequestException("");
    RuntimeException runtimeException = new RuntimeException(exception);
    IllegalStateException illegalStateException = new IllegalStateException(runtimeException);
    Throwable rootCause = ExceptionUtils.getRootCause(illegalStateException);
    Assert.assertSame(exception, rootCause);
  }
}
