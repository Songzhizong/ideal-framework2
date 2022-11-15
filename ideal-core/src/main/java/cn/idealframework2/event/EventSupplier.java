package cn.idealframework2.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;
import java.beans.Transient;

/**
 * @author 宋志宗 on 2021/10/19
 */
public interface EventSupplier {

  /**
   * 获取事件对象
   *
   * @return 事件对象
   */
  @Nonnull
  @Transient
  @JsonIgnore
  Event getEvent();
}
