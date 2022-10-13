package cn.idealframework2.event;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * @author 宋志宗 on 2022/9/29
 */
public interface EventListenerManager {

  /**
   * 监听事件
   *
   * @param name     监听器的全局唯一名称
   * @param topic    事件主题
   * @param clazz    事件类型
   * @param consumer 处理逻辑
   * @param <E>      事件类型
   * @return 事件监听器
   */
  <E extends Event> EventListener listen(@Nonnull String name,
                                         @Nonnull String topic,
                                         @Nonnull Class<E> clazz,
                                         @Nonnull Consumer<E> consumer);
}
