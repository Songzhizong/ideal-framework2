package cn.idealframework2.autoconfigure.web.webflux;

import cn.idealframework2.autoconfigure.web.MessageConverterProperties;
import cn.idealframework2.autoconfigure.web.WebProperties;
import cn.idealframework2.trace.reactive.TraceContextHolder;
import cn.idealframework2.transmission.BasicResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.util.MimeType;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * webflux消息转换器
 *
 * @author 宋志宗 on 2021/7/4
 */
@EnableWebFlux
@ConditionalOnClass({WebFluxConfigurer.class})
public class CustomWebFluxConfigurer implements WebFluxConfigurer {
  private final WebProperties webProperties;

  @Value("${spring.codec.max-in-memory-size:1MB}")
  private DataSize codecMaxInMemorySize;


  public CustomWebFluxConfigurer(WebProperties webProperties) {
    this.webProperties = webProperties;
  }

  @Override
  @SuppressWarnings("DuplicatedCode")
  public void configureHttpMessageCodecs(@Nonnull ServerCodecConfigurer configurer) {
    MessageConverterProperties messageConverter = webProperties.getMessageConverter();
    boolean enableCustom = messageConverter.isEnableCustom();
    if (enableCustom) {
      SimpleModule javaTimeModule = new JavaTimeModule();
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(messageConverter.getDateTimePattern());
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(messageConverter.getDatePattern());
      DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(messageConverter.getTimePattern());
      javaTimeModule
        .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter))
        .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter))
        .addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter))
        .addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter))
        .addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter))
        .addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

      // Long转String传输
      SimpleModule longToStrongModule = new SimpleModule();
      longToStrongModule.addSerializer(Long.class, ToStringSerializer.instance);
      longToStrongModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

      ObjectMapper objectMapper = new ObjectMapper()
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
        .setDateFormat(new SimpleDateFormat(messageConverter.getDateTimePattern()))
        .registerModule(javaTimeModule)
        .registerModule(longToStrongModule)
        .findAndRegisterModules();
      if (messageConverter.isIgnoreNull()) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      }
      configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
      configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
    }
    configurer.defaultCodecs().maxInMemorySize(Math.toIntExact(codecMaxInMemorySize.toBytes()));
  }

  public static class CustomJackson2JsonEncoder extends Jackson2JsonEncoder {

    public CustomJackson2JsonEncoder(ObjectMapper mapper, MimeType... mimeTypes) {
      super(mapper, mimeTypes);
    }

    @Nonnull
    @Override
    public Flux<DataBuffer> encode(@Nonnull Publisher<?> inputStream,
                                   @Nonnull DataBufferFactory bufferFactory,
                                   @Nonnull ResolvableType elementType,
                                   @Nullable MimeType mimeType,
                                   @Nullable Map<String, Object> hints) {
      if (inputStream instanceof Mono) {
        // 处理单一对象类型数据
        inputStream = Mono.from(inputStream)
          .flatMap(body -> {
            if (body instanceof BasicResult) {
              return TraceContextHolder.current()
                .map(opt -> {
                  if (opt.isEmpty()) {
                    return body;
                  }
                  return (BasicResult) body;
                });
            }
            return Mono.just(body);
          });
      }
      return super.encode(inputStream, bufferFactory, elementType, mimeType, hints);
    }
  }
}
