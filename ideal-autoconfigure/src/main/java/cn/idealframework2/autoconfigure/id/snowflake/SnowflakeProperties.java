package cn.idealframework2.autoconfigure.id.snowflake;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/8/14
 */
public class SnowflakeProperties {
  @Nonnull
  private Factory factory = Factory.FIXED;

  /** 数据中心id, 无论任何类型都需要自行指定 */
  private int dataCenterId = 0;

  /** 机器id, 仅适用于fixed类型 */
  private int machineId = 0;

  public enum Factory {
    /** 机器码采用固定值 */
    FIXED,
    /** redis作为机器码注册中心计算 */
    REDIS,
    /** 关系型数据库作为机器码注册中心 */
    JDBC,
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
