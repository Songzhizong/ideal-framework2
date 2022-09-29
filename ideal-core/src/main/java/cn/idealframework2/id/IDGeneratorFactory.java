package cn.idealframework2.id;

import javax.annotation.Nonnull;

/**
 * id生成器工厂接口
 *
 * @author 宋志宗 on 2020/10/10 4:21 下午
 */
public interface IDGeneratorFactory {

  /**
   * 根据业务类型获取id生成器
   *
   * @param biz 业务类型
   * @return id生成器
   */
  @Nonnull
  IDGenerator getGenerator(@Nonnull String biz);

  /**
   * 获取一个通用的id生成器
   *
   * @return id生成器
   */
  @Nonnull
  default IDGenerator getGenerator() {
    return getGenerator("$$default$$");
  }
}
