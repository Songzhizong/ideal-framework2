package cn.idealframework2.data.jpa;

import cn.idealframework2.lang.Joiner;
import cn.idealframework2.lang.Lists;
import cn.idealframework2.lang.StringUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 宋志宗 on 2021/11/2
 */
@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

  @Override
  public String convertToDatabaseColumn(List<String> attribute) {
    if (Lists.isEmpty(attribute)) {
      return "";
    }
    return Joiner.joinSkipNull(attribute, ",");
  }

  @Override
  public List<String> convertToEntityAttribute(String dbData) {
    if (StringUtils.isBlank(dbData)) {
      return new ArrayList<>();
    }
    String[] split = StringUtils.split(dbData, ",");
    return new ArrayList<>(Arrays.asList(split));
  }
}
