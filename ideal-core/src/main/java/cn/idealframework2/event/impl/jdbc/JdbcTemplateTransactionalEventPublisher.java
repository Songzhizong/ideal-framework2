package cn.idealframework2.event.impl.jdbc;

import cn.idealframework2.event.*;
import cn.idealframework2.json.JsonUtils;
import cn.idealframework2.lang.CollectionUtils;
import cn.idealframework2.lang.Joiner;
import cn.idealframework2.lang.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2022/11/22
 */
public class JdbcTemplateTransactionalEventPublisher
  implements TransactionalEventPublisher, ApplicationRunner, DisposableBean {
  private static final Logger log = LoggerFactory.getLogger(JdbcTemplateTransactionalEventPublisher.class);
  /** 一次读取的最大消息数量 */
  private static final int BATCH_SIZE = 100;
  private final AtomicBoolean running = new AtomicBoolean(false);
  @SuppressWarnings("AlibabaThreadPoolCreation")
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final int[] argTypes = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BIGINT};
  private final JdbcTemplate jdbcTemplate;
  private final DataSource dataSource;
  private final DirectEventPublisher eventPublisher;

  public JdbcTemplateTransactionalEventPublisher(@Nonnull JdbcTemplate jdbcTemplate,
                                                 @Nonnull DataSource dataSource,
                                                 @Nonnull DirectEventPublisher eventPublisher) {
    this.jdbcTemplate = jdbcTemplate;
    this.dataSource = dataSource;
    this.eventPublisher = eventPublisher;
  }

  @Override
  @SuppressWarnings("DuplicatedCode")
  public void publish(@Nullable Collection<EventSupplier> suppliers) {
    if (CollectionUtils.isEmpty(suppliers)) {
      return;
    }
    String sql = "insert into ideal_event_publish_temp (event_info, topic, exchange, timestamp) values (?, ?, ?, ?)";
    List<Object[]> batchArgs = new ArrayList<>();
    for (EventSupplier supplier : suppliers) {
      Event event = supplier.getEvent();
      Class<? extends Event> clazz = event.getClass();
      cn.idealframework2.event.annotation.Event annotation = clazz.getAnnotation(cn.idealframework2.event.annotation.Event.class);
      if (annotation == null) {
        throw new RuntimeException("event 实现类:" + clazz.getName() + " 缺少 @cn.idealframework2.event.annotation.Event 注解");
      }
      String eventInfo = JsonUtils.toJsonString(event);
      long timestamp = System.currentTimeMillis();
      String exchange = annotation.exchange();
      String topic = annotation.topic();
      Object[] objects = {eventInfo, topic, exchange, timestamp};
      batchArgs.add(objects);
    }
    jdbcTemplate.batchUpdate(sql, batchArgs, argTypes);
    log.debug("Batch save " + batchArgs.size() + " messages to temp table");
  }

  private void start() {
    boolean running = this.running.getAndSet(true);
    if (running) {
      return;
    }
    executor.execute(() -> {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        // ignore
      }
      while (this.running.get()) {
        execWhile();
      }
    });
  }

  private void execWhile() {
    AtomicBoolean sleepFlag = new AtomicBoolean(false);
    try (Connection connection = dataSource.getConnection()) {
      dbLock(connection, () -> {
        try {
          long startTimeMillis = System.currentTimeMillis();
          List<JdbcEventTemp> temps = loadEventTemps(connection);
          if (Lists.isEmpty(temps)) {
            sleepFlag.set(true);
            return;
          }
          List<Long> removeIds = new ArrayList<>();
          List<JsonStringEventSupplier> collect = temps.stream()
            .map(t -> {
              removeIds.add(t.getId());
              String eventInfo = t.getEventInfo();
              String topic = t.getTopic();
              String exchange = t.getExchange();
              return new JsonStringEventSupplier(eventInfo, topic, exchange);
            })
            .collect(Collectors.toList());
          eventPublisher.directPublish(collect);
          removeEventTemps(connection, removeIds);
          long consuming = System.currentTimeMillis() - startTimeMillis;
          log.debug(
            "Read {} messages from the database and publish them, It takes {} milliseconds",
            temps.size(), consuming);
        } catch (Exception e) {
          sleepFlag.set(true);
          throw e;
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      log.warn("dataSource.getConnection() ex:", e);
      sleepFlag.set(true);
    } finally {
      if (sleepFlag.get()) {
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          //ignore
        }
      }
      sleepFlag.set(false);
    }
  }

  @Nonnull
  private List<JdbcEventTemp> loadEventTemps(@Nonnull Connection connection) {
    String sql = "select id, event_info, topic, exchange, timestamp from ideal_event_publish_temp order by id limit ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, BATCH_SIZE);
      try (ResultSet resultSet = statement.executeQuery()) {
        List<JdbcEventTemp> res = new ArrayList<>();
        while (resultSet.next()) {
          long id = resultSet.getLong("id");
          String eventInfo = resultSet.getString("event_info");
          String topic = resultSet.getString("topic");
          String exchange = resultSet.getString("exchange");
          long timestamp = resultSet.getLong("timestamp");
          res.add(new JdbcEventTemp(id, eventInfo, topic, exchange, timestamp));
        }
        return res;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
//    return jdbcTemplate.query(sql, (rs, rn) -> {
//      long id = rs.getLong("id");
//      String eventInfo = rs.getString("event_info");
//      String topic = rs.getString("topic");
//      String exchange = rs.getString("exchange");
//      long timestamp = rs.getLong("timestamp");
//      return new JdbcEventTemp(id, eventInfo, topic, exchange, timestamp);
//    });
  }

  private void removeEventTemps(@Nonnull Connection connection, @Nonnull Collection<Long> ids) {
    String join = Joiner.join(ids, ", ", "(", ")");
    String sql = "delete from ideal_event_publish_temp where id in " + join;
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      int update = statement.executeUpdate();
      log.debug("成功删除已成功发布的消息 {}条", update);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void dbLock(@Nonnull Connection connection, @Nonnull Runnable runnable) {
    String lockSql = "select lock_name from ideal_event_lock where lock_name = 'transaction_publish' for update";
    boolean rollback = false;
    boolean tempAutoCommit;
    try {
      tempAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);
    } catch (SQLException e) {
      return;
    }
    try (PreparedStatement preparedStatement = connection.prepareStatement(lockSql)) {
      preparedStatement.execute();
      try {
        runnable.run();
      } catch (Exception e) {
        rollback = true;
        log.warn("DB lock running exception: ", e);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (rollback) {
          log.debug("Rollback transaction");
          connection.rollback();
        } else {
          connection.commit();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      try {
        connection.setAutoCommit(tempAutoCommit);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void destroy() {
    running.set(false);
    executor.shutdown();
  }

  @Override
  public void run(ApplicationArguments args) {
    this.start();
  }
}
