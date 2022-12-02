package cn.idealframework2.transmission;

import cn.idealframework2.lang.Lists;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2021/8/25
 */
@SuppressWarnings("unused")
public class PageResult<E> extends ListResult<E> {
  @Serial
  private static final long serialVersionUID = -7093873606301836395L;

  /** 页码 */
  private int page;

  /** 页大小 */
  private int size;

  /** 总数量 */
  private long total;

  /** 总页数 */
  private int totalPages;

  public PageResult() {
    super();
  }

  @Nonnull
  public static <E> PageResult<E> empty(@Nonnull Paging paging) {
    PageResult<E> res = new PageResult<>();
    res.setPage(paging.getPageNumber());
    res.setSize(paging.getPageSize());
    res.setTotal(0);
    res.setTotalPages(1);
    res.setData(new ArrayList<>());
    res.setSuccess(true);
    return res;
  }

  @Nonnull
  public static <E> PageResult<E> singleton(@Nonnull Paging paging, @Nonnull E element) {
    PageResult<E> res = new PageResult<>();
    res.setPage(paging.getPageNumber());
    res.setSize(paging.getPageSize());
    res.setTotal(1);
    res.setTotalPages(1);
    res.setData(Lists.arrayList(element));
    res.setSuccess(true);
    return res;
  }

  @Nonnull
  public <U> PageResult<U> map(Function<? super E, ? extends U> converter) {
    PageResult<U> res = new PageResult<>();
    res.setSuccess(true);
    res.setCode(this.getCode());
    res.setTitle(this.getTitle());
    res.setMessage(this.getMessage());
    res.setPage(this.getPage());
    res.setSize(this.getSize());
    res.setTotal(this.getTotal());
    res.setTotalPages(this.getTotalPages());
    List<E> data = this.getData();
    if (data.size() > 0) {
      res.setData(data.stream().map(converter).collect(Collectors.toList()));
    }
    return res;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }
}
