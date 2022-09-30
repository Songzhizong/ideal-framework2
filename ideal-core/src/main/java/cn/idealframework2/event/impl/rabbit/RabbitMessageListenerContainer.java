
package cn.idealframework2.event.impl.rabbit;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

/**
 * @author 宋志宗 on 2021/4/25
 */
public class RabbitMessageListenerContainer extends SimpleMessageListenerContainer {

  public RabbitMessageListenerContainer(RabbitEventListenerManager listenerManager,
                                        ConnectionFactory connectionFactory,
                                        int concurrentConsumers,
                                        int maxConcurrentConsumers,
                                        int prefetchCount) {
    super(connectionFactory);
    if (maxConcurrentConsumers < concurrentConsumers) {
      maxConcurrentConsumers = concurrentConsumers;
    }
    if (prefetchCount < 0) {
      prefetchCount = 0;
    }
    this.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    this.setMessageListener(listenerManager);
    this.setConcurrentConsumers(concurrentConsumers);
    this.setMaxConcurrentConsumers(maxConcurrentConsumers);
    this.setPrefetchCount(prefetchCount);
  }
}
