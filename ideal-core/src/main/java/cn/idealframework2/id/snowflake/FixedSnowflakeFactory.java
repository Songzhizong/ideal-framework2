package cn.idealframework2.id.snowflake;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 宋志宗 on 2021/6/7
 */
public class FixedSnowflakeFactory implements SnowflakeFactory {
  private final Map<String, Snowflake> idGeneratorMap = new ConcurrentHashMap<>();

  private final long dataCenterId;
  private final long machineId;

  public FixedSnowflakeFactory(long dataCenterId, long machineId) {
    this.dataCenterId = dataCenterId;
    this.machineId = machineId;
  }

  @Override
  public long dataCenterId() {
    return dataCenterId;
  }

  @Override
  public long machineId() {
    return machineId;
  }

  @Nonnull
  @Override
  public Snowflake getGenerator(@Nonnull String biz) {
    return idGeneratorMap.computeIfAbsent(biz, k -> new Snowflake(dataCenterId, machineId));
  }
}
