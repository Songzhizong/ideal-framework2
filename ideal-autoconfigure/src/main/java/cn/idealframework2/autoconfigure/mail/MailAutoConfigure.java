package cn.idealframework2.autoconfigure.mail;

import cn.idealframework2.lang.StringUtils;
import cn.idealframework2.vertx.mail.AsyncMailClient;
import cn.idealframework2.vertx.mail.ReactiveMailClient;
import io.vertx.core.Vertx;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.StartTLSOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/11/30
 */
public class MailAutoConfigure {
  private static final Logger log = LoggerFactory.getLogger(MailAutoConfigure.class);

  @Bean
  @Nullable
  public MailClient mailClient(@Nonnull MailProperties mailProperties) {
    boolean ssl = mailProperties.isSsl();
    String host = mailProperties.getHost();
    Integer port = mailProperties.getPort();
    String username = mailProperties.getUsername();
    String password = mailProperties.getPassword();
    boolean keepAlive = mailProperties.isKeepAlive();
    int maxPoolSize = mailProperties.getMaxPoolSize();
    StartTLSOptions starttls = mailProperties.getStarttls();
    if (StringUtils.isAllBlank(host, username, password)) {
      return null;
    }
    if (StringUtils.isBlank(host)) {
      log.warn("无法创建邮件客户端实例, ideal-mail.host为空");
      return null;
    }
    if (StringUtils.isBlank(username)) {
      log.warn("无法创建邮件客户端实例, ideal-mail.username");
      return null;
    }
    if (StringUtils.isBlank(password)) {
      log.warn("无法创建邮件客户端实例, ideal-mail.password");
      return null;
    }
    MailConfig config = new MailConfig()
      .setSsl(ssl)
      .setHostname(host)
      .setUsername(username)
      .setPassword(password)
      .setStarttls(starttls)
      .setKeepAlive(keepAlive)
      .setMaxPoolSize(maxPoolSize);
    if (port != null) {
      config.setPort(port);
    }
    return MailClient.createShared(Vertx.vertx(), config, "ideal-mail");
  }

  @Bean
  @Nullable
  public AsyncMailClient asyncMailClient(@Nullable MailClient mailClient) {
    if (mailClient == null) {
      return null;
    }
    return new AsyncMailClient(mailClient);
  }

  @Bean
  @Nullable
  public ReactiveMailClient reactiveMailClient(@Nullable MailClient mailClient) {
    if (mailClient == null) {
      return null;
    }
    return new ReactiveMailClient(mailClient);
  }
}
