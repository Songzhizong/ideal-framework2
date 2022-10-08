package cn.idealframework2.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author 宋志宗 on 2022/9/30
 */
public class StopWatchTest {
  private static final Log log = LogFactory.getLog(StopWatchTest.class);


  @Test
  public void test1() throws InterruptedException {
    StopWatch sw = StopWatch.createStarted("任务一");
    Assert.assertTrue(sw.isRunning());
    TimeUnit.MILLISECONDS.sleep(5);
    sw.stop();
    Assert.assertFalse(sw.isRunning());
    sw.start("任务二");
    Assert.assertTrue(sw.isRunning());
    TimeUnit.MILLISECONDS.sleep(7);
    sw.stop();
    Assert.assertFalse(sw.isRunning());
    StopWatch.TaskInfo[] taskInfo = sw.getTaskInfo();
    Assert.assertEquals(taskInfo.length, 2);
    StopWatch.TaskInfo taskInfo1 = taskInfo[0];
    log.info(taskInfo1.getTaskName() + " 耗时: " + taskInfo1.getTimeMillis() + "ms");
    Assert.assertEquals("任务一", taskInfo1.getTaskName());
    StopWatch.TaskInfo taskInfo2 = taskInfo[1];
    log.info(taskInfo2.getTaskName() + " 耗时: " + taskInfo2.getTimeMillis() + "ms");
    Assert.assertEquals("任务二", taskInfo2.getTaskName());
  }

  @Test
  public void test2() throws InterruptedException {
    StopWatch sw = StopWatch.createStarted();
    TimeUnit.MILLISECONDS.sleep(5);
    long millis = sw.stopAndGetTimeMillis();
    System.out.println(millis);
    Assert.assertFalse(sw.isRunning());
  }
}
