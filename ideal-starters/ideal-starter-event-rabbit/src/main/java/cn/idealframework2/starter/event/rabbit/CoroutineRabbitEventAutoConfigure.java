package cn.idealframework2.starter.event.rabbit;

import cn.idealframework2.event.coroutine.rabbit.CoroutineRabbitEventListenerRegistry;
import cn.idealframework2.event.rabbit.RabbitEventListenerManager;
import cn.idealframework2.kotlin.KotlinModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/12/6
 */
@ConditionalOnClass({KotlinModel.class})
public class CoroutineRabbitEventAutoConfigure {

  @Bean
  public CoroutineRabbitEventListenerRegistry coroutineRabbitEventListenerRegistry(
    @Nonnull ApplicationContext applicationContext,
    @Nonnull RabbitEventListenerManager rabbitEventListenerManager
  ) {
    return new CoroutineRabbitEventListenerRegistry(applicationContext, rabbitEventListenerManager);
  }
}
