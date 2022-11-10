package cn.idealframework2.transmission;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;
import java.beans.Transient;

/**
 * @author 宋志宗 on 2021/4/29
 */
@SuppressWarnings("unused")
public class Paging {
  private int pageNumber = 1;
  private int pageSize = 10;

  public Paging() {
  }

  public Paging(int pageNumber, int pageSize) {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public int getPageSize() {
    return pageSize;
  }

  @Nonnull
  public static Paging of(int pageNumber, int pageSize) {
    return new Paging(pageNumber, pageSize);
  }

  @Nonnull
  public SortablePaging ascBy(@Nonnull String property) {
    SortablePaging paging = new SortablePaging();
    paging.setPageNumber(this.getPageNumber());
    paging.setPageSize(this.getPageSize());
    return paging.ascBy(property);
  }

  @Transient
  @JsonIgnore
  public long getOffset() {
    return ((long) pageNumber - 1L) * (long) pageSize;
  }

  @Nonnull
  public SortablePaging descBy(@Nonnull String property) {
    SortablePaging paging = new SortablePaging();
    paging.setPageNumber(this.getPageNumber());
    paging.setPageSize(this.getPageSize());
    return paging.descBy(property);
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
}
