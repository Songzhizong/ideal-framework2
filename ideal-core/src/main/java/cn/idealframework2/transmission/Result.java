package cn.idealframework2.transmission;

import cn.idealframework2.exception.VisibleException;
import cn.idealframework2.json.JsonUtils;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.Transient;
import java.io.Serial;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2021/8/24
 */
@SuppressWarnings("unused")
public class Result<T> extends BasicResult {
  @Serial
  private static final long serialVersionUID = -4328578094677050954L;

  @Nullable
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T data;

  public Result() {
    super();
  }

  @Nonnull
  public static <T> Result<T> create(boolean success, @Nullable String code,
                                     @Nullable String message, @Nullable T data) {
    Result<T> res = new Result<>();
    res.setSuccess(success);
    res.setCode(code);
    res.setMessage(message);
    res.setData(data);
    return res;
  }

  @Nonnull
  public static <T> Result<T> success() {
    Result<T> res = new Result<>();
    res.setSuccess(true);
    return res;
  }

  @Nonnull
  public static <T> Result<T> success(@Nullable T data) {
    Result<T> res = new Result<>();
    res.setSuccess(true);
    res.setData(data);
    return res;
  }

  @Nonnull
  public static <T> Result<T> success(@Nullable T data, @Nullable String message) {
    Result<T> res = new Result<>();
    res.setSuccess(true);
    res.setMessage(message);
    res.setData(data);
    return res;
  }

  @Nonnull
  public static <T> Result<T> failure(@Nullable String message) {
    Result<T> res = new Result<>();
    res.setSuccess(false);
    res.setMessage(message);
    return res;
  }

  @Nonnull
  public static <T> Result<T> failure(@Nullable String code, @Nullable String message) {
    Result<T> res = new Result<>();
    res.setSuccess(false);
    res.setCode(code);
    res.setMessage(message);
    return res;
  }

  @Nonnull
  public static <T> Result<T> exception(@Nonnull Throwable t) {
    if (t instanceof VisibleException exception) {
      return failure(exception.getCode(), exception.getMessage());
    }
    Result<T> res = new Result<>();
    res.setSuccess(false);
    res.setMessage(t.getMessage());
    return res;
  }

  @Nonnull
  public static <T> Result<T> exception(@Nullable String message) {
    Result<T> res = new Result<>();
    res.setSuccess(false);
    res.setMessage(message);
    return res;
  }

  /**
   * 如果断言响应结果不可能为空可以调用此方法获取响应数据, 如果为空则会抛出{@link NullPointerException}
   *
   * @return 响应数据
   * @author 宋志宗 on 2021/4/14
   */
  @Nonnull
  @Transient
  public T requiredData() {
    T data = getData();
    if (data == null) {
      throw new NullPointerException("data is null");
    }
    return data;
  }

  @Nullable
  public T getData() {
    return data;
  }

  public void setData(@Nullable T data) {
    this.data = data;
  }

  @Nonnull
  public <R> Result<R> convert(@Nullable Function<T, R> function) {
    Result<R> retRes = new Result<>();
    retRes.setTraceId(this.getTraceId());
    retRes.setSuccess(this.isSuccessful());
    retRes.setCode(this.getCode());
    retRes.setMessage(this.getMessage());
    if (this.getData() != null && function != null) {
      retRes.setData(function.apply(this.getData()));
    }
    return retRes;
  }

  public static void main(String[] args) {
    Result<Void> success = success();
    System.out.println(JsonUtils.toJsonString(success));
  }
}
