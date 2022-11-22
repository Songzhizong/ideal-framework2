package cn.idealframework2.autoconfigure.event;

import cn.idealframework2.event.DirectEventPublisher;
import cn.idealframework2.event.TransactionalEventPublisher;
import cn.idealframework2.event.impl.jdbc.JdbcTemplateTransactionalEventPublisher;
import cn.idealframework2.starter.model.event.EventModel;
import cn.idealframework2.utils.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.DataSource;

/**
 * @author 宋志宗 on 2022/9/30
 */
@ConditionalOnExpression("""
  '${ideal-event.transaction.type:mongo}'.equalsIgnoreCase('jdbc') && ${ideal-event.transaction.enabled:true}
  """)
@ConditionalOnClass(EventModel.class)
public class JdbcEventAutoConfigure {

  @Bean
  public TransactionalEventPublisher transactionalEventPublisher(@Nonnull JdbcTemplate jdbcTemplate,
                                                                 @Autowired(required = false)
                                                                 @Nullable DataSource dataSource,
                                                                 @Nonnull DirectEventPublisher directEventPublisher) {
    Asserts.nonnull(dataSource, "dataSource为空");
    return new JdbcTemplateTransactionalEventPublisher(jdbcTemplate, dataSource, directEventPublisher);
  }
}
