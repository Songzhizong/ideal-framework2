package cn.idealframework2.event;

/**
 * @author 宋志宗 on 2022/11/4
 */
public record DirectEventSupplier(
  Event event,
  String topic,
  String exchange
) {
}
