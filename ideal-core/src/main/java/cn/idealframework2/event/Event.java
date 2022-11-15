package cn.idealframework2.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;
import java.beans.Transient;

/**
 * 基本事件接口
 *
 * @author 宋志宗 on 2021/10/22
 */
public interface Event extends EventSupplier {
  /**
   * 获取事件对象
   *
   * @return 事件对象
   */
  @Nonnull
  @Override
  @Transient
  @JsonIgnore
  default Event getEvent() {
    return this;
  }
}
