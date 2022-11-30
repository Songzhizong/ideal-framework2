package cn.idealframework2.vertx.mail;

import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailResult;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * @author 宋志宗 on 2022/11/30
 */
public class AsyncMailClient {
  private final MailClient mailClient;

  public AsyncMailClient(@Nonnull MailClient mailClient) {
    this.mailClient = mailClient;
  }

  @Nonnull
  public CompletableFuture<MailResult> send(@Nonnull MailMessage email) {
    return (CompletableFuture<MailResult>) mailClient.sendMail(email).toCompletionStage();
  }
}
