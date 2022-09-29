package cn.idealframework2.transmission;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2021/4/20
 */
public class Sort {
  /** 排序字段名 */
  @Nullable
  private String property;

  /** 排序方式 */
  @Nullable
  private Direction direction;

  public Sort() {
  }

  public Sort(@Nullable String property, @Nullable Direction direction) {
    this.property = property;
    this.direction = direction;
  }

  public enum Direction {
    /** 升序 */
    ASC,
    /** 降序 */
    DESC;

    public boolean isAscending() {
      return this.equals(ASC);
    }

    public boolean isDescending() {
      return this.equals(DESC);
    }
  }

  @Nullable
  public String getProperty() {
    return property;
  }

  public void setProperty(@Nullable String property) {
    this.property = property;
  }

  @Nullable
  public Direction getDirection() {
    return direction;
  }

  public void setDirection(@Nullable Direction direction) {
    this.direction = direction;
  }
}
