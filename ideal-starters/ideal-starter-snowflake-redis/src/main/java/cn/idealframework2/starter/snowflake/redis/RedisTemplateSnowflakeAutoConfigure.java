package cn.idealframework2.starter.snowflake.redis;

import cn.idealframework2.autoconfigure.id.IdProperties;
import cn.idealframework2.autoconfigure.id.snowflake.SnowflakeProperties;
import cn.idealframework2.id.IDGeneratorFactory;
import cn.idealframework2.id.snowflake.RedisTemplateSnowflakeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2022/8/14
 */
public class RedisTemplateSnowflakeAutoConfigure {
  private static final Logger log = LoggerFactory.getLogger(RedisTemplateSnowflakeAutoConfigure.class);

  @Value("${spring.application.name:}")
  private String applicationName;

  @Bean
  public IDGeneratorFactory idGeneratorFactory(@Nonnull IdProperties properties,
                                               @Nonnull StringRedisTemplate stringRedisTemplate) {
    log.info("SpringRedisSnowFlakeFactory将使用redis作为机器码注册中心进行机器码计算");
    SnowflakeProperties snowflake = properties.getSnowflake();
    int dataCenterId = snowflake.getDataCenterId();
    return new RedisTemplateSnowflakeFactory(dataCenterId, 600, 30, applicationName, stringRedisTemplate);
  }
}
