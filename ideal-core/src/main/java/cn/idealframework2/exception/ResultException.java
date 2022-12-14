package cn.idealframework2.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serial;

/**
 * @author 宋志宗 on 2022/8/16
 */
public class ResultException extends VisibleException {
  @Serial
  private static final long serialVersionUID = -6281199227602676282L;

  public ResultException(int httpStatus, int code, @Nullable String title, @Nonnull String message) {
    super(httpStatus, code, title, message);
  }
}
