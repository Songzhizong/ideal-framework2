package cn.idealframework2.lang;

import javax.annotation.Nonnull;
import java.lang.reflect.Array;

/**
 * @author 宋志宗 on 2021/8/5
 */
public class ArrayUtils {
  public static final int INDEX_NOT_FOUND = -1;
  public static final String[] EMPTY_STRING_ARRAY = new String[0];
  /**
   * An empty immutable {@code char} array.
   */
  public static final char[] EMPTY_CHAR_ARRAY = new char[0];
  public static final int[] EMPTY_INT_ARRAY = new int[0];

  public static int getLength(final Object array) {
    if (array == null) {
      return 0;
    }
    return Array.getLength(array);
  }

  public static boolean contains(final int[] array, final int valueToFind) {
    return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
  }

  public static int indexOf(final int[] array, final int valueToFind) {
    return indexOf(array, valueToFind, 0);
  }

  public static int indexOf(final int[] array, final int valueToFind, int startIndex) {
    if (array == null) {
      return INDEX_NOT_FOUND;
    }
    if (startIndex < 0) {
      startIndex = 0;
    }
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i]) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }

  public static int indexOf(final long[] array, final long valueToFind) {
    return indexOf(array, valueToFind, 0);
  }

  public static int indexOf(final long[] array, final long valueToFind, int startIndex) {
    if (array == null) {
      return INDEX_NOT_FOUND;
    }
    if (startIndex < 0) {
      startIndex = 0;
    }
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i]) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }

  /**
   * <p>Checks if an array of primitive booleans is empty or {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is empty or {@code null}
   * @since 2.1
   */
  public static boolean isEmpty(final boolean[] array) {
    return getLength(array) == 0;
  }

  /**
   * <p>Checks if an array of primitive bytes is empty or {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is empty or {@code null}
   * @since 2.1
   */
  public static boolean isEmpty(final byte[] array) {
    return getLength(array) == 0;
  }

  // IndexOf search
  // ----------------------------------------------------------------------

  /**
   * <p>Checks if an array of primitive chars is empty or {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is empty or {@code null}
   * @since 2.1
   */
  public static boolean isEmpty(final char[] array) {
    return getLength(array) == 0;
  }

  /**
   * <p>Checks if an array of primitive doubles is empty or {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is empty or {@code null}
   * @since 2.1
   */
  public static boolean isEmpty(final double[] array) {
    return getLength(array) == 0;
  }

  /**
   * <p>Checks if an array of primitive floats is empty or {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is empty or {@code null}
   * @since 2.1
   */
  public static boolean isEmpty(final float[] array) {
    return getLength(array) == 0;
  }

  /**
   * <p>Checks if an array of primitive ints is empty or {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is empty or {@code null}
   * @since 2.1
   */
  public static boolean isEmpty(final int[] array) {
    return getLength(array) == 0;
  }


  /**
   * <p>Checks if an array of primitive longs is empty or {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is empty or {@code null}
   * @since 2.1
   */
  public static boolean isEmpty(final long[] array) {
    return getLength(array) == 0;
  }

  // ----------------------------------------------------------------------

  /**
   * <p>Checks if an array of Objects is empty or {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is empty or {@code null}
   * @since 2.1
   */
  public static boolean isEmpty(final Object[] array) {
    return getLength(array) == 0;
  }

  /**
   * <p>Checks if an array of primitive shorts is empty or {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is empty or {@code null}
   * @since 2.1
   */
  public static boolean isEmpty(final short[] array) {
    return getLength(array) == 0;
  }


  /**
   * <p>Checks if an array of primitive booleans is not empty and not {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is not empty and not {@code null}
   * @since 2.5
   */
  public static boolean isNotEmpty(final boolean[] array) {
    return !isEmpty(array);
  }

  /**
   * <p>Checks if an array of primitive bytes is not empty and not {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is not empty and not {@code null}
   * @since 2.5
   */
  public static boolean isNotEmpty(final byte[] array) {
    return !isEmpty(array);
  }

  /**
   * <p>Checks if an array of primitive chars is not empty and not {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is not empty and not {@code null}
   * @since 2.5
   */
  public static boolean isNotEmpty(final char[] array) {
    return !isEmpty(array);
  }

  /**
   * <p>Checks if an array of primitive doubles is not empty and not {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is not empty and not {@code null}
   * @since 2.5
   */
  public static boolean isNotEmpty(final double[] array) {
    return !isEmpty(array);
  }

  /**
   * <p>Checks if an array of primitive floats is not empty and not {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is not empty and not {@code null}
   * @since 2.5
   */
  public static boolean isNotEmpty(final float[] array) {
    return !isEmpty(array);
  }

  /**
   * <p>Checks if an array of primitive ints is not empty and not {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is not empty and not {@code null}
   * @since 2.5
   */
  public static boolean isNotEmpty(final int[] array) {
    return !isEmpty(array);
  }

  /**
   * <p>Checks if an array of primitive longs is not empty and not {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is not empty and not {@code null}
   * @since 2.5
   */
  public static boolean isNotEmpty(final long[] array) {
    return !isEmpty(array);
  }

  /**
   * <p>Checks if an array of primitive shorts is not empty and not {@code null}.
   *
   * @param array the array to test
   * @return {@code true} if the array is not empty and not {@code null}
   * @since 2.5
   */
  public static boolean isNotEmpty(final short[] array) {
    return !isEmpty(array);
  }

  // ----------------------------------------------------------------------

  /**
   * <p>Checks if an array of Objects is not empty and not {@code null}.
   *
   * @param <T>   the component type of the array
   * @param array the array to test
   * @return {@code true} if the array is not empty and not {@code null}
   * @since 2.5
   */
  public static <T> boolean isNotEmpty(final T[] array) {
    return !isEmpty(array);
  }

  @Nonnull
  @SafeVarargs
  public static <T> T[] of(T... ts) {
    return ts;
  }
}
