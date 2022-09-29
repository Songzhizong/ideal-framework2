package cn.idealframework2.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serial;

/**
 * @author 宋志宗 on 2022/8/13
 */
public class ResourceNotFoundException extends VisibleException {
  @Serial
  private static final long serialVersionUID = -6281199227602676282L;

  public ResourceNotFoundException(@Nonnull String message) {
    super(404, null, message);
  }

  public static void throwIfNull(@Nullable Object o, @Nonnull String message) {
    if (o != null) {
      return;
    }
    throw new ResourceNotFoundException(message);
  }
}
