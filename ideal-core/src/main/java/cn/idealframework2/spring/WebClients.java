/*
 * Copyright 2021 cn.idealframework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.idealframework2.spring;

import cn.idealframework2.exception.ResultException;
import cn.idealframework2.exception.VisibleException;
import cn.idealframework2.json.JsonUtils;
import cn.idealframework2.transmission.BasicResult;
import cn.idealframework2.transmission.ListResult;
import cn.idealframework2.transmission.PageResult;
import cn.idealframework2.transmission.Result;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * reactor http相关
 * <pre>
 *   - 整个程序中应尽量共用同一个 http client
 * </pre>
 *
 * @author 宋志宗 on 2021/9/17
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public final class WebClients {
  private static final Log log = LogFactory.getLog(WebClients.class);

  @Nonnull
  public static WebClient webClient() {
    return webClient(new FullWebClientOptions());
  }

  @Nonnull
  public static WebClient webClient(@Nonnull Consumer<FullWebClientOptions> consumer) {
    FullWebClientOptions options = new FullWebClientOptions();
    consumer.accept(options);
    return webClient(options);
  }

  @Nonnull
  public static WebClient webClient(@Nonnull FullWebClientOptions options) {
    return webClientBuilder(options).build();
  }

  @Nonnull
  public static WebClient.Builder webClientBuilder(@Nonnull Consumer<FullWebClientOptions> consumer) {
    FullWebClientOptions options = new FullWebClientOptions();
    consumer.accept(options);
    return webClientBuilder(options);
  }

  @Nonnull
  public static WebClient.Builder webClientBuilder() {
    return webClientBuilder(new FullWebClientOptions());
  }

  @Nonnull
  public static WebClient.Builder webClientBuilder(@Nonnull FullWebClientOptions options) {
    HttpClient httpClient = httpClient(options.toHttpClientOptions());
    return webClientBuilderOfHttpClient(httpClient, options.toWebClientOptions());
  }

  @Nonnull
  public static WebClient.Builder webClientBuilderOfHttpClient(@Nonnull HttpClient httpClient) {
    return webClientBuilderOfHttpClient(httpClient, (WebClientOptions) null);
  }

  @Nonnull
  public static WebClient.Builder webClientBuilderOfHttpClient(@Nonnull HttpClient httpClient,
                                                               @Nonnull Consumer<WebClientOptions> consumer) {
    WebClientOptions options = new WebClientOptions();
    consumer.accept(options);
    return webClientBuilderOfHttpClient(httpClient, options);
  }

  @Nonnull
  public static WebClient.Builder webClientBuilderOfHttpClient(@Nonnull HttpClient httpClient,
                                                               @Nullable WebClientOptions options) {
    WebClient.Builder builder = WebClient.builder()
      .clientConnector(new ReactorClientHttpConnector(httpClient))
      .filter(TraceExchangeFilterFunction.getInstance());
    if (options != null) {
      // URI 编码方式
      DefaultUriBuilderFactory.EncodingMode encodingMode = options.getEncodingMode();
      if (encodingMode != null) {
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
        uriBuilderFactory.setEncodingMode(encodingMode);
        builder.uriBuilderFactory(uriBuilderFactory);
      }
      // 最大内存占用
      Integer maxInMemorySize = options.getMaxInMemorySize();
      if (maxInMemorySize != null) {
        builder.codecs(c -> c.defaultCodecs().maxInMemorySize(maxInMemorySize));
      }
    }
    return builder;
  }

  @Nonnull
  public static HttpClient httpClient() {
    return httpClient(new HttpClientOptions());
  }

  @Nonnull
  public static HttpClient httpClient(@Nonnull Consumer<HttpClientOptions> consumer) {
    HttpClientOptions options = new HttpClientOptions();
    consumer.accept(options);
    return httpClient(options);
  }

  @Nonnull
  public static HttpClient httpClient(@Nonnull HttpClientOptions options) {
    ConnectionProvider.Builder builder = ConnectionProvider
      .builder(options.getName())
      .maxIdleTime(options.getMaxIdleTime())
      .maxLifeTime(options.getMaxLifeTime())
      .maxConnections(options.getMaxConnections())
      .pendingAcquireTimeout(options.getPendingAcquireTimeout());
    String leasingStrategy = options.getLeasingStrategy();
    if (ConnectionProvider.LEASING_STRATEGY_FIFO.equalsIgnoreCase(leasingStrategy)) {
      builder.fifo();
    } else {
      builder.lifo();
    }
    Integer pendingAcquireMaxCount = options.getPendingAcquireMaxCount();
    if (pendingAcquireMaxCount != null && pendingAcquireMaxCount > 0) {
      builder.pendingAcquireMaxCount(pendingAcquireMaxCount);
    }
    Duration evictionInterval = options.getEvictionInterval();
    if (evictionInterval != null) {
      builder.evictInBackground(evictionInterval);
    }
    ConnectionProvider connectionProvider = builder.build();
    HttpClient httpClient = HttpClient.create(connectionProvider)
      .keepAlive(options.isKeepAlive())
      .followRedirect(options.isFollowRedirect())
      .compress(options.isCompressionEnabled());
    Duration responseTimeout = options.getResponseTimeout();
    if (responseTimeout != null && !responseTimeout.isZero()) {
      httpClient.responseTimeout(responseTimeout);
    }
    return httpClient;
  }


  @Nonnull
  public static <T> Function<ClientResponse, Mono<Result<T>>> result(@Nonnull Class<T> clazz) {
    return resp -> {
      HttpStatusCode statusCode = resp.statusCode();
      int status = statusCode.value();
      return resp.bodyToMono(String.class)
        .defaultIfEmpty("")
        .flatMap(result -> {
          log.debug("响应结果: " + result);
          Result<T> parse = JsonUtils.parse(result, Result.class, clazz);
          if (parse.getSuccess() == null) {
            ResultException exception = new ResultException(status, null, result);
            return Mono.error(exception);
          }
          if (statusCode.isError() || parse.isFailed()) {
            ResultException exception = new ResultException(status, parse.getCode(), parse.getMessage());
            return Mono.error(exception);
          }
          return Mono.just(parse);
        })
        .onErrorResume(throwable -> {
          if (throwable instanceof VisibleException exception) {
            return Mono.error(exception);
          }
          String message = throwable.getMessage();
          ResultException exception = new ResultException(500, null, message);
          return Mono.error(exception);
        });
    };
  }


  @Nonnull
  public static <T extends BasicResult> Function<ClientResponse, Mono<T>> result(@Nonnull TypeReference<T> reference) {
    return resp -> {
      HttpStatusCode statusCode = resp.statusCode();
      int status = statusCode.value();
      return resp.bodyToMono(String.class)
        .defaultIfEmpty("")
        .flatMap(result -> {
          log.debug("响应结果: " + result);
          T parse = JsonUtils.parse(result, reference);
          if (parse.getSuccess() == null) {
            ResultException exception = new ResultException(status, null, result);
            return Mono.error(exception);
          }
          if (statusCode.isError() || parse.isFailed()) {
            ResultException exception = new ResultException(status, parse.getCode(), parse.getMessage());
            return Mono.error(exception);
          }
          return Mono.just(parse);
        })
        .onErrorResume(throwable -> {
          if (throwable instanceof VisibleException exception) {
            return Mono.error(exception);
          }
          String message = throwable.getMessage();
          ResultException exception = new ResultException(500, null, message);
          return Mono.error(exception);
        });
    };
  }


  @Nonnull
  public static <T> Function<ClientResponse, Mono<ListResult<T>>> listResult(@Nonnull Class<T> clazz) {
    return resp -> {
      HttpStatusCode statusCode = resp.statusCode();
      int status = statusCode.value();
      return resp.bodyToMono(String.class)
        .defaultIfEmpty("")
        .flatMap(result -> {
          log.debug("响应结果: " + result);
          ListResult<T> parse = JsonUtils.parse(result, ListResult.class, clazz);
          if (parse.getSuccess() == null) {
            ResultException exception = new ResultException(status, null, result);
            return Mono.error(exception);
          }
          if (statusCode.isError() || parse.isFailed()) {
            ResultException exception = new ResultException(status, parse.getCode(), parse.getMessage());
            return Mono.error(exception);
          }
          return Mono.just(parse);
        })
        .onErrorResume(throwable -> {
          if (throwable instanceof VisibleException exception) {
            return Mono.error(exception);
          }
          String message = throwable.getMessage();
          ResultException exception = new ResultException(500, null, message);
          return Mono.error(exception);
        });
    };
  }


  @Nonnull
  public static <E> Function<ClientResponse, Mono<PageResult<E>>> pageResult(@Nonnull Class<E> clazz) {
    return resp -> {
      HttpStatusCode statusCode = resp.statusCode();
      int status = statusCode.value();
      return resp.bodyToMono(String.class)
        .defaultIfEmpty("")
        .flatMap(result -> {
          log.debug("响应结果: " + result);
          PageResult<E> parse = JsonUtils.parse(result, PageResult.class, clazz);
          if (parse.getSuccess() == null) {
            ResultException exception = new ResultException(status, null, result);
            return Mono.error(exception);
          }
          if (statusCode.isError() || parse.isFailed()) {
            ResultException exception = new ResultException(status, parse.getCode(), parse.getMessage());
            return Mono.error(exception);
          }
          return Mono.just(parse);
        })
        .onErrorResume(throwable -> {
          if (throwable instanceof VisibleException exception) {
            return Mono.error(exception);
          }
          String message = throwable.getMessage();
          ResultException exception = new ResultException(500, null, message);
          return Mono.error(exception);
        });
    };
  }

}
