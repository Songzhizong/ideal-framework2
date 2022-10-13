package cn.idealframework2.example.trace.coroutine

import cn.idealframework2.json.JsonUtils
import cn.idealframework2.trace.Operation
import cn.idealframework2.trace.Operator
import cn.idealframework2.trace.coroutine.TraceContextHolder
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
import java.time.LocalDateTime
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
    return OperationLogStore { operationLog ->
      cn.idealframework2.trace.reactive.TraceContextHolder.current().map {
        var logPrefix = ""
        if (it.isPresent) {
          logPrefix = it.get().logPrefix
        }
        log.info("{}operation log: {}", logPrefix, JsonUtils.toJsonString(operationLog))
        true
      }
    }
  }

  @Operation("测试1")
  @GetMapping("/trace/1")
  suspend fun t1(): Result<LocalDateTime> {
    delay(1.seconds)
    val logPrefix = TraceContextHolder.awaitLogPrefix()
    log.info("{}测试1", logPrefix)
    return Result.success(LocalDateTime.now())
  }

  @Operation("测试2")
  @GetMapping("/trace/2")
  suspend fun t2(): Result<LocalDateTime> {
    delay(1.seconds)
    val logPrefix = TraceContextHolder.awaitLogPrefix()
    log.info("{}测试2", logPrefix)
    @Suppress("DIVISION_BY_ZERO")
    val a = 1 / 0
    return Result.success(LocalDateTime.now())
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(CoroutineTraceExampleApplication::class.java, *args)
}
