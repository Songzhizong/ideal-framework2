package cn.idealframework2.json;

import cn.idealframework2.transmission.Result;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/8/5
 */
@SuppressWarnings("AlibabaAbstractClassShouldStartWithAbstractNaming")
public abstract class TypeReference<T> extends com.fasterxml.jackson.core.type.TypeReference<T> {

  public static final TypeReference<Map<String, String>> STRING_MAP_REFERENCE
    = new TypeReference<>() {
  };

  public static final TypeReference<List<String>> STRING_LIST_REFERENCE
    = new TypeReference<>() {
  };

  public static final TypeReference<Set<String>> STRING_SET_REFERENCE
    = new TypeReference<>() {
  };


  public static final TypeReference<Result<Void>> VOID_RESULT_REFERENCE = new TypeReference<>() {
  };

  public static final TypeReference<Result<String>> STRING_RESULT_REFERENCE = new TypeReference<>() {
  };

  public static final TypeReference<Result<Long>> LONG_RESULT_REFERENCE = new TypeReference<>() {
  };

  public static final TypeReference<Result<Integer>> INTEGER_RESULT_REFERENCE = new TypeReference<>() {
  };
}
