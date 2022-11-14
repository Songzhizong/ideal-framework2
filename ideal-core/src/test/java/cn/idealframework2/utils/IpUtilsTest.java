package cn.idealframework2.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author 宋志宗 on 2022/9/30
 */
public class IpUtilsTest {

  @Test
  public void ipv4ToInt() {
    assertEquals(IpUtils.atypicalIpv4ToInt("0.0.0.0"), Integer.MIN_VALUE);
    assertEquals(IpUtils.atypicalIpv4ToInt("255.255.255.255"), Integer.MAX_VALUE);
  }

  @Test
  public void getLocalAddress() {
    System.out.println(IpUtils.getLocalAddress().getHostAddress());
  }
}
