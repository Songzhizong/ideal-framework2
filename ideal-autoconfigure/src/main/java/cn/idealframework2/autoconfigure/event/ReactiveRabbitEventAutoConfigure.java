package cn.idealframework2.autoconfigure.event;

import cn.idealframework2.autoconfigure.event.properties.EventProperties;
import cn.idealframework2.autoconfigure.event.properties.EventRabbitProperties;
import cn.idealframework2.autoconfigure.event.properties.SpringRabbitProperties;
import cn.idealframework2.event.ReactiveDirectEventPublisher;
import cn.idealframework2.event.coroutine.EventListenerManager;
import cn.idealframework2.event.coroutine.RabbitEventListenerManager;
import cn.idealframework2.event.impl.rabbit.ReactorRabbitEventPublisher;
import cn.idealframework2.idempotent.coroutine.IdempotentHandler;
import cn.idealframework2.idempotent.coroutine.IdempotentHandlerFactory;
import cn.idealframework2.starter.model.event.coroutine.CoroutineEventModel;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.*;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * @author 宋志宗 on 2022/8/13
 */
@ConditionalOnClass(CoroutineEventModel.class)
public class ReactiveRabbitEventAutoConfigure {

  @Bean
  public ConnectionFactory connectionFactory(@Nonnull SpringRabbitProperties rabbitProperties) {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.useNio();
    connectionFactory.setUsername(rabbitProperties.getUsername());
    connectionFactory.setPassword(rabbitProperties.getPassword());
    connectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());
    return connectionFactory;
  }

  @Bean
  public Sender sender(@Nonnull ConnectionFactory connectionFactory,
                       @Nonnull SpringRabbitProperties rabbitProperties) {
    Address[] addresses = RabbitUtils.getRabbitAddresses(rabbitProperties);
    SenderOptions senderOptions = new SenderOptions()
      .resourceManagementScheduler(Schedulers.boundedElastic())
      .connectionFactory(connectionFactory)
      .connectionSupplier(cf -> cf.newConnection(addresses, "event-sender"));
    return RabbitFlux.createSender(senderOptions);
  }

  @Bean
  public Receiver receiver(@Nonnull ConnectionFactory connectionFactory,
                           @Nonnull SpringRabbitProperties rabbitProperties) {
    Address[] addresses = RabbitUtils.getRabbitAddresses(rabbitProperties);
    ReceiverOptions receiverOptions = new ReceiverOptions()
      .connectionFactory(connectionFactory)
      .connectionSubscriptionScheduler(Schedulers.boundedElastic())
      .connectionSupplier(cf -> cf.newConnection(addresses, "event-receiver"));
    return RabbitFlux.createReceiver(receiverOptions);
  }

  @Bean
  @Primary
  public ReactiveDirectEventPublisher directReactiveEventPublisher(@Nonnull Sender sender,
                                                                   @Nonnull EventProperties properties) {
    EventRabbitProperties rabbit = properties.getRabbit();
    String exchange = rabbit.getExchange();
    return new ReactorRabbitEventPublisher(sender, exchange);
  }

  @Bean
  public EventListenerManager eventListenerManager(@Nonnull EventProperties eventProperties,
                                                   @Nonnull Sender sender,
                                                   @Nonnull Receiver receiver,
                                                   @Nonnull ConfigurableApplicationContext applicationContext,
                                                   @Nonnull IdempotentHandlerFactory idempotentHandlerFactory) {
    EventRabbitProperties rabbit = eventProperties.getRabbit();
    String exchange = rabbit.getExchange();
    boolean temporary = rabbit.isTemporary();
    String queuePrefix = rabbit.getQueuePrefix();
    Duration timeout = eventProperties.getIdempotent().getTimeout();
    IdempotentHandler idempotentHandler =
      idempotentHandlerFactory.create("event", timeout);
    AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
    SingletonBeanRegistry singletonBeanRegistry = (SingletonBeanRegistry) beanFactory;
    return new RabbitEventListenerManager(
      exchange, temporary, queuePrefix, sender, receiver, idempotentHandler, singletonBeanRegistry);
  }
}
