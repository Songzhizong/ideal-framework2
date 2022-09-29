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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;

/**
 * @author 宋志宗 on 2021/9/17
 */
public class HttpClientOptions {

  @Nonnull
  private String name = "httpClient";

  /** 最大连接数 */
  private int maxConnections = 512;

  /** 等待队列大小, 默认情况下为最大连接数x2 */
  @Nullable
  private Integer pendingAcquireMaxCount;

  /** 请求超时时间 */
  @Nullable
  private Duration responseTimeout;

  /** http keep alive */
  private boolean keepAlive = true;

  /** 跟踪重定向 */
  private boolean followRedirect = false;

  /** 是否启用gzip支持 */
  private boolean compressionEnabled = false;

  @Nonnull
  public String getName() {
    return name;
  }

  public HttpClientOptions setName(@Nonnull String name) {
    this.name = name;
    return this;
  }

  public int getMaxConnections() {
    return maxConnections;
  }

  public HttpClientOptions setMaxConnections(int maxConnections) {
    this.maxConnections = maxConnections;
    return this;
  }

  @Nullable
  public Integer getPendingAcquireMaxCount() {
    return pendingAcquireMaxCount;
  }

  public HttpClientOptions setPendingAcquireMaxCount(@Nullable Integer pendingAcquireMaxCount) {
    this.pendingAcquireMaxCount = pendingAcquireMaxCount;
    return this;
  }

  @Nullable
  public Duration getResponseTimeout() {
    return responseTimeout;
  }

  public HttpClientOptions setResponseTimeout(@Nullable Duration responseTimeout) {
    this.responseTimeout = responseTimeout;
    return this;
  }

  public boolean isKeepAlive() {
    return keepAlive;
  }

  public HttpClientOptions setKeepAlive(boolean keepAlive) {
    this.keepAlive = keepAlive;
    return this;
  }

  public boolean isFollowRedirect() {
    return followRedirect;
  }

  public HttpClientOptions setFollowRedirect(boolean followRedirect) {
    this.followRedirect = followRedirect;
    return this;
  }

  public boolean isCompressionEnabled() {
    return compressionEnabled;
  }

  public HttpClientOptions setCompressionEnabled(boolean compressionEnabled) {
    this.compressionEnabled = compressionEnabled;
    return this;
  }
}
