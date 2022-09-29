package cn.idealframework2.trace;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 操作日志
 *
 * @author 宋志宗 on 2022/9/23
 */
public class OperationLog {

  @Nonnull
  private String traceId = "";

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

  @Nonnull
  public String getTraceId() {
    return traceId;
  }

  public OperationLog setTraceId(@Nonnull String traceId) {
    this.traceId = traceId;
    return this;
  }

  @Nonnull
  public String getSystem() {
    return system;
  }

  public OperationLog setSystem(@Nonnull String system) {
    this.system = system;
    return this;
  }

  @Nonnull
  public String getPlatform() {
    return platform;
  }

  public OperationLog setPlatform(@Nonnull String platform) {
    this.platform = platform;
    return this;
  }

  @Nonnull
  public String getTenantId() {
    return tenantId;
  }

  public OperationLog setTenantId(@Nonnull String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

  @Nonnull
  public String getName() {
    return name;
  }

  public OperationLog setName(@Nonnull String name) {
    this.name = name;
    return this;
  }

  @Nonnull
  public String getDetails() {
    return details;
  }

  public OperationLog setDetails(@Nonnull String details) {
    this.details = details;
    return this;
  }

  @Nonnull
  public String getPath() {
    return path;
  }

  public OperationLog setPath(@Nonnull String path) {
    this.path = path;
    return this;
  }

  @Nonnull
  public String getUserId() {
    return userId;
  }

  public OperationLog setUserId(@Nonnull String userId) {
    this.userId = userId;
    return this;
  }

  @Nonnull
  public String getOriginalIp() {
    return originalIp;
  }

  public OperationLog setOriginalIp(@Nonnull String originalIp) {
    this.originalIp = originalIp;
    return this;
  }

  @Nonnull
  public String getUserAgent() {
    return userAgent;
  }

  public OperationLog setUserAgent(@Nonnull String userAgent) {
    this.userAgent = userAgent;
    return this;
  }

  public boolean isSuccess() {
    return success;
  }

  public OperationLog setSuccess(boolean success) {
    this.success = success;
    return this;
  }

  @Nonnull
  public String getMessage() {
    return message;
  }

  public OperationLog setMessage(@Nonnull String message) {
    this.message = message;
    return this;
  }

  @Nullable
  public String getBefore() {
    return before;
  }

  public OperationLog setBefore(@Nullable String before) {
    this.before = before;
    return this;
  }

  @Nullable
  public String getAfter() {
    return after;
  }

  public OperationLog setAfter(@Nullable String after) {
    this.after = after;
    return this;
  }

  public int getConsuming() {
    return consuming;
  }

  public OperationLog setConsuming(int consuming) {
    this.consuming = consuming;
    return this;
  }

  public long getOperationTime() {
    return operationTime;
  }

  public OperationLog setOperationTime(long operationTime) {
    this.operationTime = operationTime;
    return this;
  }
}
