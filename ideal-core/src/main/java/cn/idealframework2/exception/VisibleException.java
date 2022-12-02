package cn.idealframework2.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serial;

/**
 * 可作为响应内容的可见性受检异常
 *
 * @author 宋志宗 on 2020/12/20
 */
public class VisibleException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -6281199227602676282L;

  private final int httpStatus;

  private final int code;

  @Nullable
  private final String title;

  @Nonnull
  private final String message;

  @Nullable
  private Object data;

  public VisibleException(int httpStatus, int code, @Nullable String title, @Nonnull String message) {
    super(message);
    this.httpStatus = httpStatus;
    this.code = code;
    this.title = title;
    this.message = message;
  }

  public int getHttpStatus() {
    return httpStatus;
  }

  public int getCode() {
    return code;
  }

  @Nullable
  public String getTitle() {
    return title;
  }

  @Override
  @Nonnull
  public String getMessage() {
    return message;
  }

  @Nullable
  public Object getData() {
    return data;
  }

  public VisibleException setData(@Nullable Object data) {
    this.data = data;
    return this;
  }
}
