package cn.idealframework2.autoconfigure.event;

import cn.idealframework2.autoconfigure.event.properties.SpringRabbitProperties;
import cn.idealframework2.lang.StringUtils;
import com.rabbitmq.client.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 宋志宗 on 2022/10/9
 */
public class RabbitUtils {
  private static final Logger log = LoggerFactory.getLogger(RabbitUtils.class);


  @Nonnull
  public static Address[] getRabbitAddresses(@Nonnull SpringRabbitProperties springRabbitProperties) {
    String addresses = springRabbitProperties.getAddresses();
    if (StringUtils.isBlank(addresses)) {
      Address address = new Address(springRabbitProperties.getHost(), springRabbitProperties.getPort());
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
    Address address = new Address(springRabbitProperties.getHost(), springRabbitProperties.getPort());
    return new Address[]{address};
  }
}
