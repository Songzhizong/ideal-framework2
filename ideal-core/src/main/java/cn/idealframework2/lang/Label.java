package cn.idealframework2.lang;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author 宋志宗 on 2022/4/23
 */
public class Label {
  /** 名称 */
  @Nonnull
  private String name = "";

  /** 值 */
  @Nonnull
  private String value = "";

  public Label() {
  }

  public Label(@Nonnull String name, @Nonnull String value) {
    this.name = name;
    this.value = value;
  }

  @Nonnull
  public static Label of(@Nonnull String name, @Nonnull String value) {
    return new Label(name, value);
  }

  @Nonnull
  public String getName() {
    return name;
  }

  public void setName(@Nonnull String name) {
    this.name = name;
  }

  @Nonnull
  public String getValue() {
    return value;
  }

  public void setValue(@Nonnull String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Label label = (Label) o;
    return name.equals(label.name) && value.equals(label.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, value);
  }

  @Override
  public String toString() {
    return "Label{" +
      "name='" + name + '\'' +
      ", value='" + value + '\'' +
      '}';
  }
}
