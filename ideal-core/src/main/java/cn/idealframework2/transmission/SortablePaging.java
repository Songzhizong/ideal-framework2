package cn.idealframework2.transmission;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author 宋志宗 on 2021/4/20
 */
@SuppressWarnings("unused")
public class SortablePaging extends Paging {
  @Nullable
  private List<Sort> pageSorts;

  public void clearSorts() {
    this.pageSorts = null;
  }

  public boolean containsSort(@Nonnull String property) {
    if (pageSorts == null) {
      return false;
    }
    if (pageSorts.isEmpty()) {
      return false;
    }
    for (Sort pageSort : pageSorts) {
      String sortProperty = pageSort.getProperty();
      if (property.equals(sortProperty)) {
        return true;
      }
    }
    return false;
  }

  @Nonnull
  public SortablePaging removeSort(@Nonnull String property) {
    if (pageSorts == null) {
      return this;
    }
    if (pageSorts.isEmpty()) {
      return this;
    }
    List<Sort> sorts = new ArrayList<>();
    for (Sort sort : pageSorts) {
      String sortProperty = sort.getProperty();
      if (!property.equals(sortProperty)) {
        sorts.add(sort);
      }
    }
    this.pageSorts = sorts;
    return this;
  }

  @Nonnull
  public SortablePaging removeSort(String... properties) {
    if (pageSorts == null) {
      return this;
    }
    if (pageSorts.isEmpty()) {
      return this;
    }
    if (properties == null) {
      return this;
    }
    if (properties.length == 0) {
      return this;
    }
    Set<String> propertySet = new HashSet<>(Arrays.asList(properties));
    List<Sort> sorts = new ArrayList<>();
    for (Sort sort : pageSorts) {
      String sortProperty = sort.getProperty();
      if (!propertySet.contains(sortProperty)) {
        sorts.add(sort);
      }
    }
    this.pageSorts = sorts;
    return this;
  }

  @Nonnull
  public SortablePaging sort(@Nonnull String property, @Nonnull Sort.Direction direction) {
    Sort sort = new Sort(property, direction);
    if (this.pageSorts == null) {
      this.pageSorts = new ArrayList<>();
    }
    this.pageSorts.add(sort);
    return this;
  }

  @Nonnull
  @Override
  public SortablePaging ascBy(@Nonnull String property) {
    return sort(property, Sort.Direction.ASC);
  }

  @Nonnull
  @Override
  public SortablePaging descBy(@Nonnull String property) {
    return sort(property, Sort.Direction.DESC);
  }

  @Nullable
  public List<Sort> getPageSorts() {
    return pageSorts;
  }

  public void setPageSorts(@Nullable List<Sort> pageSorts) {
    this.pageSorts = pageSorts;
  }
}
