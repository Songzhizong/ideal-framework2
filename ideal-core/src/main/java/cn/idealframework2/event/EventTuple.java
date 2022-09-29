package cn.idealframework2.event;

import cn.idealframework2.lang.Lists;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 宋志宗 on 2021/4/27
 */
public class EventTuple<V> implements EventSuppliers {
  /** 方法返回结果 */
  @Nonnull
  private final V value;

  /** 方法返回事件列表 */
  @Nonnull
  private ArrayList<EventSupplier> events;

  protected EventTuple(@Nonnull V value,
                       @Nonnull ArrayList<EventSupplier> suppliers) {
    this.value = value;
    this.events = suppliers;
  }

  @Nonnull
  public static <V> EventTuple<V> of(@Nonnull V value) {
    return new EventTuple<>(value, new ArrayList<>());
  }

  @Nonnull
  public static <V> EventTuple<V> of(@Nonnull V value,
                                     @Nonnull List<EventSupplier> suppliers) {
    return new EventTuple<>(value, new ArrayList<>(suppliers));
  }

  @Nonnull
  public static <V> EventTuple<V> of(@Nonnull V value,
                                     @Nonnull EventSupplier supplier) {
    return new EventTuple<>(value, Lists.arrayList(supplier));
  }

  @Nonnull
  public static <V> EventTuple<V> of(@Nonnull V value,
                                     @Nonnull EventSupplier... suppliers) {
    return new EventTuple<>(value, new ArrayList<>(Arrays.asList(suppliers)));
  }

  @Nonnull
  public V getValue() {
    return value;
  }

  /**
   * @deprecated {@link EventTuple#getValue()}
   */
  @Nonnull
  @Deprecated
  public V getResult() {
    return value;
  }

  @Nonnull
  @Override
  public ArrayList<EventSupplier> get() {
    return this.getEvents();
  }

  @Override
  public boolean isEmpty() {
    return Lists.isEmpty(this.getEvents());
  }

  @Nonnull
  @Override
  public EventSuppliers add(@Nullable EventSupplier supplier) {
    if (supplier == null) {
      return this;
    }
    this.getEvents().add(supplier);
    return this;
  }

  @Nonnull
  @Override
  public EventSuppliers add(@Nullable EventSuppliers suppliers) {
    if (suppliers == null || suppliers.isEmpty()) {
      return this;
    }
    if (this.isEmpty()) {
      this.setEvents(suppliers.get());
      return this;
    }
    this.getEvents().addAll(suppliers.get());
    return this;
  }

  @Nonnull
  public EventTuple<V> add(@Nullable List<EventSupplier> events) {
    if (Lists.isEmpty(events)) {
      return this;
    }
    if (this.isEmpty()) {
      if (events instanceof ArrayList) {
        this.setEvents((ArrayList<EventSupplier>) events);
      } else {
        this.setEvents(new ArrayList<>(events));
      }
      return this;
    }
    this.getEvents().addAll(events);
    return this;
  }

  @Nonnull
  public EventSuppliers toEvents() {
    return this;
  }

  @Nonnull
  public ArrayList<EventSupplier> getEvents() {
    return events;
  }

  private void setEvents(@Nonnull ArrayList<EventSupplier> events) {
    this.events = events;
  }
}
