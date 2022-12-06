package cn.idealframework2.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author 宋志宗 on 2022/9/24
 */
@SuppressWarnings("unused")
public enum CommonPool implements ExecutorService {
  /** instance */
  INSTANCE;

  private static final Logger log = LoggerFactory.getLogger(CommonPool.class);
  @Nonnull
  private static final ThreadPoolExecutor EXECUTOR;
  @Nullable
  private static final ScheduledExecutorService SCHEDULED;

  static {
    String maxSize = System.getProperty("ideal.commonPool.maxSize", "512");
    int maximumPoolSize;
    try {
      maximumPoolSize = Integer.parseInt(maxSize);
    } catch (NumberFormatException e) {
      log.error("Ideal.commonPool.maxSize配置非int类型, 使用默认配置: 512");
      maximumPoolSize = 512;
    }
    EXECUTOR = new ThreadPoolExecutor(
      0, maximumPoolSize,
      60, TimeUnit.SECONDS, new SynchronousQueue<>(),
      BasicThreadFactory.builder().namingPattern("common-pool").build(),
      (r, executor) -> {
        log.warn("Common-pool线程池资源不足");
        r.run();
      });
    if (log.isDebugEnabled()) {
      SCHEDULED = Executors.newSingleThreadScheduledExecutor();
      int finalMaximumPoolSize = maximumPoolSize;
      SCHEDULED.scheduleAtFixedRate(() -> {
        int poolSize = EXECUTOR.getPoolSize();
        if (poolSize > 0) {
          int activeCount = EXECUTOR.getActiveCount();
          long taskCount = EXECUTOR.getTaskCount();
          log.debug("CommonPool(poolSize=" + poolSize + ", maximumPoolSize=" + finalMaximumPoolSize +
            ", activeCount=" + activeCount + ", taskCount=" + taskCount + ")");
        }
      }, 30, 30, TimeUnit.SECONDS);
    } else {
      SCHEDULED = null;
    }
    Runtime.getRuntime().addShutdownHook(new Thread(CommonPool.INSTANCE::shutdown));
  }

  @Override
  public void shutdown() {
    if (SCHEDULED != null) {
      SCHEDULED.shutdown();
    }
    EXECUTOR.shutdown();
  }

  @Nonnull
  @Override
  public List<Runnable> shutdownNow() {
    if (SCHEDULED != null) {
      SCHEDULED.shutdownNow();
    }
    return EXECUTOR.shutdownNow();
  }

  @Override
  public boolean isShutdown() {
    if (SCHEDULED != null) {
      return SCHEDULED.isShutdown() && EXECUTOR.isShutdown();
    }
    return EXECUTOR.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    if (SCHEDULED != null) {
      return SCHEDULED.isTerminated() && EXECUTOR.isTerminated();
    }
    return EXECUTOR.isTerminated();
  }

  @Override
  public boolean awaitTermination(long timeout,
                                  @Nonnull TimeUnit unit) throws InterruptedException {
    return EXECUTOR.awaitTermination(timeout, unit);
  }

  @Nonnull
  @Override
  public <T> Future<T> submit(@Nonnull Callable<T> task) {
    return EXECUTOR.submit(task);
  }

  @Nonnull
  @Override
  public <T> Future<T> submit(@Nonnull Runnable task, T result) {
    return EXECUTOR.submit(task, result);
  }

  @Nonnull
  @Override
  public Future<?> submit(@Nonnull Runnable task) {
    return EXECUTOR.submit(task);
  }

  @Nonnull
  @Override
  public <T> List<Future<T>> invokeAll(@Nonnull Collection<? extends Callable<T>> tasks)
    throws InterruptedException {
    return EXECUTOR.invokeAll(tasks);
  }

  @Nonnull
  @Override
  public <T> List<Future<T>> invokeAll(@Nonnull Collection<? extends Callable<T>> tasks,
                                       long timeout,
                                       @Nonnull TimeUnit unit) throws InterruptedException {
    return EXECUTOR.invokeAll(tasks, timeout, unit);
  }

  @Nonnull
  @Override
  public <T> T invokeAny(@Nonnull Collection<? extends Callable<T>> tasks)
    throws InterruptedException, ExecutionException {
    return EXECUTOR.invokeAny(tasks);
  }

  @Override
  public <T> T invokeAny(@Nonnull Collection<? extends Callable<T>> tasks,
                         long timeout, @Nonnull TimeUnit unit)
    throws InterruptedException, ExecutionException, TimeoutException {
    return EXECUTOR.invokeAny(tasks, timeout, unit);
  }

  @Override
  public void execute(@Nonnull Runnable command) {
    EXECUTOR.execute(command);
  }
}
