package cn.idealframework2.transmission;

import cn.idealframework2.lang.Lists;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2022/4/22
 */
public class ListResult<E> extends BasicResult {
  @Serial
  private static final long serialVersionUID = -7093873606301836395L;

  /** 返回的数据 */
  @Nonnull
  private List<E> data = Collections.emptyList();

  public ListResult() {
    super();
  }


  @Nonnull
  public static <E> ListResult<E> empty() {
    ListResult<E> res = new ListResult<>();
    res.setData(new ArrayList<>());
    res.setSuccess(true);
    return res;
  }

  @Nonnull
  public static <E> ListResult<E> singleton(@Nonnull E element) {
    ListResult<E> res = new ListResult<>();
    res.setData(Lists.arrayList(element));
    res.setSuccess(true);
    return res;
  }

  @Nonnull
  @SuppressWarnings("DuplicatedCode")
  public static <E> ListResult<E> of(@Nonnull List<E> data) {
    ListResult<E> res = new ListResult<>();
    res.setData(data);
    res.setSuccess(true);
    return res;
  }

  @Nonnull
  public <U> ListResult<U> map(Function<? super E, ? extends U> converter) {
    ListResult<U> res = new ListResult<>();
    res.setSuccess(true);
    res.setCode(this.getCode());
    res.setMessage(this.getMessage());
    List<E> data = this.getData();
    if (data.size() > 0) {
      res.setData(data.stream().map(converter).collect(Collectors.toList()));
    }
    return res;
  }

  @Nonnull
  public List<E> getData() {
    return data;
  }

  public void setData(@Nonnull List<E> data) {
    this.data = data;
  }
}
