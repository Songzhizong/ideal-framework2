package cn.idealframework2.utils;

import cn.idealframework2.json.JsonUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author 宋志宗 on 2022/10/11
 */
public class SensitiveWatchTest {
  private static final String TEXT = "太多的伤yuming感情怀也许只局限于饲养基地 荧幕中的情节，" +
    "主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。" +
    "然后法轮功 我们的扮演的角色就是跟随着主人yum公的喜红客联盟 怒于饲养基地 荧幕中的情节，" +
    "主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。" +
    "然后法轮功 我们的扮演的角色就是跟随着主人yum公的喜红客联盟 怒哀20于饲养基地 荧幕中的情节，" +
    "主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。" +
    "然后法轮功 我们的扮演的角色就是跟随着主人yum公的喜红客联盟 " +
    "怒哀20哀2015/4/16 20152015/4/16乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，" +
    "关, 人, 流, 电, 发, 情, 太, 限, 法轮功, 个人, 经, 色, 许, 公, 动, 地, 方, 基, 在, 上, 红, 强," +
    " 自杀指南, 制, 卡, 三级片, 一, 夜, 多, 手机, 于, 自，" +
    "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三级片 深人静的晚上，" +
    "关上电话静静的发呆着。";

  @Test
  public void filter() {
    SensitiveWordFilter filter = SensitiveWordFilter.getDefaultInstance();
    SensitiveWordFilter.Task task = filter.watch(TEXT);
    String desensitize = task.getDesensitize();
    System.out.println("脱敏后的文本: " + desensitize);
    Set<String> sensitiveWords = task.getSensitiveWords();
    System.out.println("原文本中出现过的敏感词: " + JsonUtils.toJsonString(sensitiveWords));
    Map<String, Integer> wordFrequency = task.getSensitiveWordFrequency();
    System.out.println("原文本中各敏感词出现的次数: " + JsonUtils.toJsonString(wordFrequency));
  }

  @Test
  @Ignore
  public void benchmark() throws InterruptedException {
    int loop = 100000;
    int i = Runtime.getRuntime().availableProcessors();
    CountDownLatch countDownLatch = new CountDownLatch(i);
    Runnable runnable = () -> {
      for (int i12 = 0; i12 < loop; i12++) {
        SensitiveWordFilter filter = SensitiveWordFilter.getDefaultInstance();
        filter.watch(TEXT);
      }
      countDownLatch.countDown();
    };
    StopWatch stopWatch = StopWatch.createStarted();
    for (int i1 = 0; i1 < i; i1++) {
      new Thread(runnable).start();
    }
    countDownLatch.await();
    long timeMillis = stopWatch.stopAndGetTimeMillis();
    System.out.println(timeMillis);
  }
}
