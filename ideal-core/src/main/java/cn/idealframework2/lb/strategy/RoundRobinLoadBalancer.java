package cn.idealframework2.lb.strategy;

import cn.idealframework2.lb.LbServer;
import cn.idealframework2.lb.LoadBalancer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询策略
 *
 * @author 宋志宗 on 2020/8/19
 */
public class RoundRobinLoadBalancer<Server extends LbServer> implements LoadBalancer<Server> {
  private final AtomicInteger defaultCounter
    = new AtomicInteger(ThreadLocalRandom.current().nextInt(100));
  private final ConcurrentMap<Object, AtomicInteger> counterMap = new ConcurrentHashMap<>();

  @Override
  @Nullable
  public Server chooseServer(@Nullable Object key,
                             @Nonnull List<Server> servers) {
    if (servers.isEmpty()) {
      return null;
    }
    int size = servers.size();
    if (size == 1) {
      return servers.get(0);
    }
    AtomicInteger counter = defaultCounter;
    if (key != null) {
      counter = counterMap.computeIfAbsent(key,
        (k) -> new AtomicInteger(ThreadLocalRandom.current().nextInt(100)));
    }
    int abs = Math.abs(counter.incrementAndGet());
    return servers.get(abs % size);
  }
}
