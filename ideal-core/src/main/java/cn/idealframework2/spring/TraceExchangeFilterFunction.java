package cn.idealframework2.spring;

import cn.idealframework2.trace.TraceContext;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author 宋志宗 on 2022/9/22
 */
public enum TraceExchangeFilterFunction implements ExchangeFilterFunction {
  /** 实例 */
  INSTANCE,
  ;

  @Nonnull
  public static ExchangeFilterFunction getInstance() {
    return INSTANCE;
  }

  @Nonnull
  @Override
  public Mono<ClientResponse> filter(@Nonnull ClientRequest request,
                                     @Nonnull ExchangeFunction next) {
    return cn.idealframework2.trace.reactive.TraceContextHolder.current()
      .flatMap(op -> {
        if (op.isPresent()) {
          TraceContext context = op.get();
          ClientRequest clientRequest = ClientRequest.from(request)
            .headers(headers -> WebClientTraceUtils.setTraceHeaders(headers, context))
            .build();
          return next.exchange(clientRequest);
        }
        Optional<TraceContext> current = cn.idealframework2.trace.block.TraceContextHolder.current();
        if (current.isPresent()) {
          TraceContext context = current.get();
          ClientRequest clientRequest = ClientRequest.from(request)
            .headers(headers -> WebClientTraceUtils.setTraceHeaders(headers, context))
            .build();
          return next.exchange(clientRequest);
        }
        return next.exchange(request);
      });
  }
}
