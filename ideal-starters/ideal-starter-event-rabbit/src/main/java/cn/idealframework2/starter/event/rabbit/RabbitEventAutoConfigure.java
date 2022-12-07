package cn.idealframework2.starter.event.rabbit;

import cn.idealframework2.autoconfigure.event.EventProperties;
import cn.idealframework2.autoconfigure.event.EventRabbitProperties;
import cn.idealframework2.event.DirectEventPublisher;
import cn.idealframework2.event.ReactiveDirectEventPublisher;
import cn.idealframework2.event.rabbit.AmqpEventPublisher;
import cn.idealframework2.event.rabbit.RabbitEventListenerManager;
import cn.idealframework2.event.rabbit.ReactiveAmqpEventPublisher;
import cn.idealframework2.idempotent.IdempotentHandler;
import cn.idealframework2.idempotent.IdempotentHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * @author 宋志宗 on 2022/9/30
 */
public class RabbitEventAutoConfigure {
  private static final Logger log = LoggerFactory.getLogger(RabbitEventAutoConfigure.class);


  @Bean
  @Primary
  public DirectEventPublisher directEventPublisher(@Nonnull AmqpTemplate amqpTemplate) {
    return new AmqpEventPublisher(amqpTemplate);
  }

  @Bean
  @Primary
  public ReactiveDirectEventPublisher reactiveDirectEventPublisher(@Nonnull AmqpTemplate amqpTemplate) {
    return new ReactiveAmqpEventPublisher(amqpTemplate);
  }

  @Bean
  public RabbitEventListenerManager rabbitEventListenerManager(@Nonnull EventProperties eventProperties,
                                                               @Nonnull AmqpAdmin amqpAdmin,
                                                               @Nonnull ConfigurableApplicationContext applicationContext,
                                                               @Nonnull IdempotentHandlerFactory idempotentHandlerFactory) {
    EventRabbitProperties rabbit = eventProperties.getRabbit();
    boolean temporary = rabbit.isTemporary();
    String queuePrefix = rabbit.formattedQueuePrefix();
    Duration timeout = eventProperties.getIdempotent().getTimeout();
    IdempotentHandler idempotentHandler
      = idempotentHandlerFactory.create("event", timeout);
    AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
    SingletonBeanRegistry singletonBeanRegistry = (SingletonBeanRegistry) beanFactory;
    return new RabbitEventListenerManager(
      temporary, queuePrefix, amqpAdmin, idempotentHandler, applicationContext, singletonBeanRegistry);
  }

  @Bean
  public SimpleMessageListenerContainer simpleMessageListenerContainer(
    @Nonnull EventProperties eventProperties,
    @Nonnull RabbitEventListenerManager listenerManager,
    @Nonnull ConnectionFactory connectionFactory) {
    EventRabbitProperties rabbit = eventProperties.getRabbit();
    int consumers = rabbit.getConsumers();
    int prefetchCount = rabbit.getPrefetchCount();
    log.info("Rabbit listener consumers = {}, prefetchCount = {}", consumers, prefetchCount);
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
    container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    container.setMessageListener(listenerManager);
    container.setConcurrentConsumers(consumers);
    container.setMaxConcurrentConsumers(consumers);
    container.setPrefetchCount(prefetchCount);
    return container;
  }
}
