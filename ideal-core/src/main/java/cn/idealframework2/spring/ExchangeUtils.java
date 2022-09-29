package cn.idealframework2.spring;

import cn.idealframework2.lang.StringUtils;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * @author 宋志宗 on 2021/9/18
 */
public final class ExchangeUtils {
  static final String TRACE_CONTEXT_ATTRIBUTE_NAME = "ideal.cloud.traceContext";
  static final String LOG_PREFIX_ATTRIBUTE_NAME = "ideal.cloud.traceLogPrefix";
  private static final String UNKNOWN = "UNKNOWN";

  private ExchangeUtils() {
  }

  @Nonnull
  public static Mono<Void> writeResponse(@Nonnull ServerWebExchange exchange,
                                         @Nonnull HttpStatus httpStatus,
                                         @Nullable HttpHeaders httpHeaders,
                                         @Nonnull byte[] body) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(httpStatus);
    if (httpHeaders != null && !httpHeaders.isEmpty()) {
      response.getHeaders().putAll(httpHeaders);
    }
    DefaultDataBuffer buffer = DefaultDataBufferFactory.sharedInstance.wrap(body);
    return response.writeWith(Mono.just(buffer));
  }

  public static void clean(@Nonnull ServerWebExchange exchange) {
    Map<String, Object> attributes = exchange.getAttributes();
    attributes.remove(TRACE_CONTEXT_ATTRIBUTE_NAME);
    attributes.remove(LOG_PREFIX_ATTRIBUTE_NAME);
  }

  /**
   * 获取客户端的原始ip地址
   *
   * @return 客户端的原始ip地址
   * @author 宋志宗 on 2022/5/6
   */
  @Nonnull
  public static String getRemoteAddress(@Nonnull ServerWebExchange exchange) {
    ServerHttpRequest request = exchange.getRequest();
    HttpHeaders headers = request.getHeaders();
    String ip = headers.getFirst("X-Forwarded-For");
    if (StringUtils.isNotBlank(ip)) {
      int index = ip.indexOf(',');
      if (index > -1) {
        String substring = ip.substring(0, index);
        if (!StringUtils.equalsIgnoreCase(substring, UNKNOWN)) {
          return substring;
        }
      } else {
        return ip;
      }
    }
    InetSocketAddress remoteAddress = request.getRemoteAddress();
    if (remoteAddress == null) {
      return UNKNOWN;
    }
    String hostName = remoteAddress.getHostName();
    if (StringUtils.isNotBlank(hostName)) {
      return hostName;
    }
    return UNKNOWN;
  }

  @Nullable
  public static String getUserAgent(@Nonnull ServerWebExchange exchange) {
    HttpHeaders headers = exchange.getRequest().getHeaders();
    return headers.getFirst(HttpHeaders.USER_AGENT);
  }
}
