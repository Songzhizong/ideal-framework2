package cn.idealframework2.autoconfigure.event;

import cn.idealframework2.autoconfigure.event.properties.EventProperties;
import cn.idealframework2.autoconfigure.event.properties.EventRabbitProperties;
import cn.idealframework2.event.EventPublisher;
import cn.idealframework2.event.impl.rabbit.AmqpEventPublisher;
import cn.idealframework2.event.impl.rabbit.RabbitEventListenerManager;
import cn.idealframework2.event.impl.rabbit.RabbitMessageListenerContainer;
import cn.idealframework2.idempotent.IdempotentHandler;
import cn.idealframework2.idempotent.IdempotentHandlerFactory;
import cn.idealframework2.starter.model.event.EventModel;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * @author 宋志宗 on 2022/9/30
 */
@ConditionalOnClass(EventModel.class)
public class RabbitEventAutoConfigure {

  @Bean
  @Primary
  public EventPublisher eventPublisher(@Nonnull EventProperties properties,
                                       @Nonnull AmqpAdmin amqpAdmin,
                                       @Nonnull AmqpTemplate amqpTemplate) {
    EventRabbitProperties rabbit = properties.getRabbit();
    String exchange = rabbit.getExchange();
    return new AmqpEventPublisher(exchange, amqpAdmin, amqpTemplate);
  }

  @Bean
  public RabbitEventListenerManager rabbitEventListenerManager(@Nonnull EventProperties eventProperties,
                                                               @Nonnull AmqpAdmin amqpAdmin,
                                                               @Nonnull IdempotentHandlerFactory idempotentHandlerFactory) {
    EventRabbitProperties rabbit = eventProperties.getRabbit();
    boolean temporary = rabbit.isTemporary();
    String exchange = rabbit.getExchange();
    String queuePrefix = rabbit.formattedQueuePrefix();
    Duration timeout = eventProperties.getIdempotent().getTimeout();
    IdempotentHandler idempotentHandler
      = idempotentHandlerFactory.create("event", timeout);
    return new RabbitEventListenerManager(
      temporary, exchange, queuePrefix, amqpAdmin, idempotentHandler);
  }

  @Bean
  public RabbitMessageListenerContainer rabbitMessageListenerContainer(
    @Nonnull EventProperties eventProperties,
    @Nonnull RabbitEventListenerManager listenerManager,
    @Nonnull ConnectionFactory connectionFactory) {
    EventRabbitProperties rabbit = eventProperties.getRabbit();
    int consumers = rabbit.getConsumers();
    int prefetchCount = rabbit.getPrefetchCount();
    return new RabbitMessageListenerContainer(listenerManager, connectionFactory, consumers, consumers, prefetchCount);
  }
}
