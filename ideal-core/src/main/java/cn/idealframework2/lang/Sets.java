package cn.idealframework2.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author 宋志宗 on 2021/7/8
 */
@SuppressWarnings({"DuplicatedCode", "unused", "Java9CollectionFactory"})
public final class Sets {

  public static boolean isEmpty(@Nullable Set<?> set) {
    return set == null || set.isEmpty();
  }

  public static boolean isNotEmpty(@Nullable Set<?> set) {
    return !Sets.isEmpty(set);
  }

  @SafeVarargs
  public static <T> boolean containsAny(@Nullable Set<T> set, @Nonnull T... ts) {
    return CollectionUtils.containsAny(set, ts);
  }

  public static <T> boolean containsAny(@Nullable Set<T> set,
                                        @Nullable Collection<T> ts) {
    return CollectionUtils.containsAny(set, ts);
  }

  @Nonnull
  public static <E> Set<E> of() {
    return Collections.emptySet();
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e) {
    return Collections.singleton(e);
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e1, @Nonnull E e2) {
    Set<E> set = new HashSet<>(16);
    set.add(e1);
    set.add(e2);
    return Collections.unmodifiableSet(set);
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e1, @Nonnull E e2, @Nonnull E e3) {
    Set<E> set = new HashSet<>(16);
    set.add(e1);
    set.add(e2);
    set.add(e3);
    return Collections.unmodifiableSet(set);
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e1, @Nonnull E e2,
                              @Nonnull E e3, @Nonnull E e4) {
    Set<E> set = new HashSet<>(16);
    set.add(e1);
    set.add(e2);
    set.add(e3);
    set.add(e4);
    return Collections.unmodifiableSet(set);
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e1, @Nonnull E e2,
                              @Nonnull E e3, @Nonnull E e4, @Nonnull E e5) {
    Set<E> set = new HashSet<>(16);
    set.add(e1);
    set.add(e2);
    set.add(e3);
    set.add(e4);
    set.add(e5);
    return Collections.unmodifiableSet(set);
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e1, @Nonnull E e2, @Nonnull E e3,
                              @Nonnull E e4, @Nonnull E e5, @Nonnull E e6) {
    Set<E> set = new HashSet<>(16);
    set.add(e1);
    set.add(e2);
    set.add(e3);
    set.add(e4);
    set.add(e5);
    set.add(e6);
    return Collections.unmodifiableSet(set);
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e1, @Nonnull E e2,
                              @Nonnull E e3, @Nonnull E e4,
                              @Nonnull E e5, @Nonnull E e6, @Nonnull E e7) {
    Set<E> set = new HashSet<>(16);
    set.add(e1);
    set.add(e2);
    set.add(e3);
    set.add(e4);
    set.add(e5);
    set.add(e6);
    set.add(e7);
    return Collections.unmodifiableSet(set);
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e1, @Nonnull E e2,
                              @Nonnull E e3, @Nonnull E e4,
                              @Nonnull E e5, @Nonnull E e6,
                              @Nonnull E e7, @Nonnull E e8) {
    Set<E> set = new HashSet<>(16);
    set.add(e1);
    set.add(e2);
    set.add(e3);
    set.add(e4);
    set.add(e5);
    set.add(e6);
    set.add(e7);
    set.add(e8);
    return Collections.unmodifiableSet(set);
  }

  @Nonnull
  @SafeVarargs
  public static <E> Set<E> of(E... elements) {
    if (elements == null || elements.length == 0) {
      return of();
    }
    HashSet<E> hashSet = new HashSet<>(Arrays.asList(elements));
    return Collections.unmodifiableSet(hashSet);
  }

  @Nonnull
  public static <E> Set<E> merge(@Nullable Collection<? extends E> c1,
                                 @Nullable Collection<? extends E> c2) {
    if (c1 == null) {
      c1 = Collections.emptySet();
    }
    if (c2 == null) {
      c2 = Collections.emptySet();
    }
    Set<E> result = new HashSet<>();
    result.addAll(c1);
    result.addAll(c2);
    return result;
  }

