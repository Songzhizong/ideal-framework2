package cn.idealframework2.event.rabbit;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 宋志宗 on 2022/12/6
 */
public class RabbitEventQueues {
  private static final Set<String> QUEUES = Collections.newSetFromMap(new ConcurrentHashMap<>());

  private RabbitEventQueues() {
  }

  public static void add(@Nonnull String queueName) {
    QUEUES.add(queueName);
  }

  @Nonnull
  public static Set<String> getQueues() {
    return QUEUES;
  }
}
