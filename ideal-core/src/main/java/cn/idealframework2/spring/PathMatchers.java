package cn.idealframework2.spring;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
   * Does the given {@code path} represent a pattern that can be matched
   * by an implementation of this interface?
   * <p>If the return value is {@code false}, then the {@link #match}
   * method does not have to be used because direct equality comparisons
   * on the static path Strings will lead to the same result.
   *
   * @param path the path to check
   * @return {@code true} if the given {@code path} represents a pattern
   */
  public static boolean isPattern(@Nullable String path) {
    if (!mayPattern(path)) {
      return false;
    }
    PathPatternParser.defaultInstance.parse(path);
    return true;
  }

  private static boolean mayPattern(@Nullable String path) {
    if (path == null) {
      return false;
    }
    boolean uriVar = false;
    for (int i = 0; i < path.length(); i++) {
      char c = path.charAt(i);
      if (c == '*' || c == '?') {
        return true;
      }
      if (c == '{') {
        uriVar = true;
        continue;
      }
      if (c == '}' && uriVar) {
        return true;
      }
    }
    return false;
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
