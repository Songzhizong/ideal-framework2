package cn.idealframework2.utils;

import cn.idealframework2.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 敏感词过滤器
 *
 * @author 宋志宗 on 2022/10/11
 */
public class SensitiveWordFilter {
  private static final Logger log = LoggerFactory.getLogger(SensitiveWordFilter.class);
  private static final SensitiveWordFilter DEFAULT_INSTANCE = createFromResource("sensitive_words.txt");
  @Nonnull
  private final Set<String> sensitiveWords;

  private SensitiveWordFilter(@Nonnull Set<String> sensitiveWords) {
    this.sensitiveWords = sensitiveWords;
  }

  @Nonnull
  public static SensitiveWordFilter getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  /**
   * 根据项目资源目录下的敏感词库名称初始化, 初始化敏感词库操作通常只需要在程序启动时执行一次就可以了
   *
   * @param fileName 项目资源目录下的敏感词库文件名称
   */
  @Nonnull
  public static SensitiveWordFilter createFromResource(@Nonnull String fileName) {
    InputStream resourceAsStream = Thread.currentThread()
      .getContextClassLoader().getResourceAsStream(fileName);
    if (resourceAsStream == null) {
      log.error("敏感词库文件不存在: {}", fileName);
      throw new RuntimeException("敏感词库文件不存在");
    }
    try (resourceAsStream) {
      return createFromInputStream(resourceAsStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 通过铭感词库文件初始化, 初始化敏感词库操作通常只需要在程序启动时执行一次就可以了
   *
   * @param file 敏感词库文件
   */
  @Nonnull
  public static SensitiveWordFilter createFromFile(@Nonnull File file) {
    if (!file.exists()) {
      log.error("传入的敏感词库文件不存在");
      throw new RuntimeException("敏感词库文件不存在");
    }
    if (!file.isFile()) {
      log.error("传入的敏感词库非文件类型");
      throw new RuntimeException("传入的敏感词库非文件类型");
    }
    try (FileInputStream fileInputStream = new FileInputStream(file)) {
      return createFromInputStream(fileInputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 通过敏感词库文件输入流初始化, 初始化敏感词库操作通常只需要在程序启动时执行一次就可以了
   *
   * @param inputStream 敏感词库文件输入流
   */
  @Nonnull
  public static SensitiveWordFilter createFromInputStream(@Nonnull InputStream inputStream) {
    try (InputStreamReader isr = new InputStreamReader(inputStream);
         BufferedReader br = new BufferedReader(isr)) {
      Set<String> set = br.lines().map(String::trim)
        .filter(StringUtils::isNotBlank).collect(Collectors.toUnmodifiableSet());
      return new SensitiveWordFilter(set);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Nonnull
  public Task watch(@Nonnull String text) {
    String replaceStr = "*";
    Set<String> sensitiveWordSet = new HashSet<>(16);
    Map<String, Integer> sensitiveWordFrequency = new HashMap<>(16);
    StringBuilder desensitizeBuffer = new StringBuilder(text);
    HashMap<Integer, Integer> hash = new HashMap<>(sensitiveWords.size());
    String temp;
    for (String s : sensitiveWords) {
      temp = s;
      int findIndexSize = 0;
      for (int start; (start = desensitizeBuffer.indexOf(temp, findIndexSize)) > -1; ) {
        //从已找到的后面开始找
        findIndexSize = start + temp.length();
        //起始位置
        Integer mapStart = hash.get(start);
        //满足1个，即可更新map
        if (mapStart == null || findIndexSize > mapStart) {
          hash.put(start, findIndexSize);
        }
      }
    }
    hash.forEach((startIndex, endIndex) -> {
      //获取敏感词，并加入列表，用来统计数量
      String sensitive = desensitizeBuffer.substring(startIndex, endIndex);
      //添加敏感词到集合
      if (!sensitive.contains(replaceStr)) {
        sensitiveWordSet.add(sensitive);
        int frequency = sensitiveWordFrequency.computeIfAbsent(sensitive, k -> 0);
        sensitiveWordFrequency.put(sensitive, frequency + 1);
      }
      int count = endIndex - startIndex;
      String replace = replaceStr.repeat(Math.max(0, count));
      desensitizeBuffer.replace(startIndex, endIndex, replace);
    });
    String desensitize = desensitizeBuffer.toString();
    return new Task(text, desensitize, sensitiveWordSet, sensitiveWordFrequency);
  }

  public static class Task {
    @Nonnull
    private final String original;
    @Nonnull
    private final String desensitize;
    @Nonnull
    private final Set<String> sensitiveWords;
    @Nonnull
    private final Map<String, Integer> sensitiveWordFrequency;

    public Task(@Nonnull String original,
                @Nonnull String desensitize,
                @Nonnull Set<String> sensitiveWords,
                @Nonnull Map<String, Integer> sensitiveWordFrequency) {
      this.original = original;
      this.desensitize = desensitize;
      this.sensitiveWords = sensitiveWords;
      this.sensitiveWordFrequency = sensitiveWordFrequency;
    }

    @Nonnull
    public String getOriginal() {
      return original;
    }

    @Nonnull
    public String getDesensitize() {
      return desensitize;
    }

    @Nonnull
    public Set<String> getSensitiveWords() {
      return sensitiveWords;
    }

    @Nonnull
    public Map<String, Integer> getSensitiveWordFrequency() {
      return sensitiveWordFrequency;
    }
  }
}
