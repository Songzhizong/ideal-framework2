package cn.idealframework2.example.event.coroutine;

import cn.idealframework2.event.EventExchangeDeclarer;
import cn.idealframework2.lang.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author 宋志宗 on 2022/12/6
 */
@Component
public class ExampleEventExchangeDeclarer implements EventExchangeDeclarer {
  @Nonnull
  @Override
  public List<String> exchanges() {
    return Lists.of(TestEvent.EXCHANGE);
  }
}
