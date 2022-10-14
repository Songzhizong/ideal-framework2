package cn.idealframework2.data.jpa;

import cn.idealframework2.lang.Joiner;
import cn.idealframework2.lang.Lists;
import cn.idealframework2.lang.StringUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 宋志宗 on 2021/11/2
 */
@Converter
public class LongListConverter implements AttributeConverter<List<Long>, String> {

  @Override
  public String convertToDatabaseColumn(List<Long> attribute) {
    if (Lists.isEmpty(attribute)) {
      return "";
    }
    return Joiner.joinSkipNull(attribute, ",");
  }

  @Override
  public List<Long> convertToEntityAttribute(String dbData) {
    if (StringUtils.isBlank(dbData)) {
      return new ArrayList<>();
    }
    String[] split = StringUtils.split(dbData, ",");
    List<Long> res = new ArrayList<>(split.length);
    for (String s : split) {
      res.add(Long.valueOf(s));
    }
    return res;
  }
}
