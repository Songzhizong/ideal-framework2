package cn.idealframework2.lang;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/7/8
 */
@SuppressWarnings("unused")
public class Triple<F, S, T> {
  private F first;
  private S second;
  private T third;

  public Triple() {
  }

  public Triple(F first, S second, T third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  @Nonnull
  public static <F, S, T> Triple<F, S, T> of(F first, S second, T third) {
    return new Triple<>(first, second, third);
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

  public T getThird() {
    return third;
  }

  public void setThird(T third) {
    this.third = third;
  }
}
