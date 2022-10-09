package cn.idealframework2.example.trace.block;

import cn.idealframework2.json.JsonUtils;
import cn.idealframework2.trace.Operation;
import cn.idealframework2.trace.Operator;
import cn.idealframework2.trace.block.OperationLogStore;
import cn.idealframework2.trace.block.OperatorHolder;
import cn.idealframework2.transmission.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

/**
 * @author zzsong
 */
@RestController
@SpringBootApplication
public class BlockTraceExampleApplication {
  private static final Logger log = LoggerFactory.getLogger(BlockTraceExampleApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(BlockTraceExampleApplication.class, args);
  }

  /** 操作日志存储库 */
  @Bean
  public OperationLogStore operationLogStore() {
    return operationLog -> log.info("operation log: \n{}", JsonUtils.toPrettyJsonString(operationLog));
  }

  /** 操作人信息持有者 */
  @Bean
  public OperatorHolder operatorHolder() {
    return new OperatorHolder() {
      @Nonnull
      @Override
      public Operator get() {
        Operator operator = new Operator();
        operator.setPlatform("platform");
        operator.setTenantId("100");
        operator.setUserId("10000");
        return operator;
      }
    };
  }

  @Operation("测试1")
  @GetMapping("/trace/1")
  public Result<Void> t1() throws InterruptedException {
    TimeUnit.SECONDS.sleep(1);
    return Result.success();
  }

  @Operation("测试2")
  @GetMapping("/trace/2")
  public Result<Void> t2() throws InterruptedException {
    TimeUnit.SECONDS.sleep(1);
    @SuppressWarnings({"NumericOverflow", "divzero"})
    int a = 1 / 0;
    return Result.success();
  }
}