  @Nonnull
  public static <E> Set<E> merge(@Nullable Collection<? extends E> c1,
                                 @Nullable Collection<? extends E> c2,
                                 @Nullable Collection<? extends E> c3) {
    if (c1 == null) {
      c1 = Collections.emptySet();
    }
    if (c2 == null) {
      c2 = Collections.emptySet();
    }
    if (c3 == null) {
      c3 = Collections.emptySet();
    }
    Set<E> result = new HashSet<>();
    result.addAll(c1);
    result.addAll(c2);
    result.addAll(c3);
    return result;
  }

  @Nonnull
  public static <E> Set<E> merge(@Nullable Collection<? extends E> c1,
                                 @Nullable Collection<? extends E> c2,
                                 @Nullable Collection<? extends E> c3,
                                 @Nullable Collection<? extends E> c4) {
    if (c1 == null) {
      c1 = Collections.emptySet();
    }
    if (c2 == null) {
      c2 = Collections.emptySet();
    }
    if (c3 == null) {
      c3 = Collections.emptySet();
    }
    if (c4 == null) {
      c4 = Collections.emptySet();
    }
    Set<E> result = new HashSet<>();
    result.addAll(c1);
    result.addAll(c2);
    result.addAll(c3);
    result.addAll(c4);
    return result;
  }

  @Nonnull
  public static <E> Set<E> merge(@Nullable Collection<? extends E> c1,
                                 @Nullable Collection<? extends E> c2,
                                 @Nullable Collection<? extends E> c3,
                                 @Nullable Collection<? extends E> c4,
                                 @Nullable Collection<? extends E> c5) {
    if (c1 == null) {
      c1 = Collections.emptySet();
    }
    if (c2 == null) {
      c2 = Collections.emptySet();
    }
    if (c3 == null) {
      c3 = Collections.emptySet();
    }
    if (c4 == null) {
      c4 = Collections.emptySet();
    }
    if (c5 == null) {
      c5 = Collections.emptySet();
    }
    Set<E> result = new HashSet<>();
    result.addAll(c1);
    result.addAll(c2);
    result.addAll(c3);
    result.addAll(c4);
    result.addAll(c5);
    return result;
  }

  @Nonnull
  public static <E> Set<E> merge(@Nullable Collection<? extends E> c1,
                                 @Nullable Collection<? extends E> c2,
                                 @Nullable Collection<? extends E> c3,
                                 @Nullable Collection<? extends E> c4,
                                 @Nullable Collection<? extends E> c5,
                                 @Nullable Collection<? extends E> c6) {
    if (c1 == null) {
      c1 = Collections.emptySet();
    }
    if (c2 == null) {
      c2 = Collections.emptySet();
    }
    if (c3 == null) {
      c3 = Collections.emptySet();
    }
    if (c4 == null) {
      c4 = Collections.emptySet();
    }
    if (c5 == null) {
      c5 = Collections.emptySet();
    }
    if (c6 == null) {
      c6 = Collections.emptySet();
    }
    Set<E> result = new HashSet<>();
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
  public static <E> Set<E> merge(@Nullable Collection<? extends E>... cs) {
    if (cs == null) {
      return new HashSet<>();
    }
    Set<E> result = new HashSet<>();
    for (Collection<? extends E> c : cs) {
      if (c != null) {
        result.addAll(c);
      }
    }
    return result;
  }

  @Nonnull
  public static <E> List<List<E>> chunked(@Nullable Set<E> set, int size) {
    return CollectionUtils.chunked(set, size);
  }

  @Nullable
  public static <E> E firstOrNull(@Nullable Set<E> set) {
    if (isEmpty(set)) {
      return null;
    }
    Iterator<E> iterator = set.iterator();
    if (iterator.hasNext()) {
      return iterator.next();
    }
    return null;
  }

  @Nonnull
  public static <E> Optional<E> first(@Nullable Set<E> set) {
    E firstOrNull = firstOrNull(set);
    return Optional.ofNullable(firstOrNull);
  }
}
