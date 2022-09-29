package cn.idealframework2.lang;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/7/8
 */
public class Tuple<F, S> {
  private F first;
  private S second;

  public Tuple() {
  }

  public Tuple(F first, S second) {
    this.first = first;
    this.second = second;
  }

  @Nonnull
  public static <F, S> Tuple<F, S> of(F first, S second) {
    return new Tuple<>(first, second);
  }

  public F getFirst() {
    return first;
  }

  public void setFirst(F first) {
    this.first = first;
  }

  public S getSecond() {
    return second;
  }

  public void setSecond(S second) {
    this.second = second;
  }
}
