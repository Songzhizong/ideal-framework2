package cn.idealframework2.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2021/4/27
 */
@SuppressWarnings({"DuplicatedCode", "unused"})
public final class Lists {

  public static boolean isEmpty(@Nullable List<?> list) {
    return list == null || list.isEmpty();
  }

  public static boolean isNotEmpty(@Nullable List<?> list) {
    return !Lists.isEmpty(list);
  }

  @Nonnull
  public static <E> List<E> unmodifiable(@Nullable List<E> list) {
    if (list == null) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(list);
  }

  @Nonnull
  public static <E> List<E> of() {
    return Collections.emptyList();
  }

  @Nonnull
  public static <E> List<E> of(E e) {
    return Collections.singletonList(e);
  }

  @Nonnull
  public static <E> List<E> of(E e1, E e2) {
    List<E> list = new ArrayList<>(2);
    list.add(e1);
    list.add(e2);
    return Collections.unmodifiableList(list);
  }

  @Nonnull
  public static <E> List<E> of(E e1, E e2, E e3) {
    List<E> list = new ArrayList<>(3);
    list.add(e1);
    list.add(e2);
    list.add(e3);
    return Collections.unmodifiableList(list);
  }

  @Nonnull
  public static <E> List<E> of(E e1, E e2, E e3, E e4) {
    List<E> list = new ArrayList<>(4);
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    return Collections.unmodifiableList(list);
  }

