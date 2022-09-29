package cn.idealframework2.event;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationRunner;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/4/6
 */
public interface EventListener extends DisposableBean, ApplicationRunner {

  /**
   * 监听器名称
   *
   * @return 监听器名称
   */
  @Nonnull
  String name();

  /**
   * 监听的主题
   *
   * @return 监听的主图
   */
  @Nonnull
  String topic();
}
