package cn.idealframework2.event;

import cn.idealframework2.lang.StringUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2022/11/4
 */
public interface DirectEventPublisher extends EventPublisher {

  /**
   * 批量发布
   *
   * @param suppliers EventMessageSupplier Collection
   * @author 宋志宗 on 2021/10/19
   */
  @Override
  @SuppressWarnings("DuplicatedCode")
  default void publish(@Nonnull Collection<EventSupplier> suppliers) {
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
    directPublish(collect);
  }

  void directPublish(@Nonnull Collection<DirectEventSupplier> suppliers);
}