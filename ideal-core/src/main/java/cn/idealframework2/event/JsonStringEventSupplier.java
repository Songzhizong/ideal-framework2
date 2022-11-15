package cn.idealframework2.event;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/11/4
 */
public record JsonStringEventSupplier(
  // 事件内容的json字符串格式
  @Nonnull String eventJsonString,
  // 事件的主题
  @Nonnull String topic,
  // 投递交换区
  @Nonnull String exchange
) {
}
