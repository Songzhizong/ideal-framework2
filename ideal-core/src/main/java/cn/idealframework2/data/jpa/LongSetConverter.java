package cn.idealframework2.data.jpa;

import cn.idealframework2.lang.Joiner;
import cn.idealframework2.lang.Sets;
import cn.idealframework2.lang.StringUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/11/2
 */
@Converter
public class LongSetConverter implements AttributeConverter<Set<Long>, String> {

  @Override
  public String convertToDatabaseColumn(Set<Long> attribute) {
    if (Sets.isEmpty(attribute)) {
      return "";
    }
    return Joiner.joinSkipNull(attribute, ",");
  }

  @Override
  public Set<Long> convertToEntityAttribute(String dbData) {
    if (StringUtils.isBlank(dbData)) {
      return new LinkedHashSet<>();
    }
    String[] split = StringUtils.split(dbData, ",");
    Set<Long> res = new LinkedHashSet<>(Math.max((int) (split.length / 0.75F) + 1, 16));
    for (String s : split) {
      res.add(Long.valueOf(s));
    }
    return res;
  }
}
