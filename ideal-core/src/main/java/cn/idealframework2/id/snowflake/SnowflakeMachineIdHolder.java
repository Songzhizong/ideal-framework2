package cn.idealframework2.id.snowflake;

/**
 * @author 宋志宗 on 2021/9/30
 */
@FunctionalInterface
public interface SnowflakeMachineIdHolder {
  /**
   * 获取当前的机器id
   *
   * @return 机器id
   */
  long getCurrentMachineId();
}
