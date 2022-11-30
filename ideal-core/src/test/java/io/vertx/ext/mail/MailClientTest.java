package io.vertx.ext.mail;

import io.vertx.core.Vertx;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author 宋志宗 on 2022/11/30
 */
@Ignore
public class MailClientTest {

  @Test
  public void test() throws ExecutionException, InterruptedException {
    String hostname = "smtp.163.com";
    String username = "zzsong91@163.com";
    String password = "SNDVENOAMIQPZURA";
    Vertx vertx = Vertx.vertx();
    MailConfig config = new MailConfig();
    config.setHostname(hostname);
    config.setSsl(true);
    config.setPort(465);
    config.setStarttls(StartTLSOptions.REQUIRED);
    config.setUsername(username);
    config.setPassword(password);
    MailClient mailClient = MailClient.createShared(vertx, config);
    MailMessage message = new MailMessage();
    message.setSubject("vertx");
    message.setFrom(username);
    //  message.setTo("huarj@***");
    message.setCc(username);
    message.setHtml("""
      <table border="1">
      <tr>
      <th>Heading</th>
      <th>Another Heading</th>
      </tr>
      <tr>
      <td>row 1, cell 1</td>
      <td>row 1, cell 2</td>
      </tr>
      <tr>
      <td>row 2, cell 1</td>
      <td>row 2, cell 2</td>
      </tr>
      </table>""");
    CompletableFuture<MailResult> future = (CompletableFuture<MailResult>) mailClient.sendMail(message).toCompletionStage();
    future.get();
  }
}
