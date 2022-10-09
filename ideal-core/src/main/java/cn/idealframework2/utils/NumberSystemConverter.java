package cn.idealframework2.utils;

import javax.annotation.Nonnull;
import java.util.Stack;

/**
 * 进制转换工具
 *
 * @author 宋志宗 on 2021/6/1
 */
@SuppressWarnings("unused")
public final class NumberSystemConverter {
  /** 26进制: 小写字母 */
  public static final int SYSTEM_26 = 26;
  /** 32进制: 不包含 I L O U 字符 */
  public static final int SYSTEM_32 = 32;
  /** 36进制: 数字 + 小写字母 */
  public static final int SYSTEM_36 = 36;
  /** 52进制: 大写字母 + 小写字母 */
  public static final int SYSTEM_52 = 52;
  /** 58进制: 不包含 0 O l I 字符 */
  public static final int SYSTEM_58 = 58;
  /** 62进制: 数字 + 大小写字母 */
  public static final int SYSTEM_62 = 62;
  /** 最大62进制 */
  private static final int MAX = 62;
  /** 26进制: 小写字母 */
  private static final char[] CHARS_26 = {
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
    'u', 'v', 'w', 'x', 'y', 'z'
  };
  /** 32进制: 不包含 I L O U 字符 */
  private static final char[] CHARS_32 = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
    'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X',
    'Y', 'Z'
  };
  /** 36进制: 数字 + 小写字母 */
  private static final char[] CHARS_36 = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
    'u', 'v', 'w', 'x', 'y', 'z'
  };
  /** 52进制: 大写字母 + 小写字母 */
  private static final char[] CHARS_52 = {
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
    'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
    'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
    'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
    'Y', 'Z'
  };
  /** 58进制: 不包含 0 O l I 字符 */
  private static final char[] CHARS_58 = {
    '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
    'k', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
    'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
    'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N',
    'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
    'Y', 'Z'
  };
  /** 62进制: 数字 + 大小写字母 */
  private static final char[] CHARS_62 = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
    'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
    'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
    'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
    'Y', 'Z'
  };

  private NumberSystemConverter() {
  }

  /**
   * 10进制转其他进制
   *
   * @param number       10进制数字
   * @param targetSystem 目标进制
   * @return 其他进制表达数字
   * @author 宋志宗 on 2021/6/1
   */
  @Nonnull
  public static String tenSystemTo(long number, int targetSystem) {
    Asserts.range(targetSystem, 2, MAX, "require: 2 < targetSystem < " + MAX);
    char[] charArray = getCharArray(targetSystem);
    long rest = number;
    Stack<Character> stack = new Stack<>();
    StringBuilder result = new StringBuilder(0);
    while (rest != 0) {
      stack.add(charArray[Math.toIntExact((rest - rest / targetSystem * targetSystem))]);
      rest /= targetSystem;
    }
    while (!stack.isEmpty()) {
      result.append(stack.pop());
    }
    return result.toString();
  }

  /**
   * 其他进制转10进制
   *
   * @param value        其他进制值
   * @param originSystem [value]是几进制的数值
   * @return 10进制结果
   * @author 宋志宗 on 2021/6/1
   */
  public static long toTenSystem(@Nonnull String value, int originSystem) {
    Asserts.range(originSystem, 2, MAX, "require: 2 < originSystem < " + MAX);
    char[] charArray = getCharArray(originSystem);
    long dst = 0L;
    char[] chars = value.toCharArray();
    for (char element : chars) {
      for (int i = 0; i < charArray.length; i++) {
        if (element == charArray[i]) {
          dst = dst * originSystem + i;
          break;
        }
      }
    }
    return dst;
  }

  private static char[] getCharArray(int system) {
    return switch (system) {
      case 26 -> CHARS_26;
      case 32 -> CHARS_32;
      case 36 -> CHARS_36;
      case 52 -> CHARS_52;
      case 58 -> CHARS_58;
      default -> CHARS_62;
    };
  }
}
