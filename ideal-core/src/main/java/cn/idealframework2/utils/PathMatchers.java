package cn.idealframework2.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author 宋志宗 on 2022/10/9
 */
public final class PathMatchers {
  private static final ConcurrentMap<String, PathPattern> PATTERNS = new ConcurrentHashMap<>();
  private static final Cache<String, PathContainer> PATH_CONTAINER_CACHE = Caffeine.newBuilder()
    .expireAfterAccess(Duration.ofMinutes(10))
    .expireAfterWrite(Duration.ofMinutes(60))
    .maximumSize(10000).build();

  private PathMatchers() {
  }

  /**
   * Match the given {@code path} against the given {@code pattern},
   * according to this PathMatcher's matching strategy.
   *
   * @param pattern the pattern to match against
   * @param path    the path to test
   * @return {@code true} if the supplied {@code path} matched,
   * {@code false} if it didn't
   */
  public static boolean match(@Nonnull String pattern, @Nonnull String path) {
    PathPattern pathPattern = parsePattern(pattern);
    PathContainer pathContainer = parsePath(path);
    return pathPattern.matches(pathContainer);
  }

  @Nonnull
  private static PathContainer parsePath(@Nonnull String path) {
    return PATH_CONTAINER_CACHE.get(path, PathContainer::parsePath);
  }

  @Nonnull
  private static PathPattern parsePattern(@Nonnull String pattern) {
    return PATTERNS.computeIfAbsent(pattern, PathPatternParser.defaultInstance::parse);
  }
}
