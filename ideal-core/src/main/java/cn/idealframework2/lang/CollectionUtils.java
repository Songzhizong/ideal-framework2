package cn.idealframework2.lang;

import cn.idealframework2.utils.Asserts;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2021/7/9
 */
@SuppressWarnings("DuplicatedCode")
public final class CollectionUtils {

  public static boolean isEmpty(@Nullable Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }

  public static boolean isNotEmpty(@Nullable Collection<?> collection) {
    return !isEmpty(collection);
  }

  @SafeVarargs
  public static <T> boolean containsAny(@Nullable Collection<T> collection, @Nonnull T... ts) {
    if (isEmpty(collection)) {
      return false;
    }
    for (T t : ts) {
      if (collection.contains(t)) {
        return true;
      }
    }
    return false;
  }

  public static <T> boolean containsAny(@Nullable Collection<T> collection, @Nullable Collection<T> ts) {
    if (isEmpty(collection) || isEmpty(ts)) {
      return false;
    }
    for (T t : ts) {
      if (collection.contains(t)) {
        return true;
      }
    }
    return false;
  }


  @Nonnull
  public static <E> List<List<E>> chunked(@Nullable Collection<E> collection, int size) {
    Asserts.assertTrue(size > 0, "The size must be greater than 0");
    if (CollectionUtils.isEmpty(collection)) {
      return new ArrayList<>();
    }
    int listSize = collection.size();
    int resultCapacity = listSize / size + ((listSize % size == 0) ? 0 : 1);
    List<List<E>> result = new ArrayList<>(resultCapacity);
    List<E> currentList = new ArrayList<>(size);
    int currentSize = size;
    for (E next : collection) {
      if (currentSize == size) {
        currentList = new ArrayList<>(size);
        result.add(currentList);
        currentSize = 0;
      }
      currentList.add(next);
      currentSize++;
    }
    return result;
  }

  @Nonnull
  public static <E, R> List<List<R>> chunked(@Nullable Collection<E> collection,
                                             int size, @Nonnull Function<E, R> transform) {
    Asserts.assertTrue(size > 0, "The size must be greater than 0");
    if (CollectionUtils.isEmpty(collection)) {
      return new ArrayList<>();
    }
    int listSize = collection.size();
    int resultCapacity = listSize / size + ((listSize % size == 0) ? 0 : 1);
    List<List<R>> result = new ArrayList<>(resultCapacity);
    List<R> currentList = new ArrayList<>(size);
    int currentSize = size;
    for (E next : collection) {
      if (currentSize == size) {
        currentList = new ArrayList<>(size);
        result.add(currentList);
        currentSize = 0;
      }
      currentList.add(transform.apply(next));
      currentSize++;
    }
    return result;
  }

  /**
   * 对集合进行分组, 分组后总数不超过 limit限制
   *
   * @param collection 需要分组的集合
   * @param limit      分组后返回的集合最大尺寸
   * @author 宋志宗 on 2022/1/25
   */
  @Nonnull
  public static <E> List<List<E>> chunkedLimit(@Nullable Collection<E> collection, int limit) {
    Asserts.assertTrue(limit > 0, "The limit must be greater than 0");
    if (CollectionUtils.isEmpty(collection)) {
      return new ArrayList<>();
    }
    if (limit == 1) {
      List<E> list = new ArrayList<>(collection);
      List<List<E>> result = new ArrayList<>();
      result.add(list);
      return result;
    }
    int size = collection.size();
    if (size <= limit) {
      List<List<E>> result = new ArrayList<>();
      for (E e : collection) {
        ArrayList<E> list = new ArrayList<>();
        list.add(e);
        result.add(list);
      }
      return result;
    }
    List<List<E>> result = new ArrayList<>(limit);
    for (int i = 0; i < limit; i++) {
      result.add(new ArrayList<>());
    }
    int flag = 0;
    for (E e : collection) {
      List<E> es = result.get(flag);
      es.add(e);
      flag++;
      if (flag == limit) {
        flag = 0;
      }
    }
    return result;
  }

  private CollectionUtils() {
  }
}
