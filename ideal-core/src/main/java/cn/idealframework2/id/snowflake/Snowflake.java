package cn.idealframework2.id.snowflake;

import cn.idealframework2.id.IDGenerator;
import cn.idealframework2.utils.Asserts;

import javax.annotation.Nonnull;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 雪花id生成器
 * <pre>
 *   1位不同|时间戳|数据中心id|机器码|时钟回拨序号|序列号
 *   +----------+-----------+-------------+---------+--------+----------+
 *   |  ignore  | timestamp | data center | machine | clock  | sequence |
 *   |  1bytes  |  41bytes  |   3bytes    | 7bytes  | 2bytes | 10bytes  |
 *   +----------+-----------+-------------+---------+--------+----------+
 * </pre>
 *
 * @author 宋志宗 on 2020/9/2
 */
@SuppressWarnings("unused")
public class Snowflake implements IDGenerator {

  /** 默认起始时间戳 2019-09-03 09:46:37 */
  private static final long DEFAULT_START_TIMESTAMP = 1567475197889L;

  /** 起始时间戳 */
  private static long startTimestamp = DEFAULT_START_TIMESTAMP;

  /** 序列号位数, 单节点每毫秒最多生成1024个唯一ID */
  static final int SEQUENCE_BIT = 10;

  /** 时钟回拨序号位数, 支持短时间内4次时钟回拨 */
  static final int CLOCK_BIT = 2;

  /** 机器码占用的位数, 最多128个节点 */
  static final int MACHINE_BIT = 7;

  /** 数据中心占用的位数, 最多8个数据中心 */
  static final int DATA_CENTER_BIT = 3;

  /** 序列号最大值 */
  static final int MAX_SEQUENCE_NUM = (1 << SEQUENCE_BIT) - 1;

  /** 时钟回拨序号最大值 */
  static final int MAX_CLOCK_NUM = (1 << CLOCK_BIT) - 1;

  /** 机器码最大值 */
  static final int MAX_MACHINE_NUM = (1 << MACHINE_BIT) - 1;

  /** 数据中心最大值 */
  static final int MAX_DATA_CENTER_NUM = (1 << DATA_CENTER_BIT) - 1;

  /** 时钟回拨序号向左的位移 */
  private static final int CLOCK_LEFT = SEQUENCE_BIT;

  /** 机器码向左的位移 */
  private static final int MACHINE_LEFT = CLOCK_LEFT + CLOCK_BIT;

  /** 数据中心向左的位移 */
  private static final int DATA_CENTER_LEFT = MACHINE_LEFT + MACHINE_BIT;

  /** 时间戳向左的位移 */
  private static final int TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;


  /** 数据中心id */
  private final long dataCenterId;

  /** 机器id */
  private final SnowflakeMachineIdHolder machineIdHolder;

  private final Lock lock = new ReentrantLock();

  private long clock = 0L;
  private long sequence = 0L;
  private long lasTimestamp = -1L;

  public static void setStartTimestamp(long startTimestamp) {
    Asserts.assertTrue(startTimestamp == DEFAULT_START_TIMESTAMP, "禁止多次修改起始时间戳");
    Snowflake.startTimestamp = startTimestamp;
  }

  public Snowflake(long dataCenterId, long machineId) {
    this(dataCenterId, () -> machineId);
  }

  public Snowflake(long dataCenterId, @Nonnull SnowflakeMachineIdHolder machineIdHolder) {
    this.dataCenterId = dataCenterId;
    this.machineIdHolder = machineIdHolder;
    if (dataCenterId > MAX_DATA_CENTER_NUM) {
      throw new RuntimeException("dataCenterId 超过允许的最大值: " + dataCenterId + " > " + MAX_DATA_CENTER_NUM);
    }
    long machineId = machineIdHolder.getCurrentMachineId();
    if (machineId > MAX_MACHINE_NUM) {
      throw new RuntimeException("machineId 超过允许的最大值: " + machineId + " > " + MAX_MACHINE_NUM);
    }
  }

  @Override
  public long generate() {
    lock.lock();
    try {
      long currTimestamp = System.currentTimeMillis();
      if (currTimestamp < lasTimestamp) {
        clock = (clock + 1) & MAX_CLOCK_NUM;
        lasTimestamp = currTimestamp;
      }
      if (currTimestamp == lasTimestamp) {
        sequence = (sequence + 1) & MAX_SEQUENCE_NUM;
        if (sequence == 0L) {
          currTimestamp = getNextMill();
        }
      } else {
        sequence = 0L;
      }
      lasTimestamp = currTimestamp;
      long time = currTimestamp - startTimestamp;
      long machineId = machineIdHolder.getCurrentMachineId();
      return time << TIMESTAMP_LEFT
        | dataCenterId << DATA_CENTER_LEFT
        | machineId << MACHINE_LEFT
        | clock << CLOCK_LEFT
        | sequence;
    } finally {
      lock.unlock();
    }
  }

  private long getNextMill() {
    long mill = System.currentTimeMillis();
    while (mill <= lasTimestamp) {
      mill = System.currentTimeMillis();
    }
    return mill;
  }

  /**
   * 通过id还原时间戳
   *
   * @param id snowflake生成的id
   * @return 该id产生的时间戳
   * @author 宋志宗 on 2021/2/1
   */
  public static long restoreTimestamp(long id) {
    long difference = id >> TIMESTAMP_LEFT;
    if (difference <= 0) {
      throw new RuntimeException("非snowflake生成id");
    }
    return difference + startTimestamp;
  }

  /**
   * 通过一个基准时间戳生成其可产生的最小ID
   *
   * @param timestamp 基准时间
   * @return SnowFlake id
   * @author 宋志宗 on 2021/3/25
   */
  public static long generateMinValueByTimestamp(long timestamp) {
    return (timestamp - startTimestamp) << TIMESTAMP_LEFT;
  }
}
