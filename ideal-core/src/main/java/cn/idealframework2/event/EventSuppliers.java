package cn.idealframework2.event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 宋志宗 on 2022/1/6
 */
public interface EventSuppliers {

  /**
   * 创建一个空的事件提供者
   *
   * @return 空的事件提供者
   * @author 宋志宗 on 2022/1/19
   */
  @Nonnull
  static EventSuppliers create() {
    return EventSuppliersImpl.create();
  }

  @Nonnull
  static EventSuppliers of(@Nullable EventSupplier supplier) {
    return EventSuppliersImpl.of(supplier);
  }

  @Nonnull
  static EventSuppliers of(@Nullable List<EventSupplier> suppliers) {
    return EventSuppliersImpl.of(suppliers);
  }

  @Nonnull
  static EventSuppliers of(@Nullable EventSupplier... suppliers) {
    return EventSuppliersImpl.of(suppliers);
  }

  @Nonnull
  static EventSuppliers of(@Nullable EventSuppliers suppliers) {
    return EventSuppliersImpl.of(suppliers);
  }

  @Nonnull
  ArrayList<EventSupplier> get();

  boolean isEmpty();

  @Nonnull
  EventSuppliers add(@Nullable EventSupplier supplier);

  @Nonnull
  EventSuppliers add(@Nullable EventSuppliers suppliers);

  @Nonnull
  EventSuppliers add(@Nullable List<EventSupplier> suppliers);
}
