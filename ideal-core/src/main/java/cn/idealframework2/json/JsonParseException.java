package cn.idealframework2.json;

import java.io.Serial;

/**
 * @author 宋志宗 on 2020/10/23
 */
public class JsonParseException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -2952767589185246834L;

  public JsonParseException(Throwable cause) {
    super(cause);
  }
}
