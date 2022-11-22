package cn.idealframework2.transmission;

import cn.idealframework2.lang.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.Transient;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author 宋志宗 on 2021/8/25
 */
public class BasicResult implements Serializable {
  @Serial
  private static final long serialVersionUID = 1658084050565123764L;


  /**
   * 请求trace id
   *
   * @deprecated 转移到响应头中, 为了保持向前兼容, 暂时保留此字段
   */
  @Nullable
  @Deprecated
  private String traceId;

  /** 是否成功 */
  @Nullable
  private Boolean success = null;

  /**
   * 响应编码
   * <p>
   * 为了向前兼容暂时保留
   */
  @Deprecated
  private int code = 200;

  /** 用于描述具体业务信息的编码 */
  @Nullable
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String bizCode;

  @Nonnull
  private String message = "success";

  public BasicResult() {
  }

  @Transient
  @JsonIgnore
  public boolean isSuccessful() {
    return Boolean.TRUE.equals(success);
  }

  @Transient
  @JsonIgnore
  public boolean isFailed() {
    return !isSuccessful();
  }

  @Nullable
  @Deprecated
  public String getTraceId() {
    return traceId;
  }

  @Deprecated
  public void setTraceId(@Nullable String traceId) {
    this.traceId = traceId;
  }

  @Nullable
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  @Deprecated
  public int getCode() {
    return code;
  }

  @Deprecated
  public void setCode(int code) {
    this.code = code;
  }

  @Nullable
  public String getBizCode() {
    return bizCode;
  }

  public void setBizCode(@Nullable String bizCode) {
    this.bizCode = bizCode;
  }

  @Nonnull
  public String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    if (StringUtils.isBlank(message)) {
      message = "";
    }
    this.message = message;
  }
}
