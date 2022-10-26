package cn.idealframework2.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serial;

/**
 * @author 宋志宗 on 2022/8/15
 */
public class ForbiddenException extends VisibleException {
  @Serial
  private static final long serialVersionUID = -6281199227602676282L;

  public ForbiddenException() {
    super(403, 403, null, "Forbidden");
  }

  public ForbiddenException(@Nonnull String message) {
    super(403, 403, null, message);
  }

  public ForbiddenException(@Nullable String bizCode, @Nonnull String message) {
    super(403, 403, bizCode, message);
  }

  public ForbiddenException(int code, @Nonnull String message) {
    super(403, code, null, message);
  }
}
