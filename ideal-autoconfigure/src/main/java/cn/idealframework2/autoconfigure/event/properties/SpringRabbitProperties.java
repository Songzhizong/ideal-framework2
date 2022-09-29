package cn.idealframework2.autoconfigure.event.properties;

import cn.idealframework2.lang.StringUtils;
import com.rabbitmq.client.Address;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 宋志宗 on 2022/8/13
 */
@ConfigurationProperties("spring.rabbitmq")
public class SpringRabbitProperties {
  private static final Log log = LogFactory.getLog(SpringRabbitProperties.class);

  /**
   * RabbitMQ host. Ignored if an address is set.
   */
  private String host = "localhost";

  /**
   * RabbitMQ port. Ignored if an address is set. Default to 5672, or 5671 if SSL is
   * enabled.
   */
  private int port = 5672;


  /**
   * Comma-separated list of addresses to which the client should connect. When set, the
   * host and port are ignored.
   */
  @Nonnull
  private String addresses = "";

  /**
   * Login user to authenticate to the broker.
   */
  private String username = "guest";

  /**
   * Login to authenticate against the broker.
   */
  private String password = "guest";

  /**
   * Virtual host to use when connecting to the broker.
   */
  private String virtualHost = "/";

  @Nonnull
  public Address[] getRabbitAddresses() {
    String addresses = getAddresses();
    if (StringUtils.isBlank(addresses)) {
      Address address = new Address(getHost(), getPort());
      return new Address[]{address};
    }
    List<Address> addressList = new ArrayList<>();
    String[] addressArray = StringUtils.split(addresses, ",");
    for (String address : addressArray) {
      if (StringUtils.isBlank(address)) {
        continue;
      }
      String[] hostPort = StringUtils.split(address, ":");
      if (hostPort.length != 2) {
        String msg = "无效的addresses配置: " + addresses;
        log.error(msg);
        throw new IllegalArgumentException(msg);
      }
      String host = hostPort[0].trim();
      String portStr = hostPort[1].trim();
      int port = Integer.parseInt(portStr);
      addressList.add(new Address(host, port));
    }
    if (addressList.size() > 0) {
      return addressList.toArray(new Address[]{});
    }
    Address address = new Address(getHost(), getPort());
    return new Address[]{address};
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  @Nonnull
  public String getAddresses() {
    return addresses;
  }

  public void setAddresses(@Nonnull String addresses) {
    this.addresses = addresses;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getVirtualHost() {
    return virtualHost;
  }

  public void setVirtualHost(String virtualHost) {
    this.virtualHost = virtualHost;
  }
}
