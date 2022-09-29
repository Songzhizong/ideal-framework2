package cn.idealframework2.trace;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/23
 */
public class Operator {

  @Nonnull
  private String platform = "";

  @Nonnull
  private String tenantId = "";

  @Nonnull
  private String userId = "";

  @Nonnull
  public String getPlatform() {
    return platform;
  }

  public Operator setPlatform(@Nonnull String platform) {
    this.platform = platform;
    return this;
  }

  @Nonnull
  public String getTenantId() {
    return tenantId;
  }

  public Operator setTenantId(@Nonnull String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

  @Nonnull
  public String getUserId() {
    return userId;
  }

  public Operator setUserId(@Nonnull String userId) {
    this.userId = userId;
    return this;
  }
}
