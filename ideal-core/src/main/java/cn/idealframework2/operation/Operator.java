package cn.idealframework2.operation;

import javax.annotation.Nonnull;

/**
 * 操作人信息
 *
 * @author 宋志宗 on 2022/9/23
 */
@SuppressWarnings("unused")
public class Operator {

  /** 平台编码 */
  @Nonnull
  private String platform = "";

  /** 租户id */
  @Nonnull
  private String tenantId = "";

  /** 用户id */
  @Nonnull
  private String userId = "";

  @Nonnull
  public String getPlatform() {
    return platform;
  }

  public void setPlatform(@Nonnull String platform) {
    this.platform = platform;
  }

  @Nonnull
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(@Nonnull String tenantId) {
    this.tenantId = tenantId;
  }

  @Nonnull
  public String getUserId() {
    return userId;
  }

  public void setUserId(@Nonnull String userId) {
    this.userId = userId;
  }

  @Override
  public String toString() {
    return "Operator{" +
      "platform='" + platform + '\'' +
      ", tenantId='" + tenantId + '\'' +
      ", userId='" + userId + '\'' +
      '}';
  }
}