  @Nonnull
  public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5) {
    List<E> list = new ArrayList<>(5);
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    return Collections.unmodifiableList(list);
  }

  @Nonnull
  public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
    List<E> list = new ArrayList<>(6);
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    list.add(e6);
    return Collections.unmodifiableList(list);
  }

  @Nonnull
  public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
    List<E> list = new ArrayList<>(7);
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    list.add(e6);
    list.add(e7);
    return Collections.unmodifiableList(list);
  }

  @Nonnull
  public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
    List<E> list = new ArrayList<>(8);
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    list.add(e6);
    list.add(e7);
    list.add(e8);
    return Collections.unmodifiableList(list);
  }

  @Nonnull
  @SafeVarargs
  public static <E> List<E> of(E... elements) {
    if (elements == null || elements.length == 0) {
      return of();
    }
    ArrayList<E> arrayList = new ArrayList<>(Arrays.asList(elements));
    return Collections.unmodifiableList(arrayList);
  }

  @Nonnull
  public static <E> List<E> ofArray(@Nullable E[] intArray) {
    if (intArray == null) {
      return Collections.emptyList();
    }
    return Arrays.asList(intArray);
  }

  @Nonnull
  public static List<Character> ofArray(@Nullable char[] charArray) {
    if (charArray == null) {
      return Collections.emptyList();
    }
    List<Character> res = new ArrayList<>(charArray.length);
    for (char i : charArray) {
      res.add(i);
    }
    return unmodifiable(res);
  }

  @Nonnull
  public static List<Integer> ofArray(@Nullable int[] intArray) {
    if (intArray == null) {
      return Collections.emptyList();
    }
    List<Integer> res = new ArrayList<>(intArray.length);
    for (int i : intArray) {
      res.add(i);
    }
    return unmodifiable(res);
  }

  @Nonnull
  public static List<Long> ofArray(@Nullable long[] longArray) {
    if (longArray == null) {
      return Collections.emptyList();
    }
    List<Long> res = new ArrayList<>(longArray.length);
    for (long i : longArray) {
      res.add(i);
    }
    return unmodifiable(res);
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e);
    return list;
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e1, E e2) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e1);
    list.add(e2);
    return list;
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e1, E e2, E e3) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e1);
    list.add(e2);
    list.add(e3);
    return list;
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e1, E e2, E e3, E e4) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    return list;
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e1, E e2, E e3, E e4, E e5) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    return list;
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e1, E e2, E e3, E e4, E e5, E e6) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    list.add(e6);
    return list;
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    list.add(e6);
    list.add(e7);
    return list;
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    list.add(e6);
    list.add(e7);
    list.add(e8);
    return list;
  }

  @Nonnull
  @SafeVarargs
  public static <E> ArrayList<E> arrayList(E... elements) {
    if (elements == null || elements.length == 0) {
      return new ArrayList<>();
    }
    return new ArrayList<>(Arrays.asList(elements));
  }

  @Nonnull
  public static <E> List<E> merge(@Nullable Collection<? extends E> c1,
                                  @Nullable Collection<? extends E> c2) {
    if (c1 == null) {
      c1 = Collections.emptyList();
    }
    if (c2 == null) {
      c2 = Collections.emptyList();
    }
    int capacity = c1.size() + c2.size();
    List<E> result = new ArrayList<>(capacity);
    result.addAll(c1);
    result.addAll(c2);
    return result;
  }

  @Nonnull
  public static <E> List<E> merge(@Nullable Collection<? extends E> c1,
                                  @Nullable Collection<? extends E> c2,
                                  @Nullable Collection<? extends E> c3) {
    if (c1 == null) {
      c1 = Collections.emptyList();
    }
    if (c2 == null) {
      c2 = Collections.emptyList();
    }
    if (c3 == null) {
      c3 = Collections.emptyList();
    }
    int capacity = c1.size() + c2.size() + c3.size();
    List<E> result = new ArrayList<>(capacity);
    result.addAll(c1);
    result.addAll(c2);
    result.addAll(c3);
    return result;
  }

  @Nonnull
  public static <E> List<E> merge(@Nullable Collection<? extends E> c1,
                                  @Nullable Collection<? extends E> c2,
                                  @Nullable Collection<? extends E> c3,
                                  @Nullable Collection<? extends E> c4) {
    if (c1 == null) {
      c1 = Collections.emptyList();
    }
    if (c2 == null) {
      c2 = Collections.emptyList();
    }
    if (c3 == null) {
      c3 = Collections.emptyList();
    }
    if (c4 == null) {
      c4 = Collections.emptyList();
    }
    int capacity = c1.size() + c2.size() + c3.size() + c4.size();
    List<E> result = new ArrayList<>(capacity);
    result.addAll(c1);
    result.addAll(c2);
    result.addAll(c3);
    result.addAll(c4);
    return result;
  }

  @Nonnull
  public static <E> List<E> merge(@Nullable Collection<? extends E> c1,
                                  @Nullable Collection<? extends E> c2,
                                  @Nullable Collection<? extends E> c3,
                                  @Nullable Collection<? extends E> c4,
                                  @Nullable Collection<? extends E> c5) {
    if (c1 == null) {
      c1 = Collections.emptyList();
    }
    if (c2 == null) {
      c2 = Collections.emptyList();
    }
    if (c3 == null) {
      c3 = Collections.emptyList();
    }
    if (c4 == null) {
      c4 = Collections.emptyList();
    }
    if (c5 == null) {
      c5 = Collections.emptyList();
    }
    int capacity = c1.size() + c2.size() + c3.size() + c4.size() + c5.size();
    List<E> result = new ArrayList<>(capacity);
    result.addAll(c1);
    result.addAll(c2);
    result.addAll(c3);
    result.addAll(c4);
    result.addAll(c5);
    return result;
  }

  @Nonnull
  public static <E> List<E> merge(@Nullable Collection<? extends E> c1,
                                  @Nullable Collection<? extends E> c2,
                                  @Nullable Collection<? extends E> c3,
                                  @Nullable Collection<? extends E> c4,
                                  @Nullable Collection<? extends E> c5,
                                  @Nullable Collection<? extends E> c6) {
    if (c1 == null) {
      c1 = Collections.emptyList();
    }
    if (c2 == null) {
      c2 = Collections.emptyList();
    }
    if (c3 == null) {
      c3 = Collections.emptyList();
    }
    if (c4 == null) {
      c4 = Collections.emptyList();
    }
    if (c5 == null) {
      c5 = Collections.emptyList();
    }
    if (c6 == null) {
      c6 = Collections.emptyList();
    }
    int capacity = c1.size() + c2.size() + c3.size() + c4.size() + c5.size() + c6.size();
    List<E> result = new ArrayList<>(capacity);
    result.addAll(c1);
    result.addAll(c2);
    result.addAll(c3);
    result.addAll(c4);
    result.addAll(c5);
    result.addAll(c6);
    return result;
  }

  @Nonnull
  @SafeVarargs
  public static <E> List<E> merge(@Nullable Collection<? extends E> collection,
                                  @Nullable Collection<? extends E>... cs) {
    if (collection == null) {
      collection = Collections.emptyList();
    }
    int capacity = collection.size();
    if (cs != null) {
      for (Collection<? extends E> c : cs) {
        if (c != null) {
          capacity += c.size();
        }
      }
    }
    List<E> result = new ArrayList<>(capacity);
    result.addAll(collection);
    if (cs != null) {
      for (Collection<? extends E> c : cs) {
        if (c != null) {
          result.addAll(c);
        }
      }
    }
    return result;
  }

  /**
   * List 去重
   *
   * @param list 输入对象
   * @param <E>  必须重写hashcode和equals方法
   * @return 去重后的结果
   * @author 宋志宗 on 2021/9/27
   */
  @Nonnull
  public static <E> List<E> distinct(@Nullable List<E> list) {
    if (list == null) {
      return new ArrayList<>();
    }
    return new ArrayList<>(new LinkedHashSet<>(list));
  }

  /**
   * List去重
   *
   * @param list     需要去重的list
   * @param function 根据元素获取去重key
   * @return 去重后的list
   * @author 宋志宗 on 2021/9/27
   */
  @Nonnull
  public static <E, D> List<E> distinctBy(@Nullable List<E> list,
                                          @Nonnull Function<E, D> function) {
    if (list == null) {
      return new ArrayList<>();
    }
    if (list.isEmpty()) {
      return list;
    }
    int initialCapacity = Math.max((int) (list.size() / 0.75F) + 1, 16);
    Map<D, Boolean> map = new HashMap<>(initialCapacity);
    List<E> result = new ArrayList<>();
    for (E e : list) {
      if (e == null) {
        continue;
      }
      D apply = function.apply(e);
      Boolean put = map.put(apply, Boolean.TRUE);
      if (put == null) {
        result.add(e);
      }
    }
    return result;
  }

  /**
   * 对集合进行分块
   *
   * @param list 集合
   * @param size 块大小
   * @author 宋志宗 on 2021/10/27
   */
  @Nonnull
  public static <E> List<List<E>> chunked(@Nullable List<E> list, int size) {
    return CollectionUtils.chunked(list, size);
  }


  /**
   * 对集合进行分块并转换
   *
   * @param collection 集合
   * @param size       块大小
   * @param transform  转换函数
   * @author 宋志宗 on 2021/10/27
   */
  @Nonnull
  public static <E, R> List<List<R>> chunked(@Nullable List<E> collection,
                                             int size, @Nonnull Function<E, R> transform) {
    return CollectionUtils.chunked(collection, size, transform);
  }

  /**
   * 对集合进行分组, 分组后总数不超过 limit限制
   *
   * @param list  需要分组的集合
   * @param limit 分组后返回的集合最大尺寸
   * @author 宋志宗 on 2022/1/25
   */
  @Nonnull
  public static <E> List<List<E>> chunkedLimit(@Nullable List<E> list, int limit) {
    return CollectionUtils.chunkedLimit(list, limit);
  }

  private Lists() {
  }
}
