package cn.idealframework2.autoconfigure.id.snowflake;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/8/14
 */
public class SnowflakeProperties {
  @Nonnull
  private Factory factory = Factory.fixed;

  private int dataCenterId = 0;

  private int machineId = 0;

  public enum Factory {
    /** 固定值 */
    fixed,
    /** redis作为注册中心计算 */
    redis
  }

  @Nonnull
  public Factory getFactory() {
    return factory;
  }

  public void setFactory(@Nonnull Factory factory) {
    this.factory = factory;
  }

  public int getDataCenterId() {
    return dataCenterId;
  }

  public void setDataCenterId(int dataCenterId) {
    this.dataCenterId = dataCenterId;
  }

  public int getMachineId() {
    return machineId;
  }

  public void setMachineId(int machineId) {
    this.machineId = machineId;
  }
}
