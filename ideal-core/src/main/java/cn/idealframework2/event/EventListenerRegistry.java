package cn.idealframework2.event;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * @author 宋志宗 on 2022/9/29
 */
public interface EventListenerRegistry {

  /**
   * 监听事件
   *
   * @param name     监听器的全局唯一名称
   * @param clazz    事件类型
   * @param consumer 处理逻辑
   * @param <E>      事件类型
   */
  <E> void register(@Nonnull String name,
                    @Nonnull Class<E> clazz,
                    @Nonnull Consumer<E> consumer);
}
