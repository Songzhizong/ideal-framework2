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
package cn.idealframework2.cache.serialize;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/7/10
 */
public class StringValueSerializer implements ValueSerializer<String> {
  private static final StringValueSerializer INSTANCE = new StringValueSerializer();

  private StringValueSerializer() {
  }

  @Nonnull
  public static StringValueSerializer instance() {
    return INSTANCE;
  }

  @Nonnull
  @Override
  public String serialize(@Nonnull String value) {
    return value;
  }

  @Nonnull
  @Override
  public String deserialize(@Nonnull String value) {
    return value;
  }
}
