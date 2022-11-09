package cn.idealframework2.event;

import cn.idealframework2.lang.CollectionUtils;
import cn.idealframework2.lang.StringUtils;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2022/11/4
 */
public interface ReactiveDirectEventPublisher extends ReactiveEventPublisher {

  /**
   * 发布事件
   *
   * @param suppliers 事件提供者
   * @return 发布结果
   */
  @Nonnull
  @SuppressWarnings("DuplicatedCode")
  default Mono<Boolean> publish(@Nullable Collection<EventSupplier> suppliers) {
    if (CollectionUtils.isEmpty(suppliers)) {
      return Mono.just(true);
    }
    List<DirectEventSupplier> collect = suppliers.stream().map(supplier -> {
      Event event = supplier.get();
      Class<? extends Event> clazz = event.getClass();
      cn.idealframework2.event.annotation.Event annotation = clazz.getAnnotation(cn.idealframework2.event.annotation.Event.class);
      if (annotation == null) {
        throw new RuntimeException("event 实现类:" + clazz.getName() + " 缺少 @cn.idealframework2.event.annotation.Event 注解");
      }
      String exchange = annotation.exchange();
      String topic = annotation.topic();
      if (StringUtils.isBlank(topic)) {
        throw new RuntimeException("event 实现类:" + clazz.getName() + " 获取topic为空");
      }
      return new DirectEventSupplier(event, topic, exchange);
    }).collect(Collectors.toList());
    return directPublish(collect);
  }

  /**
   * 指定exchange和topic进行事件发布
   *
   * @param suppliers 事件提供者
   * @return 发布结果
   */
  @Nonnull
  Mono<Boolean> directPublish(@Nullable Collection<DirectEventSupplier> suppliers);
}
