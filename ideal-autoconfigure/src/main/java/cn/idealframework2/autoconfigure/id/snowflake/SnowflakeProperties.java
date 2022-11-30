package cn.idealframework2.autoconfigure.id.snowflake;

/**
 * @author 宋志宗 on 2022/8/14
 */
public class SnowflakeProperties {

  /** 数据中心id, 无论任何类型都需要自行指定 */
  private int dataCenterId = 0;

  /** 机器id, 仅适用于fixed类型 */
  private int machineId = 0;

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
