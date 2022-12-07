package cn.idealframework2.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/12/7
 */
public class Singleton<V> {
  @Nullable
  private V value;

  public Singleton(@Nullable V value) {
    this.value = value;
  }

  @Nonnull
  public static <V> Singleton<V> of(@Nullable V value) {
    return new Singleton<>(value);
  }

  @Nullable
  public V getValue() {
    return value;
  }

  public void setValue(@Nullable V value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "Singleton{" +
      "value=" + value +
      '}';
  }
}
