package cn.idealframework2.event.impl.rabbit;

import cn.idealframework2.event.EventListener;
import com.rabbitmq.client.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.boot.ApplicationArguments;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author 宋志宗 on 2022/9/29
 */
public class RabbitEventListenerManager implements ChannelAwareMessageListener, EventListenerManager {
  private static final Log log = LogFactory.getLog(RabbitEventListenerManager.class);
  private final Map<String, RabbitEventListener<?>> listenerMap = new HashMap<>();

  @Override
  public void onMessage(Message message, Channel channel) throws Exception {
    if (channel == null) {
      log.warn("channel is null");
      return;
    }

  }

  @Override
  public <E> EventListener listen(@Nonnull String name,
                                  @Nonnull String topic,
                                  @Nonnull Class<E> clazz,
                                  @Nonnull Consumer<E> consumer) {
    
    return null;
  }

  public static class RabbitEventListener<E> implements EventListener {
    private final Class<E> clazz;
    private final Consumer<E> consumer;

    public RabbitEventListener(@Nonnull Class<E> clazz,
                               @Nonnull Consumer<E> consumer) {
      this.clazz = clazz;
      this.consumer = consumer;
    }

    public void accept(@Nonnull E e) {
      consumer.accept(e);
    }

    @Nonnull
    @Override
    public String name() {
      return null;
    }

    @Nonnull
    @Override
    public String topic() {
      return null;
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
  }

}
