package cn.idealframework2.example.trace.coroutine

import cn.idealframework2.json.JsonUtils
import cn.idealframework2.trace.Operation
import cn.idealframework2.trace.Operator
import cn.idealframework2.trace.reactive.OperationLogStore
import cn.idealframework2.trace.reactive.OperatorHolder
import cn.idealframework2.transmission.Result
import kotlinx.coroutines.delay
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import kotlin.time.Duration.Companion.seconds

/**
 * @author zzsong
 */
@RestController
@SpringBootApplication
class CoroutineTraceExampleApplication {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(CoroutineTraceExampleApplication::class.java)
  }

  @Bean
  fun operatorHolder(): OperatorHolder {
    return OperatorHolder {
      val operator = Operator()
      operator.platform = "platform"
      operator.tenantId = "100"
      operator.userId = "10000"
      Mono.just(operator)
    }
  }

  @Bean
  fun operationLogStore(): OperationLogStore {
    return OperationLogStore {
      log.info("operation log: \n{}", JsonUtils.toPrettyJsonString(it))
      Mono.just(true)
    }
  }

  @Operation("测试1")
  @GetMapping("/trace/1")
  suspend fun t1(): Result<Void> {
    return Result.success()
  }

  @Operation("测试2")
  @GetMapping("/trace/2")
  suspend fun t2(): Result<Void> {
    delay(1.seconds)
    @Suppress("DIVISION_BY_ZERO")
    val a = 1 / 0
    return Result.success()
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(CoroutineTraceExampleApplication::class.java, *args)
}
