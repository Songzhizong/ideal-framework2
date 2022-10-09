package cn.idealframework2.utils;

import cn.idealframework2.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author 宋志宗 on 2022/7/14
 */
@SuppressWarnings("unused")
public class PropertiesReader {
  private static final Logger log = LoggerFactory.getLogger(PropertiesReader.class);
  private static final ConcurrentMap<String, PropertiesReader> RESOURCE_READER_MAP = new ConcurrentHashMap<>();
  private final Map<String, String> propertyMap = new HashMap<>();

  @Nonnull
  public static PropertiesReader ofResourceFile(@Nonnull String filename) {
    return RESOURCE_READER_MAP.computeIfAbsent(filename, k -> {
      InputStream inputStream = Thread.currentThread()
          .getContextClassLoader().getResourceAsStream(filename);
      if (inputStream == null) {
        String message = "找不到资源目录下的配置文件: " + filename;
        log.error(message);
        throw new RuntimeException(message);
      }
      return createReader(inputStream);
    });
  }

  @Nonnull
  private static PropertiesReader createReader(InputStream inputStream) {
    PropertiesReader reader = new PropertiesReader();
    try (InputStream is = inputStream;
         InputStreamReader isr = new InputStreamReader(is);
         BufferedReader br = new BufferedReader(isr)) {
      br.lines().forEach(line -> {
        if (StringUtils.isBlank(line)) {
          return;
        }
        String trim = line.trim();
        //noinspection AlibabaUndefineMagicConstant
        if (trim.startsWith("#")) {
          return;
        }
        String[] split = StringUtils.split(trim, "=", 2);
        //noinspection AlibabaUndefineMagicConstant
        if (split.length < 2) {
          return;
        }
        reader.propertyMap.put(split[0], split[1]);
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return reader;
  }

  @Nullable
  public String getString(@Nonnull String key) {
    return propertyMap.get(key);
  }

  @Nonnull
  public String getStringOrThrow(@Nonnull String key) {
    String string = getString(key);
    if (StringUtils.isBlank(string)) {
      throw new RuntimeException("属性配置为空: " + key);
    }
    return string;
  }

  @Nullable
  public String getStringOrDefault(@Nonnull String key, @Nonnull String defaultValue) {
    String value = getString(key);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  @Nullable
  public Integer getInt(@Nonnull String key) {
    String value = propertyMap.get(key);
    if (StringUtils.isBlank(value)) {
      return null;
    }
    return Integer.valueOf(value);
  }


  @SuppressWarnings({"AlibabaMethodReturnWrapperType"})
  public int getIntOrDefault(@Nonnull String key, int defaultValue) {
    Integer integer = getInt(key);
    if (integer == null) {
      return defaultValue;
    }
    return integer;
  }

  @Nullable
  public Long getLong(@Nonnull String key) {
    String value = propertyMap.get(key);
    if (StringUtils.isBlank(value)) {
      return null;
    }
    return Long.valueOf(value);
  }


  @SuppressWarnings({"AlibabaMethodReturnWrapperType"})
  public long getLongOrDefault(@Nonnull String key, int defaultValue) {
    Long value = getLong(key);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
