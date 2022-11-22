package cn.idealframework2.transmission;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serial;
import java.io.Serializable;

/**
 * 区间
 *
 * @author 宋志宗 on 2020/5/21
 */
public class Range<T> implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  /** 起始 */
  @Nullable
  private T start;

  /** 结束 */
  @Nullable
  private T end;

  public Range() {
  }

  public Range(@Nullable T start, @Nullable T end) {
    this.start = start;
    this.end = end;
  }

  @Nonnull
  public static <T> Range<T> of(@Nullable T start, @Nullable T end) {
    return new Range<>(start, end);
  }

  @Nullable
  public T getStart() {
    return start;
  }

  public void setStart(@Nullable T start) {
    this.start = start;
  }

  @Nullable
  public T getEnd() {
    return end;
  }

  public void setEnd(@Nullable T end) {
    this.end = end;
  }

  @Override
  public String toString() {
    return "Range{" +
      "start=" + start +
      ", end=" + end +
      '}';
  }
}
