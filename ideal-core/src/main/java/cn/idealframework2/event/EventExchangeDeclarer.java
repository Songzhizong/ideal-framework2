package cn.idealframework2.event;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 用于申明交换机, 由具体的事件代理实现去自动化创建相应的Exchange或Topic或其他
 *
 * @author 宋志宗 on 2022/12/6
 */
public interface EventExchangeDeclarer {

  @Nonnull
  List<String> exchanges();
}
