package cn.idealframework2.exception;

import javax.annotation.Nonnull;
import java.io.Serial;

/**
 * @author 宋志宗 on 2022/8/16
 */
public class InternalServerException extends VisibleException {
  @Serial
  private static final long serialVersionUID = -6281199227602676282L;

  public InternalServerException(@Nonnull String message) {
    super(500, 500, null, message);
  }
}
