package cn.idealframework2.json;

import java.io.Serial;

/**
 * @author 宋志宗 on 2020/10/23
 */
public class JsonFormatException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -6377988611315237492L;

  public JsonFormatException(Throwable cause) {
    super(cause);
  }
}
