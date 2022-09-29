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

import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2021/9/17
 */
public class WebClientOptions {
  @Nullable
  private DefaultUriBuilderFactory.EncodingMode encodingMode;

  @Nullable
  private Integer maxInMemorySize;

  @Nullable
  public DefaultUriBuilderFactory.EncodingMode getEncodingMode() {
    return encodingMode;
  }

  public WebClientOptions setEncodingMode(@Nullable DefaultUriBuilderFactory.EncodingMode encodingMode) {
    this.encodingMode = encodingMode;
    return this;
  }

  @Nullable
  public Integer getMaxInMemorySize() {
    return maxInMemorySize;
  }

  public WebClientOptions setMaxInMemorySize(@Nullable Integer maxInMemorySize) {
    this.maxInMemorySize = maxInMemorySize;
    return this;
  }
}
