package cn.idealframework2.spring;

import cn.idealframework2.transmission.PageResult;
import cn.idealframework2.transmission.Paging;
import cn.idealframework2.transmission.Sort;
import cn.idealframework2.transmission.SortablePaging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2021/4/20
 */
public final class SpringPageConverter {
  private SpringPageConverter() {
  }

  @Nonnull
  public static Pageable pageable(@Nonnull SortablePaging paging) {
    int page = paging.getPageNumber();
    int size = paging.getPageSize();
    List<Sort> pageSorts = paging.getPageSorts();

    List<org.springframework.data.domain.Sort.Order> orderList = null;
    if (pageSorts != null && pageSorts.size() > 0) {
      orderList = new ArrayList<>();
      for (Sort sort : pageSorts) {
        if (sort == null) {
          continue;
        }
        Sort.Direction direction = sort.getDirection();
        if (direction == null) {
          continue;
        }
        String property = sort.getProperty();
        if (property == null) {
          continue;
        }
        if (direction.isAscending()) {
          orderList.add(org.springframework.data.domain.Sort.Order.asc(property));
        } else {
          orderList.add(org.springframework.data.domain.Sort.Order.desc(property));
        }
      }
    }
    // 我们的页码从1开始, spring的页码从0开始, 转换一下
    if (orderList != null) {
      return PageRequest.of(page - 1, size, org.springframework.data.domain.Sort.by(orderList));
    } else {
      return PageRequest.of(page - 1, size);
    }
  }

  @Nonnull
  public static Pageable pageable(@Nonnull Paging paging) {
    if (paging instanceof SortablePaging sortablePaging) {
      return pageable(sortablePaging);
    }
    int page = paging.getPageNumber();
    int size = paging.getPageSize();
    return PageRequest.of(page - 1, size);
  }

  @Nonnull
  @SuppressWarnings("DuplicatedCode")
  public static <E> PageResult<E> pageResult(@Nonnull Page<E> page) {
    PageResult<E> res = new PageResult<>();
    res.setPage(page.getNumber() + 1);
    res.setSize(page.getSize());
    res.setTotal(page.getTotalElements());
    res.setTotalPages(page.getTotalPages());
    res.setData(page.getContent());
    res.setSuccess(true);
    return res;
  }

  @Nonnull
  public static <E, R> PageResult<R> pageResult(@Nonnull Page<E> page,
                                                @Nonnull Function<E, R> converter) {
    Page<R> map = page.map(converter);
    return pageResult(map);
  }
}
