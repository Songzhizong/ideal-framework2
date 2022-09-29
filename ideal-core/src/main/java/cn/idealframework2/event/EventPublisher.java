package cn.idealframework2.event;

import cn.idealframework2.lang.Lists;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * @author 宋志宗 on 2022/8/13
 */
public interface EventPublisher {

  /**
   * 批量发布
   *
   * @param suppliers EventMessageSupplier Collection
   * @author 宋志宗 on 2021/10/19
   */
  void publish(@Nonnull Collection<EventSupplier> suppliers);

  /**
   * 发布事件
   *
   * @param suppliers 事件提供者
   */
  default void publish(@Nonnull EventSuppliers suppliers) {
    List<EventSupplier> eventSuppliers = suppliers.get();
    publish(eventSuppliers);
  }

  /**
   * 发布事件
   *
   * @param supplier 事件提供者
   */
  default void publish(@Nonnull EventSupplier supplier) {
    publish(Lists.of(supplier));
  }
}
