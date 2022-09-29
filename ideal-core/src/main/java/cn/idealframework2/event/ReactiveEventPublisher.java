package cn.idealframework2.event;

import cn.idealframework2.lang.Lists;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author 宋志宗 on 2022/8/13
 */
public interface ReactiveEventPublisher {

  /**
   * 发布事件
   *
   * @param suppliers 事件提供者
   * @return 发布结果
   */
  @Nonnull
  Mono<Boolean> publish(@Nonnull Collection<EventSupplier> suppliers);

  /**
   * 发布事件
   *
   * @param suppliers 事件提供者
   * @return 发布结果
   */
  @Nonnull
  default Mono<Boolean> publish(@Nonnull EventSuppliers suppliers) {
    ArrayList<EventSupplier> list = suppliers.get();
    return publish(list);
  }

  /**
   * 发布事件
   *
   * @param supplier 事件提供者
   * @return 发布结果
   */
  @Nonnull
  default Mono<Boolean> publish(@Nonnull EventSupplier supplier) {
    return publish(Lists.of(supplier));
  }
}
