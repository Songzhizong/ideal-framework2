package cn.idealframework2.autoconfigure.event;

import cn.idealframework2.autoconfigure.cache.CacheProperties;
import cn.idealframework2.autoconfigure.event.properties.EventProperties;
import cn.idealframework2.autoconfigure.event.properties.EventRabbitProperties;
import cn.idealframework2.event.EventPublisher;
import cn.idealframework2.event.impl.rabbit.AmqpEventPublisher;
import cn.idealframework2.event.impl.rabbit.RabbitEventListenerManager;
import cn.idealframework2.event.impl.rabbit.RabbitMessageListenerContainer;
import cn.idealframework2.starter.model.event.EventModel;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;

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
                                                               @Nonnull CacheProperties cacheProperties,
                                                               @Nonnull AmqpAdmin amqpAdmin,
                                                               @Nonnull StringRedisTemplate redisTemplate) {
    EventRabbitProperties rabbit = eventProperties.getRabbit();
    boolean temporary = rabbit.isTemporary();
    String exchange = rabbit.getExchange();
    String queuePrefix = rabbit.formattedQueuePrefix();
    String cachePrefix = cacheProperties.formattedPrefix();
    return new RabbitEventListenerManager(temporary, exchange, queuePrefix, cachePrefix, amqpAdmin, redisTemplate);
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
