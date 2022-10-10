package cn.idealframework2.autoconfigure.web.webmvc;

import cn.idealframework2.autoconfigure.web.MessageConverterProperties;
import cn.idealframework2.autoconfigure.web.WebProperties;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author 宋志宗 on 2022/10/10
 */
@ConditionalOnClass({WebMvcConfigurer.class})
@ConditionalOnExpression("${ideal-web.message-converter.enable-custom:true}")
public class CustomWebMvcConfigurer {
  private final WebProperties webProperties;

  public CustomWebMvcConfigurer(WebProperties webProperties) {
    this.webProperties = webProperties;
  }


  @Bean
  @SuppressWarnings("DuplicatedCode")
  public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
    MessageConverterProperties messageConverter = webProperties.getMessageConverter();
    SimpleModule JAVA_TIME_MODULE = new JavaTimeModule();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(messageConverter.getDateTimePattern());
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(messageConverter.getDatePattern());
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(messageConverter.getTimePattern());
    JAVA_TIME_MODULE
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
      // 对于空的对象转json的时候不抛出错误
      .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
      // 禁用遇到未知属性抛出异常
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      // 序列化BigDecimal时不使用科学计数法输出
      .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
      .setDateFormat(new SimpleDateFormat(messageConverter.getDateTimePattern()))
      .registerModule(JAVA_TIME_MODULE)
      .registerModule(longToStrongModule)
      .findAndRegisterModules();
    // 序列化是忽略null值
    if (messageConverter.isIgnoreNull()) {
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    return new MappingJackson2HttpMessageConverter(objectMapper);
  }

  @Bean
  public WebMvcConfigurer webMvcConfigurer(MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter) {
    return new WebMvcConfigurer() {
      @Override
      public void configureMessageConverters(@Nonnull List<HttpMessageConverter<?>> converters) {
        converters.add(customJackson2HttpMessageConverter);
      }
    };
  }
}
