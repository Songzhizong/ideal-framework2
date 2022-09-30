package cn.idealframework2.utils;

import cn.idealframework2.lang.StringUtils;

import javax.annotation.Nonnull;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author 宋志宗 on 2022/9/20
 */
public final class IpUtils {
  private static final String IPV4_REGEX = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";
  private static final Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);
  private static final Pattern IPV4_RANGE_PATTERN = Pattern.compile(IPV4_REGEX + "-" + IPV4_REGEX);

  private IpUtils() {
  }

  /**
   * ip地址转int数字, 0.0.0.0 ~ 255.255.255.255 => Integer.MIN_VALUE ~ Integer.MAX_VALUE
   *
   * @param address ipv4地址值
   * @return 数字表现形式
   */
  public static int ipv4ToInt(@Nonnull String address) {
    if (!isIpv4(address)) {
      throw new IllegalArgumentException("非法的ipv4地址: " + address);
    }

    String[] split = StringUtils.split(address, ".");
    long r = Long.parseLong(split[0]);
    r = r << 8 | Long.parseLong(split[1]);
    r = r << 8 | Long.parseLong(split[2]);
    r = r << 8 | Long.parseLong(split[3]);
    return Math.toIntExact(r + Integer.MIN_VALUE);
  }

  public static boolean isIpv4(@Nonnull String address) {
    return IPV4_PATTERN.matcher(address).matches();
  }

  /**
   * 判断ip地址是否可ping通
   *
   * @param ip ip地址
   * @return 是否可达
   * @author 宋志宗 on 2021/12/10
   */
  public static boolean reachable(@Nonnull String ip) {
    return reachable(ip, 1000);
  }

  /**
   * 判断ip地址是否可ping通
   *
   * @param ip           ip地址
   * @param timeoutMills 超时时间
   * @return 是否可达
   * @author 宋志宗 on 2021/12/10
   */
  public static boolean reachable(@Nonnull String ip, int timeoutMills) {
    try {
      InetAddress address = Inet4Address.getByName(ip);
      return address.isReachable(timeoutMills);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 获取ip端内所有ip地址
   *
   * @param startIp 起始ip
   * @param endIp   结束ip
   * @param mask    子网掩码
   * @return IP段内所有IP地址
   * @author 宋志宗 on 2022/5/25
   */
  @Nonnull
  public static List<String> getIpRange(@Nonnull String startIp,
                                        @Nonnull String endIp,
                                        @Nonnull String mask) {
    List<String> result = new ArrayList<>();
    SubnetUtils utils = new SubnetUtils(startIp, mask);
    SubnetUtils.SubnetInfo info = utils.getInfo();
    String[] allIps = info.getAllAddresses();
    for (String allIp : allIps) {
      if (IpUtils.ipInRange(allIp, startIp, endIp)) {
        result.add(allIp);
      }
    }
    return result;
  }

  /**
   * 判断ip地址是否在ip段之内
   *
   * @param ip      ip地址
   * @param startIp 起始ip
   * @param endIp   结束ip
   * @return 是否在ip段内
   * @author 宋志宗 on 2022/5/25
   */
  private static boolean ipInRange(@Nonnull String ip,
                                   @Nonnull String startIp,
                                   @Nonnull String endIp) {
    int match = ipv4ToInt(ip);
    int start = ipv4ToInt(startIp);
    int end = ipv4ToInt(endIp);
    if (start < end) {
      return start <= match && match <= end;
    }
    return end <= match && match <= start;
  }
}
