package cn.idealframework2.id.snowflake;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * TODO 基于关系型数据库的SnowFlake生成器工厂
 *
 * @author 宋志宗 on 2022/10/12
 */
public class JdbcSnowflakeFactory implements SnowflakeFactory, SnowflakeMachineIdHolder {
  private static final Logger log = LoggerFactory.getLogger(JdbcSnowflakeFactory.class);

  @Override
  public long dataCenterId() {
    return 0;
  }

  @Override
  public long machineId() {
    return 0;
  }

  @Nonnull
  @Override
  public Snowflake getGenerator(@Nonnull String biz) {
    return null;
  }

  @Override
  public long getCurrentMachineId() {
    return 0;
  }
}
