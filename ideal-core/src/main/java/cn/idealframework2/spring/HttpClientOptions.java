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

import reactor.netty.resources.ConnectionProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;

/**
 * @author 宋志宗 on 2021/9/17
 */
@SuppressWarnings("unused")
public class HttpClientOptions {

  @Nonnull
  private String name = "httpClient";

  /** 检测连接有效性的时间间隔 */
  @Nullable
  private Duration evictionInterval;

  /** 最大连接数 */
  private int maxConnections = 512;

  /** 等待队列大小, 默认情况下为最大连接数x2 */
  @Nullable
  private Integer pendingAcquireMaxCount;

  /** 等待连接的超时时间 */
  @Nonnull
  private Duration pendingAcquireTimeout = Duration.ofSeconds(1);

  /** 连接最大空闲时间 */
  private Duration maxIdleTime = Duration.ofSeconds(60);

  /** 连接最大存活时间 */
  private Duration maxLifeTime = Duration.ofMinutes(10);

  /** 获取连接的策略, 默认使用最近创建的 */
  @Nonnull
  private String leasingStrategy = ConnectionProvider.LEASING_STRATEGY_LIFO;

  /** 请求超时时间 */
  @Nullable
  private Duration responseTimeout;

  /** http keep alive */
  private boolean keepAlive = true;

  /** 跟踪重定向 */
  private boolean followRedirect = false;

  /** 是否启用gzip支持 */
  private boolean compressionEnabled = false;

  /** 优先选择最新创建的连接 */
  public HttpClientOptions lifo() {
    leasingStrategy = ConnectionProvider.LEASING_STRATEGY_LIFO;
    return this;
  }

  /** 优先选择最早创建的连接 */
  public HttpClientOptions fifo() {
    leasingStrategy = ConnectionProvider.LEASING_STRATEGY_FIFO;
    return this;
  }

  @Nonnull
  public String getName() {
    return name;
  }

  public HttpClientOptions setName(@Nonnull String name) {
    this.name = name;
    return this;
  }

  @Nullable
  public Duration getEvictionInterval() {
    return evictionInterval;
  }

  public HttpClientOptions setEvictionInterval(@Nullable Duration evictionInterval) {
    this.evictionInterval = evictionInterval;
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

  @Nonnull
  public Duration getPendingAcquireTimeout() {
    return pendingAcquireTimeout;
  }

  public HttpClientOptions setPendingAcquireTimeout(@Nonnull Duration pendingAcquireTimeout) {
    this.pendingAcquireTimeout = pendingAcquireTimeout;
    return this;
  }

  public Duration getMaxIdleTime() {
    return maxIdleTime;
  }

  public HttpClientOptions setMaxIdleTime(Duration maxIdleTime) {
    this.maxIdleTime = maxIdleTime;
    return this;
  }

  public Duration getMaxLifeTime() {
    return maxLifeTime;
  }

  public HttpClientOptions setMaxLifeTime(Duration maxLifeTime) {
    this.maxLifeTime = maxLifeTime;
    return this;
  }

  @Nonnull
  public String getLeasingStrategy() {
    return leasingStrategy;
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
