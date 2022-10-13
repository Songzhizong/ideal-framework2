package cn.idealframework2.spring;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.util.pattern.PatternParseException;

/**
 * @author 宋志宗 on 2022/10/13
 */
public class PathMatchersTest {

  @Test
  public void match() {
    Assert.assertThrows(PatternParseException.class, () -> PathMatchers.match("/{*t}/test", "/a/b/test"));
    Assert.assertThrows(PatternParseException.class, () -> PathMatchers.match("/**/test", "/a/b/test"));
    Assert.assertTrue(PathMatchers.match("/*/test", "/a/test"));
    Assert.assertTrue(PathMatchers.match("/test/**", "/test/a"));
    Assert.assertTrue(PathMatchers.match("/test/**", "/test/a/b"));
    Assert.assertTrue(PathMatchers.match("/{path}/test/**", "/t1/test/a/b"));
  }
}
