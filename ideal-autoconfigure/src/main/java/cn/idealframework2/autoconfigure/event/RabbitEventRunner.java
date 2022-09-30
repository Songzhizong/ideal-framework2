package cn.idealframework2.autoconfigure.event;

import cn.idealframework2.event.impl.rabbit.RabbitEventListenerManager;
import cn.idealframework2.event.impl.rabbit.RabbitMessageListenerContainer;
import cn.idealframework2.starter.model.event.EventModel;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/9/30
 */
@ConditionalOnClass(EventModel.class)
public class RabbitEventRunner implements ApplicationRunner {
  private final RabbitEventListenerManager listenerManager;
  private final RabbitMessageListenerContainer listenerContainer;

  public RabbitEventRunner(@Nonnull RabbitEventListenerManager listenerManager,
                           @Nonnull RabbitMessageListenerContainer listenerContainer) {
    this.listenerManager = listenerManager;
    this.listenerContainer = listenerContainer;
  }


  @Override
  public void run(ApplicationArguments args) {
    String[] queues = listenerManager.getQueues().toArray(new String[0]);
    listenerContainer.addQueueNames(queues);
  }
}
