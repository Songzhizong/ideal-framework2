package cn.idealframework2.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 宋志宗 on 2021/7/9
 */
@SuppressWarnings({"DuplicatedCode", "Java9CollectionFactory"})
public final class Maps {
  private static final Logger log = LoggerFactory.getLogger(Map.class);

  @Nonnull
  public static <K, V> Map<K, V> of() {
    return Collections.emptyMap();
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k, @Nonnull V v) {
    return Collections.singletonMap(k, v);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2) {
    Map<K, V> map = new HashMap<>(4);
    map.put(k1, v1);
    map.put(k2, v2);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3) {
    Map<K, V> map = new HashMap<>(8);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4) {
    Map<K, V> map = new HashMap<>(8);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5) {
    Map<K, V> map = new HashMap<>(8);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6) {
    Map<K, V> map = new HashMap<>(16);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6,
                                    @Nonnull K k7, @Nonnull V v7) {
    Map<K, V> map = new HashMap<>(16);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6,
                                    @Nonnull K k7, @Nonnull V v7,
                                    @Nonnull K k8, @Nonnull V v8) {
    Map<K, V> map = new HashMap<>(16);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6,
                                    @Nonnull K k7, @Nonnull V v7,
                                    @Nonnull K k8, @Nonnull V v8,
                                    @Nonnull K k9, @Nonnull V v9) {
    Map<K, V> map = new HashMap<>(16);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6,
                                    @Nonnull K k7, @Nonnull V v7,
                                    @Nonnull K k8, @Nonnull V v8,
                                    @Nonnull K k9, @Nonnull V v9,
                                    @Nonnull K k10, @Nonnull V v10) {
    Map<K, V> map = new HashMap<>(16);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6,
                                    @Nonnull K k7, @Nonnull V v7,
                                    @Nonnull K k8, @Nonnull V v8,
                                    @Nonnull K k9, @Nonnull V v9,
                                    @Nonnull K k10, @Nonnull V v10,
                                    @Nonnull K k11, @Nonnull V v11) {
    Map<K, V> map = new HashMap<>(16);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6,
                                    @Nonnull K k7, @Nonnull V v7,
                                    @Nonnull K k8, @Nonnull V v8,
                                    @Nonnull K k9, @Nonnull V v9,
                                    @Nonnull K k10, @Nonnull V v10,
                                    @Nonnull K k11, @Nonnull V v11,
                                    @Nonnull K k12, @Nonnull V v12) {
    Map<K, V> map = new HashMap<>(32);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    map.put(k12, v12);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6,
                                    @Nonnull K k7, @Nonnull V v7,
                                    @Nonnull K k8, @Nonnull V v8,
                                    @Nonnull K k9, @Nonnull V v9,
                                    @Nonnull K k10, @Nonnull V v10,
                                    @Nonnull K k11, @Nonnull V v11,
                                    @Nonnull K k12, @Nonnull V v12,
                                    @Nonnull K k13, @Nonnull V v13) {
    Map<K, V> map = new HashMap<>(32);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    map.put(k12, v12);
    map.put(k13, v13);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6,
                                    @Nonnull K k7, @Nonnull V v7,
                                    @Nonnull K k8, @Nonnull V v8,
                                    @Nonnull K k9, @Nonnull V v9,
                                    @Nonnull K k10, @Nonnull V v10,
                                    @Nonnull K k11, @Nonnull V v11,
                                    @Nonnull K k12, @Nonnull V v12,
                                    @Nonnull K k13, @Nonnull V v13,
                                    @Nonnull K k14, @Nonnull V v14) {
    Map<K, V> map = new HashMap<>(32);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    map.put(k12, v12);
    map.put(k13, v13);
    map.put(k14, v14);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6,
                                    @Nonnull K k7, @Nonnull V v7,
                                    @Nonnull K k8, @Nonnull V v8,
                                    @Nonnull K k9, @Nonnull V v9,
                                    @Nonnull K k10, @Nonnull V v10,
                                    @Nonnull K k11, @Nonnull V v11,
                                    @Nonnull K k12, @Nonnull V v12,
                                    @Nonnull K k13, @Nonnull V v13,
                                    @Nonnull K k14, @Nonnull V v14,
                                    @Nonnull K k15, @Nonnull V v15) {
    Map<K, V> map = new HashMap<>(32);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    map.put(k12, v12);
    map.put(k13, v13);
    map.put(k14, v14);
    map.put(k15, v15);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6,
                                    @Nonnull K k7, @Nonnull V v7,
                                    @Nonnull K k8, @Nonnull V v8,
                                    @Nonnull K k9, @Nonnull V v9,
                                    @Nonnull K k10, @Nonnull V v10,
                                    @Nonnull K k11, @Nonnull V v11,
                                    @Nonnull K k12, @Nonnull V v12,
                                    @Nonnull K k13, @Nonnull V v13,
                                    @Nonnull K k14, @Nonnull V v14,
                                    @Nonnull K k15, @Nonnull V v15,
                                    @Nonnull K k16, @Nonnull V v16) {
    Map<K, V> map = new HashMap<>(32);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    map.put(k12, v12);
    map.put(k13, v13);
    map.put(k14, v14);
    map.put(k15, v15);
    map.put(k16, v16);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6,
                                    @Nonnull K k7, @Nonnull V v7,
                                    @Nonnull K k8, @Nonnull V v8,
                                    @Nonnull K k9, @Nonnull V v9,
                                    @Nonnull K k10, @Nonnull V v10,
                                    @Nonnull K k11, @Nonnull V v11,
                                    @Nonnull K k12, @Nonnull V v12,
                                    @Nonnull K k13, @Nonnull V v13,
                                    @Nonnull K k14, @Nonnull V v14,
                                    @Nonnull K k15, @Nonnull V v15,
                                    @Nonnull K k16, @Nonnull V v16,
                                    @Nonnull K k17, @Nonnull V v17) {
    Map<K, V> map = new HashMap<>(32);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    map.put(k12, v12);
    map.put(k13, v13);
    map.put(k14, v14);
    map.put(k15, v15);
    map.put(k16, v16);
    map.put(k17, v17);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6,
                                    @Nonnull K k7, @Nonnull V v7,
                                    @Nonnull K k8, @Nonnull V v8,
                                    @Nonnull K k9, @Nonnull V v9,
                                    @Nonnull K k10, @Nonnull V v10,
                                    @Nonnull K k11, @Nonnull V v11,
                                    @Nonnull K k12, @Nonnull V v12,
                                    @Nonnull K k13, @Nonnull V v13,
                                    @Nonnull K k14, @Nonnull V v14,
                                    @Nonnull K k15, @Nonnull V v15,
                                    @Nonnull K k16, @Nonnull V v16,
                                    @Nonnull K k17, @Nonnull V v17,
                                    @Nonnull K k18, @Nonnull V v18) {
    Map<K, V> map = new HashMap<>(32);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    map.put(k12, v12);
    map.put(k13, v13);
    map.put(k14, v14);
    map.put(k15, v15);
    map.put(k16, v16);
    map.put(k17, v17);
    map.put(k18, v18);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6,
                                    @Nonnull K k7, @Nonnull V v7,
                                    @Nonnull K k8, @Nonnull V v8,
                                    @Nonnull K k9, @Nonnull V v9,
                                    @Nonnull K k10, @Nonnull V v10,
                                    @Nonnull K k11, @Nonnull V v11,
                                    @Nonnull K k12, @Nonnull V v12,
                                    @Nonnull K k13, @Nonnull V v13,
                                    @Nonnull K k14, @Nonnull V v14,
                                    @Nonnull K k15, @Nonnull V v15,
                                    @Nonnull K k16, @Nonnull V v16,
                                    @Nonnull K k17, @Nonnull V v17,
                                    @Nonnull K k18, @Nonnull V v18,
                                    @Nonnull K k19, @Nonnull V v19) {
    Map<K, V> map = new HashMap<>(32);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    map.put(k12, v12);
    map.put(k13, v13);
    map.put(k14, v14);
    map.put(k15, v15);
    map.put(k16, v16);
    map.put(k17, v17);
    map.put(k18, v18);
    map.put(k19, v19);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6,
                                    @Nonnull K k7, @Nonnull V v7,
                                    @Nonnull K k8, @Nonnull V v8,
                                    @Nonnull K k9, @Nonnull V v9,
                                    @Nonnull K k10, @Nonnull V v10,
                                    @Nonnull K k11, @Nonnull V v11,
                                    @Nonnull K k12, @Nonnull V v12,
                                    @Nonnull K k13, @Nonnull V v13,
                                    @Nonnull K k14, @Nonnull V v14,
                                    @Nonnull K k15, @Nonnull V v15,
                                    @Nonnull K k16, @Nonnull V v16,
                                    @Nonnull K k17, @Nonnull V v17,
                                    @Nonnull K k18, @Nonnull V v18,
                                    @Nonnull K k19, @Nonnull V v19,
                                    @Nonnull K k20, @Nonnull V v20) {
    Map<K, V> map = new HashMap<>(32);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    map.put(k12, v12);
    map.put(k13, v13);
    map.put(k14, v14);
    map.put(k15, v15);
    map.put(k16, v16);
    map.put(k17, v17);
    map.put(k18, v18);
    map.put(k19, v19);
    map.put(k20, v20);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  @SafeVarargs
  public static <K, V> Map<K, V> ofEntries(@Nonnull Map.Entry<K, V>... entries) {
    Map<K, V> map = new HashMap<>(entries.length);
    for (Map.Entry<K, V> entry : entries) {
      map.put(entry.getKey(), entry.getValue());
    }
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> unmodifiable(@Nonnull Map<K, V> map) {
    Map<K, V> newMap;
    try {
      @SuppressWarnings("rawtypes")
      Class<? extends Map> mapClass = map.getClass();
      @SuppressWarnings("rawtypes")
      Constructor<? extends Map> constructor = mapClass.getConstructor(Map.class);
      //noinspection unchecked
      newMap = constructor.newInstance(map);
    } catch (InstantiationException
             | IllegalAccessException
             | InvocationTargetException
             | NoSuchMethodException e) {
      log.warn("Reflection create map ex:", e);
      newMap = new LinkedHashMap<>(map);
    }
    return Collections.unmodifiableMap(newMap);
  }

  public static boolean isEmpty(@Nullable Map<?, ?> map) {
    return map == null || map.isEmpty();
  }

  public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
    return !isEmpty(map);
  }

  private Maps() {
  }
}
