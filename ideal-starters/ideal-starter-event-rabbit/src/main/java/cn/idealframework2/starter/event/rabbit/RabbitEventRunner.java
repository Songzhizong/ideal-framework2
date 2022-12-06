package cn.idealframework2.starter.event.rabbit;

import cn.idealframework2.event.rabbit.RabbitEventQueues;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/30
 */
public class RabbitEventRunner implements ApplicationRunner {
  private final SimpleMessageListenerContainer listenerContainer;

  public RabbitEventRunner(@Nonnull SimpleMessageListenerContainer listenerContainer) {
    this.listenerContainer = listenerContainer;
  }


  @Override
  public void run(ApplicationArguments args) {
    String[] queues = RabbitEventQueues.getQueues().toArray(new String[0]);
    listenerContainer.addQueueNames(queues);
  }
}
