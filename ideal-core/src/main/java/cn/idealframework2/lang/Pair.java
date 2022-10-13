package cn.idealframework2.lang;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author 宋志宗 on 2021/11/2
 */
public class Pair<L, R> implements Map.Entry<L, R> {
  @Nonnull
  private L left;
  @Nonnull
  private R right;

  public Pair() {
  }

  public Pair(@Nonnull L left, @Nonnull R right) {
    this.left = left;
    this.right = right;
  }

  @Nonnull
  public static <L, R> Pair<L, R> of(@Nonnull L l, @Nonnull R r) {
    return new Pair<>(l, r);
  }

  @Nonnull
  @Override
  public L getKey() {
    return left;
  }

  @Nonnull
  @Override
  public R getValue() {
    return right;
  }

  @Nonnull
  @Override
  public R setValue(R value) {
    this.setRight(value);
    return right;
  }

  @Nonnull
  public L getLeft() {
    return left;
  }

  public Pair<L, R> setLeft(@Nonnull L left) {
    this.left = left;
    return this;
  }

  @Nonnull
  public R getRight() {
    return right;
  }

  public Pair<L, R> setRight(@Nonnull R right) {
    this.right = right;
    return this;
  }
}
