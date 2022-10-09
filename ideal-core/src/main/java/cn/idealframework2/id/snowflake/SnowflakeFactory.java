package cn.idealframework2.id.snowflake;

import cn.idealframework2.id.IDGeneratorFactory;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/9/13
 */
@SuppressWarnings("unused")
public interface SnowflakeFactory extends IDGeneratorFactory {

  /**
   * 数据中心id
   *
   * @return 数据中心id
   */
  long dataCenterId();

  /**
   * 机器id
   *
   * @return 机器id
   */
  long machineId();

  /**
   * 获取id生成器
   *
   * @param biz 业务类型
   * @return id生成器
   */
  @Nonnull
  @Override
  Snowflake getGenerator(@Nonnull String biz);
}
