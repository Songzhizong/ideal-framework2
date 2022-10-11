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

  @Nullable
  private Boolean success = null;

  @Nullable
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String code = null;

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

  public void setMessage(@Nullable String message) {
    if (StringUtils.isBlank(message)) {
      message = "";
    }
    this.message = message;
  }

  @Nullable
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  @Nullable
  public String getCode() {
    return code;
  }

  public void setCode(@Nullable String code) {
    this.code = code;
  }

  @Nonnull
  public String getMessage() {
    return message;
  }
}
