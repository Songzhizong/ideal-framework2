package cn.idealframework2.event;

import cn.idealframework2.lang.ArrayUtils;
import cn.idealframework2.lang.Lists;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 宋志宗 on 2021/11/22
 */
public final class EventSuppliersImpl implements EventSuppliers {
  @Nonnull
  private ArrayList<EventSupplier> suppliers;

  private EventSuppliersImpl(@Nonnull ArrayList<EventSupplier> suppliers) {
    this.suppliers = suppliers;
  }

  @Nonnull
  public static EventSuppliersImpl create() {
    return new EventSuppliersImpl(new ArrayList<>());
  }

  @Nonnull
  public static EventSuppliersImpl of(@Nullable EventSupplier supplier) {
    if (supplier == null) {
      return create();
    }
    return new EventSuppliersImpl(Lists.arrayList(supplier));
  }

  @Nonnull
  public static EventSuppliersImpl of(@Nullable List<EventSupplier> suppliers) {
    if (Lists.isEmpty(suppliers)) {
      return create();
    }
    return new EventSuppliersImpl(new ArrayList<>(suppliers));
  }

  @Nonnull
  public static EventSuppliersImpl of(@Nullable EventSupplier... suppliers) {
    if (ArrayUtils.isEmpty(suppliers)) {
      return create();
    }
    return new EventSuppliersImpl(Lists.arrayList(suppliers));
  }

  @Nonnull
  public static EventSuppliersImpl of(@Nullable EventSuppliers suppliers) {
    if (suppliers == null || suppliers.isEmpty()) {
      return create();
    }
    return new EventSuppliersImpl(suppliers.get());
  }

  @Nonnull
  @Override
  public ArrayList<EventSupplier> get() {
    return getSuppliers();
  }

  @Override
  @Transient
  @JsonIgnore
  public boolean isEmpty() {
    return Lists.isEmpty(getSuppliers());
  }

  @Nonnull
  @Override
  public EventSuppliersImpl add(@Nullable EventSupplier supplier) {
    if (supplier == null) {
      return this;
    }
    this.getSuppliers().add(supplier);
    return this;
  }

  @Nonnull
  @Override
  public EventSuppliersImpl add(@Nullable EventSuppliers suppliers) {
    if (suppliers == null || suppliers.isEmpty()) {
      return this;
    }
    ArrayList<EventSupplier> otherEvents = suppliers.get();
    if (this.isEmpty()) {
      this.setSuppliers(otherEvents);
      return this;
    }
    this.getSuppliers().addAll(otherEvents);
    return this;
  }

  @Nonnull
  @Override
  public EventSuppliersImpl add(@Nullable List<EventSupplier> suppliers) {
    if (Lists.isEmpty(suppliers)) {
      return this;
    }
    this.getSuppliers().addAll(suppliers);
    return this;
  }

  @Nonnull
  public ArrayList<EventSupplier> getSuppliers() {
    return suppliers;
  }

  public void setSuppliers(@Nonnull ArrayList<EventSupplier> suppliers) {
    this.suppliers = suppliers;
  }
}
