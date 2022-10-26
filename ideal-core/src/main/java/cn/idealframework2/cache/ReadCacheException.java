package cn.idealframework2.cache;

import cn.idealframework2.exception.VisibleException;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/29
 */
public class ReadCacheException extends VisibleException {

  public ReadCacheException(@Nonnull String message) {
    super(500, 500, null, message);
  }
}
