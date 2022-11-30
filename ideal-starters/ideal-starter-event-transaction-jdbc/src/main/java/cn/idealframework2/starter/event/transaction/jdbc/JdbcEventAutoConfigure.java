package cn.idealframework2.starter.event.transaction.jdbc;

import cn.idealframework2.event.DirectEventPublisher;
import cn.idealframework2.event.TransactionalEventPublisher;
import cn.idealframework2.event.impl.jdbc.JdbcTemplateTransactionalEventPublisher;
import cn.idealframework2.starter.model.event.EventModel;
import cn.idealframework2.utils.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.DataSource;

/**
 * @author 宋志宗 on 2022/9/30
 */
@ConditionalOnClass(EventModel.class)
public class JdbcEventAutoConfigure {
  private static final Logger log = LoggerFactory.getLogger(JdbcEventAutoConfigure.class);

  @Bean
  public TransactionalEventPublisher transactionalEventPublisher(@Nonnull JdbcTemplate jdbcTemplate,
                                                                 @Autowired(required = false)
                                                                 @Nullable DataSource dataSource,
                                                                 @Nonnull DirectEventPublisher directEventPublisher) {
    Asserts.nonnull(dataSource, "dataSource为空");
    log.info("Usage JdbcTemplateTransactionalEventPublisher");
    return new JdbcTemplateTransactionalEventPublisher(jdbcTemplate, dataSource, directEventPublisher);
  }
}
