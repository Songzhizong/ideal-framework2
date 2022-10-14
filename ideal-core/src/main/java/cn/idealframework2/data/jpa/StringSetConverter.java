package cn.idealframework2.data.jpa;

import cn.idealframework2.lang.Joiner;
import cn.idealframework2.lang.Sets;
import cn.idealframework2.lang.StringUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/11/2
 */
@Converter
public class StringSetConverter implements AttributeConverter<Set<String>, String> {

  @Override
  public String convertToDatabaseColumn(Set<String> attribute) {
    if (Sets.isEmpty(attribute)) {
      return "";
    }
    return Joiner.joinSkipNull(attribute, ",");
  }

  @Override
  public Set<String> convertToEntityAttribute(String dbData) {
    if (StringUtils.isBlank(dbData)) {
      return new LinkedHashSet<>();
    }
    String[] split = StringUtils.split(dbData, ",");
    return new LinkedHashSet<>(Arrays.asList(split));
  }
}
