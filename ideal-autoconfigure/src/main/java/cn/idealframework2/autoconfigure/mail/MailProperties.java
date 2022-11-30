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

  /**
   * the max allowed number of open connections to the mail server if not set the default is 32
   */
  protected int maxPoolSize = 32;
  /**
   * SMTP server host. For instance, 'smtp.example.com'.
   */
  @Nullable
  private String host;
  /**
   * SMTP server port.
   */
  @Nullable
  private Integer port;
  /**
   * Login user of the SMTP server.
   */
  @Nullable
  private String username;
  /**
   * Login password of the SMTP server.
   */
  @Nullable
  private String password;
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
}
