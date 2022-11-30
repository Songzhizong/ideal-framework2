package cn.idealframework2.vertx.mail;

import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailResult;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * @author 宋志宗 on 2022/11/30
 */
public class ReactiveMailClient {
  private final MailClient mailClient;

  public ReactiveMailClient(@Nonnull MailClient mailClient) {
    this.mailClient = mailClient;
  }

  @Nonnull
  public Mono<MailResult> send(@Nonnull MailMessage email) {
    CompletableFuture<MailResult> future = (CompletableFuture<MailResult>) mailClient.sendMail(email).toCompletionStage();
    return Mono.fromFuture(future);
  }
}
