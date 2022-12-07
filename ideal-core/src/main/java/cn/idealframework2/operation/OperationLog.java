package cn.idealframework2.operation;

import cn.idealframework2.json.JsonUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 操作日志
 *
 * @author 宋志宗 on 2022/9/23
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class OperationLog {

  @Nullable
  private String traceId;

  /** 系统名称 */
  @Nonnull
  private String system = "";

  /** 平台 */
  @Nonnull
  private String platform = "";

  /** 租户ID */
  @Nonnull
  private String tenantId = "";

  /** 操作名称 */
  @Nonnull
  private String name = "";

  /** 操作详情 */
  @Nonnull
  private String details = "";

  /** 请求路径 */
  @Nonnull
  private String path = "";

  /** 用户ID */
  @Nonnull
  private String userId = "";

  /** 原始请求地址 */
  @Nonnull
  private String originalIp = "";

  /** 浏览器UA */
  @Nonnull
  private String userAgent = "";

  /** 是否成功 */
  private boolean success = true;

  /** 执行信息, 可用于记录错误信息 */
  @Nonnull
  private String message = "";

  /** 变更前 */
  @Nullable
  private String before;

  /** 变更后 */
  @Nullable
  private String after;

  /** 耗时, 单位毫秒 */
  private int consuming = -1;

  /** 操作时间 */
  private long operationTime = -1L;

  @Nullable
  public String getTraceId() {
    return traceId;
  }

  public void setTraceId(@Nullable String traceId) {
    this.traceId = traceId;
  }

  @Nonnull
  public String getSystem() {
    return system;
  }

  public void setSystem(@Nonnull String system) {
    this.system = system;
  }

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
  public String getName() {
    return name;
  }

  public void setName(@Nonnull String name) {
    this.name = name;
  }

  @Nonnull
  public String getDetails() {
    return details;
  }

  public void setDetails(@Nonnull String details) {
    this.details = details;
  }

  @Nonnull
  public String getPath() {
    return path;
  }

  public void setPath(@Nonnull String path) {
    this.path = path;
  }

  @Nonnull
  public String getUserId() {
    return userId;
  }

  public void setUserId(@Nonnull String userId) {
    this.userId = userId;
  }

  @Nonnull
  public String getOriginalIp() {
    return originalIp;
  }

  public void setOriginalIp(@Nonnull String originalIp) {
    this.originalIp = originalIp;
  }

  @Nonnull
  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(@Nonnull String userAgent) {
    this.userAgent = userAgent;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  @Nonnull
  public String getMessage() {
    return message;
  }

  public void setMessage(@Nonnull String message) {
    this.message = message;
  }

  @Nullable
  public String getBefore() {
    return before;
  }

  public void setBefore(@Nullable String before) {
    this.before = before;
  }

  @Nullable
  public String getAfter() {
    return after;
  }

  public void setAfter(@Nullable String after) {
    this.after = after;
  }

  public int getConsuming() {
    return consuming;
  }

  public void setConsuming(int consuming) {
    this.consuming = consuming;
  }

  public long getOperationTime() {
    return operationTime;
  }

  public void setOperationTime(long operationTime) {
    this.operationTime = operationTime;
  }

  @Override
  public String toString() {
    return JsonUtils.toJsonString(this);
  }
}
