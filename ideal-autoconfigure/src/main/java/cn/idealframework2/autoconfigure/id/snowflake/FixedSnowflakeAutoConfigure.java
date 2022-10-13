package cn.idealframework2.autoconfigure.id.snowflake;

import cn.idealframework2.autoconfigure.id.IdProperties;
import cn.idealframework2.id.IDGeneratorFactory;
import cn.idealframework2.id.snowflake.FixedSnowflakeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/8/14
 */
@ConditionalOnExpression("""
  '${ideal-id.type:snowflake}'.equalsIgnoreCase('snowflake')
  &&'${ideal-id.snowflake.factory:fixed}'.equalsIgnoreCase('fixed')
  """)
public class FixedSnowflakeAutoConfigure {
  private static final Logger log = LoggerFactory.getLogger(FixedSnowflakeAutoConfigure.class);

  @Bean
  public IDGeneratorFactory idGeneratorFactory(@Nonnull IdProperties properties) {
    log.warn("当前选择的FixedSnowFlakeFactory如果未合理分配机器码将会出现不同节点产生相同id的问题");
    SnowflakeProperties snowflake = properties.getSnowflake();
    int dataCenterId = snowflake.getDataCenterId();
    int machineId = snowflake.getMachineId();
    return new FixedSnowflakeFactory(dataCenterId, machineId);
  }
}
