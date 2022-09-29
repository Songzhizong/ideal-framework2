package cn.idealframework2.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

/**
 * @author 宋志宗 on 2021/9/2
 */
public final class RedisTemplateUtils {
  private static final Log log = LogFactory.getLog(RedisTemplateUtils.class);
  private static final byte[] SCRIPT
    = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end"
    .getBytes(StandardCharsets.UTF_8);


  public static boolean tryLock(@Nonnull StringRedisTemplate redisTemplate,
                                @Nonnull String lockKey,
                                @Nonnull String lockValue,
                                @Nonnull Duration timeout) {
    Boolean lockSuccess = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, timeout);
    boolean tryLock = lockSuccess != null && lockSuccess;
    if (tryLock) {
      log.debug("成功加锁: " + lockKey);
    }
    return tryLock;
  }

  public static void unlock(@Nonnull StringRedisTemplate redisTemplate,
                            @Nonnull String lockKey, @Nonnull String lockValue) {
    RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
    RedisConnection connection = Objects.requireNonNull(factory).getConnection();
    Long eval = connection.scriptingCommands().<Long>eval(
      SCRIPT, ReturnType.INTEGER, 1,
      lockKey.getBytes(Charset.defaultCharset()),
      lockValue.getBytes(Charset.defaultCharset())
    );
    if (eval != null && eval > 0) {
      log.debug("成功释放锁: " + lockKey);
    }
  }

  @Nonnull
  public static Mono<Boolean> unlock(@Nonnull ReactiveStringRedisTemplate redisTemplate,
                                     @Nonnull String lockKey, @Nonnull String lockValue) {
    ReactiveRedisConnection connection = redisTemplate
      .getConnectionFactory().getReactiveConnection();
    return connection.scriptingCommands()
      .eval(ByteBuffer.wrap(SCRIPT), ReturnType.INTEGER, 1,
        ByteBuffer.wrap(lockKey.getBytes(Charset.defaultCharset())),
        ByteBuffer.wrap(lockValue.getBytes(Charset.defaultCharset())))
      .collectList()
      .map(list -> {
        long i = 0;
        for (Object o : list) {
          if (o != null) {
            i += (long) o;
          }
        }
        return i > 0;
      });
  }

  private RedisTemplateUtils() {
  }
}
