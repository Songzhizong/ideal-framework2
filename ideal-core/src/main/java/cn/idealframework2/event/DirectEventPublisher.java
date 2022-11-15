package cn.idealframework2.event;

import cn.idealframework2.json.JsonUtils;
import cn.idealframework2.lang.CollectionUtils;
import cn.idealframework2.lang.StringUtils;

import javax.annotation.Nullable;
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
  default void publish(@Nullable Collection<EventSupplier> suppliers) {
    if (CollectionUtils.isEmpty(suppliers)) {
      return;
    }
    List<JsonStringEventSupplier> collect = suppliers.stream().map(supplier -> {
      Event event = supplier.getEvent();
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
      String eventJsonString = JsonUtils.toJsonString(event);
      return new JsonStringEventSupplier(eventJsonString, topic, exchange);
    }).collect(Collectors.toList());
    directPublish(collect);
  }

  void directPublish(@Nullable Collection<JsonStringEventSupplier> suppliers);
}
