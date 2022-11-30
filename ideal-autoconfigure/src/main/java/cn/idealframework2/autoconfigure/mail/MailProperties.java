package cn.idealframework2.autoconfigure.mail;

import io.vertx.ext.mail.StartTLSOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/8/13
 */
@ConfigurationProperties("ideal-mail")
public class MailProperties {

  /** SMTP服务端地址 */
  @Nullable
  private String host;

  /** SMTP服务端口号 */
  @Nullable
  private Integer port;

  /** 发送邮件的邮箱地址 */
  @Nullable
  private String username;

  /** 发送账户的密码 */
  @Nullable
  private String password;

  /** 连接池最大连接数 */
  private int maxPoolSize = 32;

  /** 是否开启连接池 */
  private boolean keepAlive = true;

  /** 是否启用ssl */
  private boolean ssl = false;

  @Nonnull
  private StartTLSOptions starttls = StartTLSOptions.OPTIONAL;

  @Nullable
  public String getHost() {
    return host;
  }

  public void setHost(@Nullable String host) {
    this.host = host;
  }

  @Nullable
  public Integer getPort() {
    return port;
  }

  public void setPort(@Nullable Integer port) {
    this.port = port;
  }

  @Nullable
  public String getUsername() {
    return username;
  }

  public void setUsername(@Nullable String username) {
    this.username = username;
  }

  @Nullable
  public String getPassword() {
    return password;
  }

  public void setPassword(@Nullable String password) {
    this.password = password;
  }

  public boolean isSsl() {
    return ssl;
  }

  public void setSsl(boolean ssl) {
    this.ssl = ssl;
  }

  @Nonnull
  public StartTLSOptions getStarttls() {
    return starttls;
  }

  public void setStarttls(@Nonnull StartTLSOptions starttls) {
    this.starttls = starttls;
  }

  public int getMaxPoolSize() {
    return maxPoolSize;
  }

  public void setMaxPoolSize(int maxPoolSize) {
    this.maxPoolSize = maxPoolSize;
  }

  public boolean isKeepAlive() {
    return keepAlive;
  }

  public void setKeepAlive(boolean keepAlive) {
    this.keepAlive = keepAlive;
  }
}
