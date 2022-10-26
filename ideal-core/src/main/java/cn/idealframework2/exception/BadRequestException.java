package cn.idealframework2.exception;

import javax.annotation.Nonnull;
import java.io.Serial;

/**
 * @author 宋志宗 on 2022/8/13
 */
public class BadRequestException extends VisibleException {
  @Serial
  private static final long serialVersionUID = -6281199227602676282L;

  public BadRequestException(@Nonnull String message) {
    super(400, 400, null, message);
  }
}
