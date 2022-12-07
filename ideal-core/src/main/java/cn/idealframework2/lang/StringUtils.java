package cn.idealframework2.lang;

import javax.annotation.Nullable;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 字符串工具类, 移植自 commons-lang3
 *
 * @author 宋志宗 on 2021/8/5
 */
@SuppressWarnings({"unused", "SpellCheckingInspection", "GrazieInspection"})
public class StringUtils {

  /**
   * A String for a space character.
   */
  public static final String SPACE = " ";
  /**
   * The empty String {@code ""}.
   */
  public static final String EMPTY = "";
  /**
   * A String for linefeed LF ("\n").
   *
   * @see <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10.6">JLF: Escape Sequences
   * for Character and String Literals</a>
   */
  public static final String LF = "\n";
  /**
   * A String for carriage return CR ("\r").
   *
   * @see <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10.6">JLF: Escape Sequences
   * for Character and String Literals</a>
   */
  public static final String CR = "\r";
  /**
   * Represents a failed index search.
   */
  public static final int INDEX_NOT_FOUND = -1;
  private static final int STRING_BUILDER_SIZE = 256;
  /**
   * <p>The maximum size to which the padding constant(s) can expand.</p>
   */
  private static final int PAD_LIMIT = 8192;

  /**
   * Pattern used in {@link #stripAccents(String)}.
   */
  private static final Pattern STRIP_ACCENTS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+"); //$NON-NLS-1$

  /**
   * <p>{@code StringUtils} instances should NOT be constructed in
   * standard programming. Instead, the class should be used as
   * {@code StringUtils.trim(" foo ");}.</p>
   *
   * <p>This constructor is public to permit tools that require a JavaBean
   * instance to operate.</p>
   */
  public StringUtils() {
  }

  /**
   * <p>Abbreviates a String using ellipses. This will turn
   * "Now is the time for all good men" into "Now is the time for..."</p>
   *
   * <p>Specifically:</p>
   * <ul>
   *   <li>If the number of characters in {@code str} is less than or equal to
   *       {@code maxWidth}, return {@code str}.</li>
   *   <li>Else abbreviate it to {@code (substring(str, 0, max-3) + "...")}.</li>
   *   <li>If {@code maxWidth} is less than {@code 4}, throw an
   *       {@code IllegalArgumentException}.</li>
   *   <li>In no case will it return a String of length greater than
   *       {@code maxWidth}.</li>
   * </ul>
   *
   * <pre>
   * StringUtils.abbreviate(null, *)      = null
   * StringUtils.abbreviate("", 4)        = ""
   * StringUtils.abbreviate("abcdefg", 6) = "abc..."
   * StringUtils.abbreviate("abcdefg", 7) = "abcdefg"
   * StringUtils.abbreviate("abcdefg", 8) = "abcdefg"
   * StringUtils.abbreviate("abcdefg", 4) = "a..."
   * StringUtils.abbreviate("abcdefg", 3) = IllegalArgumentException
   * </pre>
   *
   * @param str      the String to check, may be null
   * @param maxWidth maximum length of result String, must be at least 4
   * @return abbreviated String, {@code null} if null String input
   * @throws IllegalArgumentException if the width is too small
   */
  public static String abbreviate(String str, int maxWidth) {
    return abbreviate(str, "...", 0, maxWidth);
  }

  /**
   * <p>Abbreviates a String using ellipses. This will turn
   * "Now is the time for all good men" into "...is the time for..."</p>
   *
   * <p>Works like {@code abbreviate(String, int)}, but allows you to specify
   * a "left edge" offset.  Note that this left edge is not necessarily going to
   * be the leftmost character in the result, or the first character following the
   * ellipses, but it will appear somewhere in the result.
   *
   * <p>In no case will it return a String of length greater than
   * {@code maxWidth}.</p>
   *
   * <pre>
   * StringUtils.abbreviate(null, *, *)                = null
   * StringUtils.abbreviate("", 0, 4)                  = ""
   * StringUtils.abbreviate("abcdefghijklmno", -1, 10) = "abcdefg..."
   * StringUtils.abbreviate("abcdefghijklmno", 0, 10)  = "abcdefg..."
   * StringUtils.abbreviate("abcdefghijklmno", 1, 10)  = "abcdefg..."
   * StringUtils.abbreviate("abcdefghijklmno", 4, 10)  = "abcdefg..."
   * StringUtils.abbreviate("abcdefghijklmno", 5, 10)  = "...fghi..."
   * StringUtils.abbreviate("abcdefghijklmno", 6, 10)  = "...ghij..."
   * StringUtils.abbreviate("abcdefghijklmno", 8, 10)  = "...ijklmno"
   * StringUtils.abbreviate("abcdefghijklmno", 10, 10) = "...ijklmno"
   * StringUtils.abbreviate("abcdefghijklmno", 12, 10) = "...ijklmno"
   * StringUtils.abbreviate("abcdefghij", 0, 3)        = IllegalArgumentException
   * StringUtils.abbreviate("abcdefghij", 5, 6)        = IllegalArgumentException
   * </pre>
   *
   * @param str      the String to check, may be null
   * @param offset   left edge of source String
   * @param maxWidth maximum length of result String, must be at least 4
   * @return abbreviated String, {@code null} if null String input
   * @throws IllegalArgumentException if the width is too small
   */
  public static String abbreviate(String str, int offset, int maxWidth) {
    return abbreviate(str, "...", offset, maxWidth);
  }

  /**
   * <p>Abbreviates a String using another given String as replacement marker. This will turn
   * "Now is the time for all good men" into "Now is the time for..." if "..." was defined
   * as the replacement marker.</p>
   *
   * <p>Specifically:</p>
   * <ul>
   *   <li>If the number of characters in {@code str} is less than or equal to
   *       {@code maxWidth}, return {@code str}.</li>
   *   <li>Else abbreviate it to {@code (substring(str, 0, max-abbrevMarker.length) + abbrevMarker)}.</li>
   *   <li>If {@code maxWidth} is less than {@code abbrevMarker.length + 1}, throw an
   *       {@code IllegalArgumentException}.</li>
   *   <li>In no case will it return a String of length greater than
   *       {@code maxWidth}.</li>
   * </ul>
   *
   * <pre>
   * StringUtils.abbreviate(null, "...", *)      = null
   * StringUtils.abbreviate("abcdefg", null, *)  = "abcdefg"
   * StringUtils.abbreviate("", "...", 4)        = ""
   * StringUtils.abbreviate("abcdefg", ".", 5)   = "abcd."
   * StringUtils.abbreviate("abcdefg", ".", 7)   = "abcdefg"
   * StringUtils.abbreviate("abcdefg", ".", 8)   = "abcdefg"
   * StringUtils.abbreviate("abcdefg", "..", 4)  = "ab.."
   * StringUtils.abbreviate("abcdefg", "..", 3)  = "a.."
   * StringUtils.abbreviate("abcdefg", "..", 2)  = IllegalArgumentException
   * StringUtils.abbreviate("abcdefg", "...", 3) = IllegalArgumentException
   * </pre>
   *
   * @param str          the String to check, may be null
   * @param abbrevMarker the String used as replacement marker
   * @param maxWidth     maximum length of result String, must be at least {@code abbrevMarker.length + 1}
   * @return abbreviated String, {@code null} if null String input
   * @throws IllegalArgumentException if the width is too small
   */
  public static String abbreviate(String str, String abbrevMarker, int maxWidth) {
    return abbreviate(str, abbrevMarker, 0, maxWidth);
  }

  /**
   * <p>Abbreviates a String using a given replacement marker. This will turn
   * "Now is the time for all good men" into "...is the time for..." if "..." was defined
   * as the replacement marker.</p>
   *
   * <p>Works like {@code abbreviate(String, String, int)}, but allows you to specify
   * a "left edge" offset.  Note that this left edge is not necessarily going to
   * be the leftmost character in the result, or the first character following the
   * replacement marker, but it will appear somewhere in the result.
   *
   * <p>In no case will it return a String of length greater than {@code maxWidth}.</p>
   *
   * <pre>
   * StringUtils.abbreviate(null, null, *, *)                 = null
   * StringUtils.abbreviate("abcdefghijklmno", null, *, *)    = "abcdefghijklmno"
   * StringUtils.abbreviate("", "...", 0, 4)                  = ""
   * StringUtils.abbreviate("abcdefghijklmno", "---", -1, 10) = "abcdefg---"
   * StringUtils.abbreviate("abcdefghijklmno", ",", 0, 10)    = "abcdefghi,"
   * StringUtils.abbreviate("abcdefghijklmno", ",", 1, 10)    = "abcdefghi,"
   * StringUtils.abbreviate("abcdefghijklmno", ",", 2, 10)    = "abcdefghi,"
   * StringUtils.abbreviate("abcdefghijklmno", "::", 4, 10)   = "::efghij::"
   * StringUtils.abbreviate("abcdefghijklmno", "...", 6, 10)  = "...ghij..."
   * StringUtils.abbreviate("abcdefghijklmno", "*", 9, 10)    = "*ghijklmno"
   * StringUtils.abbreviate("abcdefghijklmno", "'", 10, 10)   = "'ghijklmno"
   * StringUtils.abbreviate("abcdefghijklmno", "!", 12, 10)   = "!ghijklmno"
   * StringUtils.abbreviate("abcdefghij", "abra", 0, 4)       = IllegalArgumentException
   * StringUtils.abbreviate("abcdefghij", "...", 5, 6)        = IllegalArgumentException
   * </pre>
   *
   * @param str          the String to check, may be null
   * @param abbrevMarker the String used as replacement marker
   * @param offset       left edge of source String
   * @param maxWidth     maximum length of result String, must be at least 4
   * @return abbreviated String, {@code null} if null String input
   * @throws IllegalArgumentException if the width is too small
   */
  public static String abbreviate(String str, String abbrevMarker, int offset, int maxWidth) {
    if (isNotEmpty(str) && EMPTY.equals(abbrevMarker) && maxWidth > 0) {
      return substring(str, 0, maxWidth);
    } else if (isAnyEmpty(str, abbrevMarker)) {
      return str;
    }
    int abbrevMarkerLength = abbrevMarker.length();
    int minAbbrevWidth = abbrevMarkerLength + 1;
    int minAbbrevWidthOffset = abbrevMarkerLength + abbrevMarkerLength + 1;

    if (maxWidth < minAbbrevWidth) {
      throw new IllegalArgumentException(String.format("Minimum abbreviation width is %d", minAbbrevWidth));
    }
    int strLen = str.length();
    if (strLen <= maxWidth) {
      return str;
    }
    if (offset > strLen) {
      offset = strLen;
    }
    if (strLen - offset < maxWidth - abbrevMarkerLength) {
      offset = strLen - (maxWidth - abbrevMarkerLength);
    }
    if (offset <= abbrevMarkerLength + 1) {
      return str.substring(0, maxWidth - abbrevMarkerLength) + abbrevMarker;
    }
    if (maxWidth < minAbbrevWidthOffset) {
      throw new IllegalArgumentException(String.format("Minimum abbreviation width with offset is %d", minAbbrevWidthOffset));
    }
    if (offset + maxWidth - abbrevMarkerLength < strLen) {
      return abbrevMarker + abbreviate(str.substring(offset), abbrevMarker, maxWidth - abbrevMarkerLength);
    }
    return abbrevMarker + str.substring(strLen - (maxWidth - abbrevMarkerLength));
  }

  /**
   * <p>Abbreviates a String to the length passed, replacing the middle characters with the supplied
   * replacement String.</p>
   *
   * <p>This abbreviation only occurs if the following criteria is met:</p>
   * <ul>
   * <li>Neither the String for abbreviation nor the replacement String are null or empty </li>
   * <li>The length to truncate to is less than the length of the supplied String</li>
   * <li>The length to truncate to is greater than 0</li>
   * <li>The abbreviated String will have enough room for the length supplied replacement String
   * and the first and last characters of the supplied String for abbreviation</li>
   * </ul>
   * <p>Otherwise, the returned String will be the same as the supplied String for abbreviation.
   * </p>
   *
   * <pre>
   * StringUtils.abbreviateMiddle(null, null, 0)      = null
   * StringUtils.abbreviateMiddle("abc", null, 0)      = "abc"
   * StringUtils.abbreviateMiddle("abc", ".", 0)      = "abc"
   * StringUtils.abbreviateMiddle("abc", ".", 3)      = "abc"
   * StringUtils.abbreviateMiddle("abcdef", ".", 4)     = "ab.f"
   * </pre>
   *
   * @param str    the String to abbreviate, may be null
   * @param middle the String to replace the middle characters with, may be null
   * @param length the length to abbreviate {@code str} to.
   * @return the abbreviated String if the above criteria is met, or the original String supplied for abbreviation.
   */
  public static String abbreviateMiddle(String str, String middle, int length) {
    if (isAnyEmpty(str, middle) || length >= str.length() || length < middle.length() + 2) {
      return str;
    }

    int targetSting = length - middle.length();
    int startOffset = targetSting / 2 + targetSting % 2;
    int endOffset = str.length() - targetSting / 2;

    return str.substring(0, startOffset) +
      middle +
      str.substring(endOffset);
  }

  /**
   * Appends the suffix to the end of the string if the string does not
   * already end with the suffix.
   *
   * @param str        The string.
   * @param suffix     The suffix to append to the end of the string.
   * @param ignoreCase Indicates whether the compare should ignore case.
   * @param suffixes   Additional suffixes that are valid terminators (optional).
   * @return A new String if suffix was appended, the same string otherwise.
   */
  private static String appendIfMissing(String str, CharSequence suffix, boolean ignoreCase, CharSequence... suffixes) {
    if (str == null || isEmpty(suffix) || endsWith(str, suffix, ignoreCase)) {
      return str;
    }
    if (ArrayUtils.isNotEmpty(suffixes)) {
      for (CharSequence s : suffixes) {
        if (endsWith(str, s, ignoreCase)) {
          return str;
        }
      }
    }
    return str + suffix;
  }

  /**
   * Appends the suffix to the end of the string if the string does not
   * already end with any of the suffixes.
   *
   * <pre>
   * StringUtils.appendIfMissing(null, null) = null
   * StringUtils.appendIfMissing("abc", null) = "abc"
   * StringUtils.appendIfMissing("", "xyz") = "xyz"
   * StringUtils.appendIfMissing("abc", "xyz") = "abcxyz"
   * StringUtils.appendIfMissing("abcxyz", "xyz") = "abcxyz"
   * StringUtils.appendIfMissing("abcXYZ", "xyz") = "abcXYZxyz"
   * </pre>
   * <p>With additional suffixes,</p>
   * <pre>
   * StringUtils.appendIfMissing(null, null, null) = null
   * StringUtils.appendIfMissing("abc", null, null) = "abc"
   * StringUtils.appendIfMissing("", "xyz", null) = "xyz"
   * StringUtils.appendIfMissing("abc", "xyz", new CharSequence[]{null}) = "abcxyz"
   * StringUtils.appendIfMissing("abc", "xyz", "") = "abc"
   * StringUtils.appendIfMissing("abc", "xyz", "mno") = "abcxyz"
   * StringUtils.appendIfMissing("abcxyz", "xyz", "mno") = "abcxyz"
   * StringUtils.appendIfMissing("abcmno", "xyz", "mno") = "abcmno"
   * StringUtils.appendIfMissing("abcXYZ", "xyz", "mno") = "abcXYZxyz"
   * StringUtils.appendIfMissing("abcMNO", "xyz", "mno") = "abcMNOxyz"
   * </pre>
   *
   * @param str      The string.
   * @param suffix   The suffix to append to the end of the string.
   * @param suffixes Additional suffixes that are valid terminators.
   * @return A new String if suffix was appended, the same string otherwise.
   */
  public static String appendIfMissing(String str, CharSequence suffix, CharSequence... suffixes) {
    return appendIfMissing(str, suffix, false, suffixes);
  }

  /**
   * Appends the suffix to the end of the string if the string does not
   * already end, case insensitive, with any of the suffixes.
   *
   * <pre>
   * StringUtils.appendIfMissingIgnoreCase(null, null) = null
   * StringUtils.appendIfMissingIgnoreCase("abc", null) = "abc"
   * StringUtils.appendIfMissingIgnoreCase("", "xyz") = "xyz"
   * StringUtils.appendIfMissingIgnoreCase("abc", "xyz") = "abcxyz"
   * StringUtils.appendIfMissingIgnoreCase("abcxyz", "xyz") = "abcxyz"
   * StringUtils.appendIfMissingIgnoreCase("abcXYZ", "xyz") = "abcXYZ"
   * </pre>
   * <p>With additional suffixes,</p>
   * <pre>
   * StringUtils.appendIfMissingIgnoreCase(null, null, null) = null
   * StringUtils.appendIfMissingIgnoreCase("abc", null, null) = "abc"
   * StringUtils.appendIfMissingIgnoreCase("", "xyz", null) = "xyz"
   * StringUtils.appendIfMissingIgnoreCase("abc", "xyz", new CharSequence[]{null}) = "abcxyz"
   * StringUtils.appendIfMissingIgnoreCase("abc", "xyz", "") = "abc"
   * StringUtils.appendIfMissingIgnoreCase("abc", "xyz", "mno") = "abcxyz"
   * StringUtils.appendIfMissingIgnoreCase("abcxyz", "xyz", "mno") = "abcxyz"
   * StringUtils.appendIfMissingIgnoreCase("abcmno", "xyz", "mno") = "abcmno"
   * StringUtils.appendIfMissingIgnoreCase("abcXYZ", "xyz", "mno") = "abcXYZ"
   * StringUtils.appendIfMissingIgnoreCase("abcMNO", "xyz", "mno") = "abcMNO"
   * </pre>
   *
   * @param str      The string.
   * @param suffix   The suffix to append to the end of the string.
   * @param suffixes Additional suffixes that are valid terminators.
   * @return A new String if suffix was appended, the same string otherwise.
   */
  public static String appendIfMissingIgnoreCase(String str, CharSequence suffix, CharSequence... suffixes) {
    return appendIfMissing(str, suffix, true, suffixes);
  }

  /**
   * <p>Centers a String in a larger String of size {@code size}
   * using the space character (' ').</p>
   *
   * <p>If the size is less than the String length, the original String is returned.
   * A {@code null} String returns {@code null}.
   * A negative size is treated as zero.</p>
   *
   * <p>Equivalent to {@code center(str, size, " ")}.</p>
   *
   * <pre>
   * StringUtils.center(null, *)   = null
   * StringUtils.center("", 4)     = "    "
   * StringUtils.center("ab", -1)  = "ab"
   * StringUtils.center("ab", 4)   = " ab "
   * StringUtils.center("abcd", 2) = "abcd"
   * StringUtils.center("a", 4)    = " a  "
   * </pre>
   *
   * @param str  the String to center, may be null
   * @param size the int size of new String, negative treated as zero
   * @return centered String, {@code null} if null String input
   */
  public static String center(String str, int size) {
    return center(str, size, ' ');
  }

  /**
   * <p>Centers a String in a larger String of size {@code size}.
   * Uses a supplied character as the value to pad the String with.</p>
   *
   * <p>If the size is less than the String length, the String is returned.
   * A {@code null} String returns {@code null}.
   * A negative size is treated as zero.</p>
   *
   * <pre>
   * StringUtils.center(null, *, *)     = null
   * StringUtils.center("", 4, ' ')     = "    "
   * StringUtils.center("ab", -1, ' ')  = "ab"
   * StringUtils.center("ab", 4, ' ')   = " ab "
   * StringUtils.center("abcd", 2, ' ') = "abcd"
   * StringUtils.center("a", 4, ' ')    = " a  "
   * StringUtils.center("a", 4, 'y')    = "yayy"
   * </pre>
   *
   * @param str     the String to center, may be null
   * @param size    the int size of new String, negative treated as zero
   * @param padChar the character to pad the new String with
   * @return centered String, {@code null} if null String input
   */
  public static String center(String str, int size, char padChar) {
    if (str == null || size <= 0) {
      return str;
    }
    int strLen = str.length();
    int pads = size - strLen;
    if (pads <= 0) {
      return str;
    }
    str = leftPad(str, strLen + pads / 2, padChar);
    str = rightPad(str, size, padChar);
    return str;
  }

  /**
   * <p>Centers a String in a larger String of size {@code size}.
   * Uses a supplied String as the value to pad the String with.</p>
   *
   * <p>If the size is less than the String length, the String is returned.
   * A {@code null} String returns {@code null}.
   * A negative size is treated as zero.</p>
   *
   * <pre>
   * StringUtils.center(null, *, *)     = null
   * StringUtils.center("", 4, " ")     = "    "
   * StringUtils.center("ab", -1, " ")  = "ab"
   * StringUtils.center("ab", 4, " ")   = " ab "
   * StringUtils.center("abcd", 2, " ") = "abcd"
   * StringUtils.center("a", 4, " ")    = " a  "
   * StringUtils.center("a", 4, "yz")   = "yayz"
   * StringUtils.center("abc", 7, null) = "  abc  "
   * StringUtils.center("abc", 7, "")   = "  abc  "
   * </pre>
   *
   * @param str    the String to center, may be null
   * @param size   the int size of new String, negative treated as zero
   * @param padStr the String to pad the new String with, must not be null or empty
   * @return centered String, {@code null} if null String input
   * @throws IllegalArgumentException if padStr is {@code null} or empty
   */
  public static String center(String str, int size, String padStr) {
    if (str == null || size <= 0) {
      return str;
    }
    if (isEmpty(padStr)) {
      padStr = SPACE;
    }
    int strLen = str.length();
    int pads = size - strLen;
    if (pads <= 0) {
      return str;
    }
    str = leftPad(str, strLen + pads / 2, padStr);
    str = rightPad(str, size, padStr);
    return str;
  }

  /**
   * <p>Compare two Strings lexicographically, as per {@link String#compareTo(String)}, returning :</p>
   * <ul>
   *  <li>{@code int = 0}, if {@code str1} is equal to {@code str2} (or both {@code null})</li>
   *  <li>{@code int < 0}, if {@code str1} is less than {@code str2}</li>
   *  <li>{@code int > 0}, if {@code str1} is greater than {@code str2}</li>
   * </ul>
   *
   * <p>This is a {@code null} safe version of :</p>
   * <blockquote><pre>str1.compareTo(str2)</pre></blockquote>
   *
   * <p>{@code null} value is considered less than non-{@code null} value.
   * Two {@code null} references are considered equal.</p>
   *
   * <pre>
   * StringUtils.compare(null, null)   = 0
   * StringUtils.compare(null , "a")   &lt; 0
   * StringUtils.compare("a", null)    &gt; 0
   * StringUtils.compare("abc", "abc") = 0
   * StringUtils.compare("a", "b")     &lt; 0
   * StringUtils.compare("b", "a")     &gt; 0
   * StringUtils.compare("a", "B")     &gt; 0
   * StringUtils.compare("ab", "abc")  &lt; 0
   * </pre>
   *
   * @param str1 the String to compare from
   * @param str2 the String to compare to
   * @return &lt; 0, 0, &gt; 0, if {@code str1} is respectively less, equal or greater than {@code str2}
   * @see #compare(String, String, boolean)
   * @see String#compareTo(String)
   */
  public static int compare(String str1, String str2) {
    return compare(str1, str2, true);
  }

  /**
   * <p>Compare two Strings lexicographically, as per {@link String#compareTo(String)}, returning :</p>
   * <ul>
   *  <li>{@code int = 0}, if {@code str1} is equal to {@code str2} (or both {@code null})</li>
   *  <li>{@code int < 0}, if {@code str1} is less than {@code str2}</li>
   *  <li>{@code int > 0}, if {@code str1} is greater than {@code str2}</li>
   * </ul>
   *
   * <p>This is a {@code null} safe version of :</p>
   * <blockquote><pre>str1.compareTo(str2)</pre></blockquote>
   *
   * <p>{@code null} inputs are handled according to the {@code nullIsLess} parameter.
   * Two {@code null} references are considered equal.</p>
   *
   * <pre>
   * StringUtils.compare(null, null, *)     = 0
   * StringUtils.compare(null , "a", true)  &lt; 0
   * StringUtils.compare(null , "a", false) &gt; 0
   * StringUtils.compare("a", null, true)   &gt; 0
   * StringUtils.compare("a", null, false)  &lt; 0
   * StringUtils.compare("abc", "abc", *)   = 0
   * StringUtils.compare("a", "b", *)       &lt; 0
   * StringUtils.compare("b", "a", *)       &gt; 0
   * StringUtils.compare("a", "B", *)       &gt; 0
   * StringUtils.compare("ab", "abc", *)    &lt; 0
   * </pre>
   *
   * @param str1       the String to compare from
   * @param str2       the String to compare to
   * @param nullIsLess whether consider {@code null} value less than non-{@code null} value
   * @return &lt; 0, 0, &gt; 0, if {@code str1} is respectively less, equal ou greater than {@code str2}
   * @see String#compareTo(String)
   */
  public static int compare(String str1, String str2, boolean nullIsLess) {
    //noinspection StringEquality
    if (str1 == str2) { // NOSONARLINT this intentionally uses == to allow for both null
      return 0;
    }
    if (str1 == null) {
      return nullIsLess ? -1 : 1;
    }
    if (str2 == null) {
      return nullIsLess ? 1 : -1;
    }
    return str1.compareTo(str2);
  }

  /**
   * <p>Compare two Strings lexicographically, ignoring case differences,
   * as per {@link String#compareToIgnoreCase(String)}, returning :</p>
   * <ul>
   *  <li>{@code int = 0}, if {@code str1} is equal to {@code str2} (or both {@code null})</li>
   *  <li>{@code int < 0}, if {@code str1} is less than {@code str2}</li>
   *  <li>{@code int > 0}, if {@code str1} is greater than {@code str2}</li>
   * </ul>
   *
   * <p>This is a {@code null} safe version of :</p>
   * <blockquote><pre>str1.compareToIgnoreCase(str2)</pre></blockquote>
   *
   * <p>{@code null} value is considered less than non-{@code null} value.
   * Two {@code null} references are considered equal.
   * Comparison is case insensitive.</p>
   *
   * <pre>
   * StringUtils.compareIgnoreCase(null, null)   = 0
   * StringUtils.compareIgnoreCase(null , "a")   &lt; 0
   * StringUtils.compareIgnoreCase("a", null)    &gt; 0
   * StringUtils.compareIgnoreCase("abc", "abc") = 0
   * StringUtils.compareIgnoreCase("abc", "ABC") = 0
   * StringUtils.compareIgnoreCase("a", "b")     &lt; 0
   * StringUtils.compareIgnoreCase("b", "a")     &gt; 0
   * StringUtils.compareIgnoreCase("a", "B")     &lt; 0
   * StringUtils.compareIgnoreCase("A", "b")     &lt; 0
   * StringUtils.compareIgnoreCase("ab", "ABC")  &lt; 0
   * </pre>
   *
   * @param str1 the String to compare from
   * @param str2 the String to compare to
   * @return &lt; 0, 0, &gt; 0, if {@code str1} is respectively less, equal ou greater than {@code str2},
   * ignoring case differences.
   * @see #compareIgnoreCase(String, String, boolean)
   * @see String#compareToIgnoreCase(String)
   */
  public static int compareIgnoreCase(String str1, String str2) {
    return compareIgnoreCase(str1, str2, true);
  }

  /**
   * <p>Compare two Strings lexicographically, ignoring case differences,
   * as per {@link String#compareToIgnoreCase(String)}, returning :</p>
   * <ul>
   *  <li>{@code int = 0}, if {@code str1} is equal to {@code str2} (or both {@code null})</li>
   *  <li>{@code int < 0}, if {@code str1} is less than {@code str2}</li>
   *  <li>{@code int > 0}, if {@code str1} is greater than {@code str2}</li>
   * </ul>
   *
   * <p>This is a {@code null} safe version of :</p>
   * <blockquote><pre>str1.compareToIgnoreCase(str2)</pre></blockquote>
   *
   * <p>{@code null} inputs are handled according to the {@code nullIsLess} parameter.
   * Two {@code null} references are considered equal.
   * Comparison is case insensitive.</p>
   *
   * <pre>
   * StringUtils.compareIgnoreCase(null, null, *)     = 0
   * StringUtils.compareIgnoreCase(null , "a", true)  &lt; 0
   * StringUtils.compareIgnoreCase(null , "a", false) &gt; 0
   * StringUtils.compareIgnoreCase("a", null, true)   &gt; 0
   * StringUtils.compareIgnoreCase("a", null, false)  &lt; 0
   * StringUtils.compareIgnoreCase("abc", "abc", *)   = 0
   * StringUtils.compareIgnoreCase("abc", "ABC", *)   = 0
   * StringUtils.compareIgnoreCase("a", "b", *)       &lt; 0
   * StringUtils.compareIgnoreCase("b", "a", *)       &gt; 0
   * StringUtils.compareIgnoreCase("a", "B", *)       &lt; 0
   * StringUtils.compareIgnoreCase("A", "b", *)       &lt; 0
   * StringUtils.compareIgnoreCase("ab", "abc", *)    &lt; 0
   * </pre>
   *
   * @param str1       the String to compare from
   * @param str2       the String to compare to
   * @param nullIsLess whether consider {@code null} value less than non-{@code null} value
   * @return &lt; 0, 0, &gt; 0, if {@code str1} is respectively less, equal ou greater than {@code str2},
   * ignoring case differences.
   * @see String#compareToIgnoreCase(String)
   */
  public static int compareIgnoreCase(String str1, String str2, boolean nullIsLess) {
    //noinspection StringEquality
    if (str1 == str2) { // NOSONARLINT this intentionally uses == to allow for both null
      return 0;
    }
    if (str1 == null) {
      return nullIsLess ? -1 : 1;
    }
    if (str2 == null) {
      return nullIsLess ? 1 : -1;
    }
    return str1.compareToIgnoreCase(str2);
  }

  /**
   * <p>Checks if CharSequence contains a search CharSequence, handling {@code null}.
   * This method uses {@link String#indexOf(String)} if possible.</p>
   *
   * <p>A {@code null} CharSequence will return {@code false}.</p>
   *
   * <pre>
   * StringUtils.contains(null, *)     = false
   * StringUtils.contains(*, null)     = false
   * StringUtils.contains("", "")      = true
   * StringUtils.contains("abc", "")   = true
   * StringUtils.contains("abc", "a")  = true
   * StringUtils.contains("abc", "z")  = false
   * </pre>
   *
   * @param seq       the CharSequence to check, may be null
   * @param searchSeq the CharSequence to find, may be null
   * @return true if the CharSequence contains the search CharSequence,
   * false if not or {@code null} string input
   */
  public static boolean contains(CharSequence seq, CharSequence searchSeq) {
    if (seq == null || searchSeq == null) {
      return false;
    }
    return CharSequenceUtils.indexOf(seq, searchSeq, 0) >= 0;
  }

  /**
   * <p>Checks if CharSequence contains a search character, handling {@code null}.
   * This method uses {@link String#indexOf(int)} if possible.</p>
   *
   * <p>A {@code null} or empty ("") CharSequence will return {@code false}.</p>
   *
   * <pre>
   * StringUtils.contains(null, *)    = false
   * StringUtils.contains("", *)      = false
   * StringUtils.contains("abc", 'a') = true
   * StringUtils.contains("abc", 'z') = false
   * </pre>
   *
   * @param seq        the CharSequence to check, may be null
   * @param searchChar the character to find
   * @return true if the CharSequence contains the search character,
   * false if not or {@code null} string input
   */
  public static boolean contains(CharSequence seq, int searchChar) {
    if (isEmpty(seq)) {
      return false;
    }
    return CharSequenceUtils.indexOf(seq, searchChar, 0) >= 0;
  }

  /**
   * <p>Checks if the CharSequence contains any character in the given
   * set of characters.</p>
   *
   * <p>A {@code null} CharSequence will return {@code false}.
   * A {@code null} or zero length search array will return {@code false}.</p>
   *
   * <pre>
   * StringUtils.containsAny(null, *)                  = false
   * StringUtils.containsAny("", *)                    = false
   * StringUtils.containsAny(*, null)                  = false
   * StringUtils.containsAny(*, [])                    = false
   * StringUtils.containsAny("zzabyycdxx", ['z', 'a']) = true
   * StringUtils.containsAny("zzabyycdxx", ['b', 'y']) = true
   * StringUtils.containsAny("zzabyycdxx", ['z', 'y']) = true
   * StringUtils.containsAny("aba", ['z'])             = false
   * </pre>
   *
   * @param cs          the CharSequence to check, may be null
   * @param searchChars the chars to search for, may be null
   * @return the {@code true} if any of the chars are found,
   * {@code false} if no match or null input
   */
  public static boolean containsAny(CharSequence cs, char... searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
      return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    int csLast = csLength - 1;
    int searchLast = searchLength - 1;
    for (int i = 0; i < csLength; i++) {
      char ch = cs.charAt(i);
      for (int j = 0; j < searchLength; j++) {
        if (searchChars[j] == ch) {
          if (Character.isHighSurrogate(ch)) {
            if (j == searchLast) {
              // missing low surrogate, fine, like String.indexOf(String)
              return true;
            }
            if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
              return true;
            }
          } else {
            // ch is in the Basic Multilingual Plane
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * <p>
   * Checks if the CharSequence contains any character in the given set of characters.
   * </p>
   *
   * <p>
   * A {@code null} CharSequence will return {@code false}. A {@code null} search CharSequence will return
   * {@code false}.
   * </p>
   *
   * <pre>
   * StringUtils.containsAny(null, *)               = false
   * StringUtils.containsAny("", *)                 = false
   * StringUtils.containsAny(*, null)               = false
   * StringUtils.containsAny(*, "")                 = false
   * StringUtils.containsAny("zzabyycdxx", "za")    = true
   * StringUtils.containsAny("zzabyycdxx", "by")    = true
   * StringUtils.containsAny("zzabyycdxx", "zy")    = true
   * StringUtils.containsAny("zzabyycdxx", "\tx")   = true
   * StringUtils.containsAny("zzabyycdxx", "$.#yF") = true
   * StringUtils.containsAny("aba", "z")            = false
   * </pre>
   *
   * @param cs          the CharSequence to check, may be null
   * @param searchChars the chars to search for, may be null
   * @return the {@code true} if any of the chars are found, {@code false} if no match or null input
   */
  public static boolean containsAny(CharSequence cs, CharSequence searchChars) {
    if (searchChars == null) {
      return false;
    }
    return containsAny(cs, CharSequenceUtils.toCharArray(searchChars));
  }

  /**
   * <p>Checks if CharSequence contains a search CharSequence irrespective of case,
   * handling {@code null}. Case-insensitivity is defined as by
   * {@link String#equalsIgnoreCase(String)}.
   *
   * <p>A {@code null} CharSequence will return {@code false}.</p>
   *
   * <pre>
   * StringUtils.containsIgnoreCase(null, *) = false
   * StringUtils.containsIgnoreCase(*, null) = false
   * StringUtils.containsIgnoreCase("", "") = true
   * StringUtils.containsIgnoreCase("abc", "") = true
   * StringUtils.containsIgnoreCase("abc", "a") = true
   * StringUtils.containsIgnoreCase("abc", "z") = false
   * StringUtils.containsIgnoreCase("abc", "A") = true
   * StringUtils.containsIgnoreCase("abc", "Z") = false
   * </pre>
   *
   * @param str       the CharSequence to check, may be null
   * @param searchStr the CharSequence to find, may be null
   * @return true if the CharSequence contains the search CharSequence irrespective of
   * case or false if not or {@code null} string input
   */
  public static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
    if (str == null || searchStr == null) {
      return false;
    }
    int len = searchStr.length();
    int max = str.length() - len;
    for (int i = 0; i <= max; i++) {
      if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, len)) {
        return true;
      }
    }
    return false;
  }

  /**
   * <p>Checks that the CharSequence does not contain certain characters.</p>
   *
   * <p>A {@code null} CharSequence will return {@code true}.
   * A {@code null} invalid character array will return {@code true}.
   * An empty CharSequence (length()=0) always returns true.</p>
   *
   * <pre>
   * StringUtils.containsNone(null, *)       = true
   * StringUtils.containsNone(*, null)       = true
   * StringUtils.containsNone("", *)         = true
   * StringUtils.containsNone("ab", '')      = true
   * StringUtils.containsNone("abab", 'xyz') = true
   * StringUtils.containsNone("ab1", 'xyz')  = true
   * StringUtils.containsNone("abz", 'xyz')  = false
   * </pre>
   *
   * @param cs          the CharSequence to check, may be null
   * @param searchChars an array of invalid chars, may be null
   * @return true if it contains none of the invalid chars, or is null
   */
  public static boolean containsNone(CharSequence cs, char... searchChars) {
    if (cs == null || searchChars == null) {
      return true;
    }
    int csLen = cs.length();
    int csLast = csLen - 1;
    int searchLen = searchChars.length;
    int searchLast = searchLen - 1;
    for (int i = 0; i < csLen; i++) {
      char ch = cs.charAt(i);
      for (int j = 0; j < searchLen; j++) {
        if (searchChars[j] == ch) {
          if (Character.isHighSurrogate(ch)) {
            if (j == searchLast) {
              // missing low surrogate, fine, like String.indexOf(String)
              return false;
            }
            if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
              return false;
            }
          } else {
            // ch is in the Basic Multilingual Plane
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * <p>Checks that the CharSequence does not contain certain characters.</p>
   *
   * <p>A {@code null} CharSequence will return {@code true}.
   * A {@code null} invalid character array will return {@code true}.
   * An empty String ("") always returns true.</p>
   *
   * <pre>
   * StringUtils.containsNone(null, *)       = true
   * StringUtils.containsNone(*, null)       = true
   * StringUtils.containsNone("", *)         = true
   * StringUtils.containsNone("ab", "")      = true
   * StringUtils.containsNone("abab", "xyz") = true
   * StringUtils.containsNone("ab1", "xyz")  = true
   * StringUtils.containsNone("abz", "xyz")  = false
   * </pre>
   *
   * @param cs           the CharSequence to check, may be null
   * @param invalidChars a String of invalid chars, may be null
   * @return true if it contains none of the invalid chars, or is null
   */
  public static boolean containsNone(CharSequence cs, String invalidChars) {
    if (invalidChars == null) {
      return true;
    }
    return containsNone(cs, invalidChars.toCharArray());
  }

  /**
   * <p>Checks if the CharSequence contains only certain characters.</p>
   *
   * <p>A {@code null} CharSequence will return {@code false}.
   * A {@code null} valid character array will return {@code false}.
   * An empty CharSequence (length()=0) always returns {@code true}.</p>
   *
   * <pre>
   * StringUtils.containsOnly(null, *)       = false
   * StringUtils.containsOnly(*, null)       = false
   * StringUtils.containsOnly("", *)         = true
   * StringUtils.containsOnly("ab", '')      = false
   * StringUtils.containsOnly("abab", 'abc') = true
   * StringUtils.containsOnly("ab1", 'abc')  = false
   * StringUtils.containsOnly("abz", 'abc')  = false
   * </pre>
   *
   * @param cs    the String to check, may be null
   * @param valid an array of valid chars, may be null
   * @return true if it only contains valid chars and is non-null
   */
  public static boolean containsOnly(CharSequence cs, char... valid) {
    // All these pre-checks are to maintain API with an older version
    if (valid == null || cs == null) {
      return false;
    }
    if (cs.length() == 0) {
      return true;
    }
    if (valid.length == 0) {
      return false;
    }
    return indexOfAnyBut(cs, valid) == INDEX_NOT_FOUND;
  }

  /**
   * <p>Checks if the CharSequence contains only certain characters.</p>
   *
   * <p>A {@code null} CharSequence will return {@code false}.
   * A {@code null} valid character String will return {@code false}.
   * An empty String (length()=0) always returns {@code true}.</p>
   *
   * <pre>
   * StringUtils.containsOnly(null, *)       = false
   * StringUtils.containsOnly(*, null)       = false
   * StringUtils.containsOnly("", *)         = true
   * StringUtils.containsOnly("ab", "")      = false
   * StringUtils.containsOnly("abab", "abc") = true
   * StringUtils.containsOnly("ab1", "abc")  = false
   * StringUtils.containsOnly("abz", "abc")  = false
   * </pre>
   *
   * @param cs         the CharSequence to check, may be null
   * @param validChars a String of valid chars, may be null
   * @return true if it only contains valid chars and is non-null
   */
  public static boolean containsOnly(CharSequence cs, String validChars) {
    if (cs == null || validChars == null) {
      return false;
    }
    return containsOnly(cs, validChars.toCharArray());
  }

  /**
   * <p>Check whether the given CharSequence contains any whitespace characters.</p>
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * @param seq the CharSequence to check (may be {@code null})
   * @return {@code true} if the CharSequence is not empty and
   * contains at least 1 (breaking) whitespace character
   */
  // From org.springframework.util.StringUtils, under Apache License 2.0
  public static boolean containsWhitespace(CharSequence seq) {
    if (isEmpty(seq)) {
      return false;
    }
    int strLen = seq.length();
    for (int i = 0; i < strLen; i++) {
      if (Character.isWhitespace(seq.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  private static void convertRemainingAccentCharacters(StringBuilder decomposed) {
    for (int i = 0; i < decomposed.length(); i++) {
      if (decomposed.charAt(i) == '\u0141') {
        decomposed.setCharAt(i, 'L');
      } else if (decomposed.charAt(i) == '\u0142') {
        decomposed.setCharAt(i, 'l');
      }
    }
  }

  /**
   * <p>Counts how many times the char appears in the given string.</p>
   *
   * <p>A {@code null} or empty ("") String input returns {@code 0}.</p>
   *
   * <pre>
   * StringUtils.countMatches(null, *)       = 0
   * StringUtils.countMatches("", *)         = 0
   * StringUtils.countMatches("abba", 0)  = 0
   * StringUtils.countMatches("abba", 'a')   = 2
   * StringUtils.countMatches("abba", 'b')  = 2
   * StringUtils.countMatches("abba", 'x') = 0
   * </pre>
   *
   * @param str the CharSequence to check, may be null
   * @param ch  the char to count
   * @return the number of occurrences, 0 if the CharSequence is {@code null}
   */
  public static int countMatches(CharSequence str, char ch) {
    if (isEmpty(str)) {
      return 0;
    }
    int count = 0;
    // We could also call str.toCharArray() for faster look ups but that would generate more garbage.
    for (int i = 0; i < str.length(); i++) {
      if (ch == str.charAt(i)) {
        count++;
      }
    }
    return count;
  }

  /**
   * <p>Counts how many times the substring appears in the larger string.
   * Note that the code only counts non-overlapping matches.</p>
   *
   * <p>A {@code null} or empty ("") String input returns {@code 0}.</p>
   *
   * <pre>
   * StringUtils.countMatches(null, *)       = 0
   * StringUtils.countMatches("", *)         = 0
   * StringUtils.countMatches("abba", null)  = 0
   * StringUtils.countMatches("abba", "")    = 0
   * StringUtils.countMatches("abba", "a")   = 2
   * StringUtils.countMatches("abba", "ab")  = 1
   * StringUtils.countMatches("abba", "xxx") = 0
   * StringUtils.countMatches("ababa", "aba") = 1
   * </pre>
   *
   * @param str the CharSequence to check, may be null
   * @param sub the substring to count, may be null
   * @return the number of occurrences, 0 if either CharSequence is {@code null}
   */
  public static int countMatches(CharSequence str, CharSequence sub) {
    if (isEmpty(str) || isEmpty(sub)) {
      return 0;
    }
    int count = 0;
    int idx = 0;
    while ((idx = CharSequenceUtils.indexOf(str, sub, idx)) != INDEX_NOT_FOUND) {
      count++;
      idx += sub.length();
    }
    return count;
  }

  /**
   * <p>Deletes all whitespaces from a String as defined by
   * {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.deleteWhitespace(null)         = null
   * StringUtils.deleteWhitespace("")           = ""
   * StringUtils.deleteWhitespace("abc")        = "abc"
   * StringUtils.deleteWhitespace("   ab  c  ") = "abc"
   * </pre>
   *
   * @param str the String to delete whitespace from, may be null
   * @return the String without whitespaces, {@code null} if null String input
   */
  public static String deleteWhitespace(String str) {
    if (isEmpty(str)) {
      return str;
    }
    int sz = str.length();
    char[] chs = new char[sz];
    int count = 0;
    for (int i = 0; i < sz; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        chs[count++] = str.charAt(i);
      }
    }
    if (count == sz) {
      return str;
    }
    if (count == 0) {
      return EMPTY;
    }
    return new String(chs, 0, count);
  }

  /**
   * <p>Compares two Strings, and returns the portion where they differ.
   * More precisely, return the remainder of the second String,
   * starting from where it's different from the first. This means that
   * the difference between "abc" and "ab" is the empty String and not "c". </p>
   *
   * <p>For example,
   * {@code difference("i am a machine", "i am a robot") -> "robot"}.</p>
   *
   * <pre>
   * StringUtils.difference(null, null) = null
   * StringUtils.difference("", "") = ""
   * StringUtils.difference("", "abc") = "abc"
   * StringUtils.difference("abc", "") = ""
   * StringUtils.difference("abc", "abc") = ""
   * StringUtils.difference("abc", "ab") = ""
   * StringUtils.difference("ab", "abxyz") = "xyz"
   * StringUtils.difference("abcde", "abxyz") = "xyz"
   * StringUtils.difference("abcde", "xyz") = "xyz"
   * </pre>
   *
   * @param str1 the first String, may be null
   * @param str2 the second String, may be null
   * @return the portion of str2 where it differs from str1; returns the
   * empty String if they are equal
   * @see #indexOfDifference(CharSequence, CharSequence)
   */
  public static String difference(String str1, String str2) {
    if (str1 == null) {
      return str2;
    }
    if (str2 == null) {
      return str1;
    }
    int at = indexOfDifference(str1, str2);
    if (at == INDEX_NOT_FOUND) {
      return EMPTY;
    }
    return str2.substring(at);
  }

  /**
   * <p>Check if a CharSequence ends with a specified suffix.</p>
   *
   * <p>{@code null}s are handled without exceptions. Two {@code null}
   * references are considered to be equal. The comparison is case sensitive.</p>
   *
   * <pre>
   * StringUtils.endsWith(null, null)      = true
   * StringUtils.endsWith(null, "def")     = false
   * StringUtils.endsWith("abcdef", null)  = false
   * StringUtils.endsWith("abcdef", "def") = true
   * StringUtils.endsWith("ABCDEF", "def") = false
   * StringUtils.endsWith("ABCDEF", "cde") = false
   * StringUtils.endsWith("ABCDEF", "")    = true
   * </pre>
   *
   * @param str    the CharSequence to check, may be null
   * @param suffix the suffix to find, may be null
   * @return {@code true} if the CharSequence ends with the suffix, case sensitive, or
   * both {@code null}
   * @see String#endsWith(String)
   */
  public static boolean endsWith(CharSequence str, CharSequence suffix) {
    return endsWith(str, suffix, false);
  }

  /**
   * <p>Check if a CharSequence ends with a specified suffix (optionally case insensitive).</p>
   *
   * @param str        the CharSequence to check, may be null
   * @param suffix     the suffix to find, may be null
   * @param ignoreCase indicates whether the compare should ignore case
   *                   (case insensitive) or not.
   * @return {@code true} if the CharSequence starts with the prefix or
   * both {@code null}
   * @see String#endsWith(String)
   */
  private static boolean endsWith(CharSequence str, CharSequence suffix, boolean ignoreCase) {
    if (str == null || suffix == null) {
      return str == suffix;
    }
    if (suffix.length() > str.length()) {
      return false;
    }
    int strOffset = str.length() - suffix.length();
    return CharSequenceUtils.regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
  }

  /**
   * <p>Check if a CharSequence ends with any of the provided case-sensitive suffixes.</p>
   *
   * <pre>
   * StringUtils.endsWithAny(null, null)      = false
   * StringUtils.endsWithAny(null, new String[] {"abc"})  = false
   * StringUtils.endsWithAny("abcxyz", null)     = false
   * StringUtils.endsWithAny("abcxyz", new String[] {""}) = true
   * StringUtils.endsWithAny("abcxyz", new String[] {"xyz"}) = true
   * StringUtils.endsWithAny("abcxyz", new String[] {null, "xyz", "abc"}) = true
   * StringUtils.endsWithAny("abcXYZ", "def", "XYZ") = true
   * StringUtils.endsWithAny("abcXYZ", "def", "xyz") = false
   * </pre>
   *
   * @param sequence      the CharSequence to check, may be null
   * @param searchStrings the case-sensitive CharSequences to find, may be empty or contain {@code null}
   * @return {@code true} if the input {@code sequence} is {@code null} AND no {@code searchStrings} are provided, or
   * the input {@code sequence} ends in any of the provided case-sensitive {@code searchStrings}.
   * @see StringUtils#endsWith(CharSequence, CharSequence)
   */
  public static boolean endsWithAny(CharSequence sequence, CharSequence... searchStrings) {
    if (isEmpty(sequence) || ArrayUtils.isEmpty(searchStrings)) {
      return false;
    }
    for (CharSequence searchString : searchStrings) {
      if (endsWith(sequence, searchString)) {
        return true;
      }
    }
    return false;
  }

  /**
   * <p>Case insensitive check if a CharSequence ends with a specified suffix.</p>
   *
   * <p>{@code null}s are handled without exceptions. Two {@code null}
   * references are considered to be equal. The comparison is case insensitive.</p>
   *
   * <pre>
   * StringUtils.endsWithIgnoreCase(null, null)      = true
   * StringUtils.endsWithIgnoreCase(null, "def")     = false
   * StringUtils.endsWithIgnoreCase("abcdef", null)  = false
   * StringUtils.endsWithIgnoreCase("abcdef", "def") = true
   * StringUtils.endsWithIgnoreCase("ABCDEF", "def") = true
   * StringUtils.endsWithIgnoreCase("ABCDEF", "cde") = false
   * </pre>
   *
   * @param str    the CharSequence to check, may be null
   * @param suffix the suffix to find, may be null
   * @return {@code true} if the CharSequence ends with the suffix, case insensitive, or
   * both {@code null}
   * @see String#endsWith(String)
   */
  public static boolean endsWithIgnoreCase(CharSequence str, CharSequence suffix) {
    return endsWith(str, suffix, true);
  }

  /**
   * <p>Compares two CharSequences, returning {@code true} if they represent
   * equal sequences of characters.</p>
   *
   * <p>{@code null}s are handled without exceptions. Two {@code null}
   * references are considered to be equal. The comparison is <strong>case sensitive</strong>.</p>
   *
   * <pre>
   * StringUtils.equals(null, null)   = true
   * StringUtils.equals(null, "abc")  = false
   * StringUtils.equals("abc", null)  = false
   * StringUtils.equals("abc", "abc") = true
   * StringUtils.equals("abc", "ABC") = false
   * </pre>
   *
   * @param cs1 the first CharSequence, may be {@code null}
   * @param cs2 the second CharSequence, may be {@code null}
   * @return {@code true} if the CharSequences are equal (case-sensitive), or both {@code null}
   * @see Object#equals(Object)
   * @see #equalsIgnoreCase(CharSequence, CharSequence)
   */
  public static boolean equals(CharSequence cs1, CharSequence cs2) {
    if (cs1 == cs2) {
      return true;
    }
    if (cs1 == null || cs2 == null) {
      return false;
    }
    if (cs1.length() != cs2.length()) {
      return false;
    }
    if (cs1 instanceof String && cs2 instanceof String) {
      return cs1.equals(cs2);
    }
    // Step-wise comparison
    int length = cs1.length();
    for (int i = 0; i < length; i++) {
      if (cs1.charAt(i) != cs2.charAt(i)) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>Compares given {@code string} to a CharSequences vararg of {@code searchStrings},
   * returning {@code true} if the {@code string} is equal to any of the {@code searchStrings}.</p>
   *
   * <pre>
   * StringUtils.equalsAny(null, (CharSequence[]) null) = false
   * StringUtils.equalsAny(null, null, null)    = true
   * StringUtils.equalsAny(null, "abc", "def")  = false
   * StringUtils.equalsAny("abc", null, "def")  = false
   * StringUtils.equalsAny("abc", "abc", "def") = true
   * StringUtils.equalsAny("abc", "ABC", "DEF") = false
   * </pre>
   *
   * @param string        to compare, may be {@code null}.
   * @param searchStrings a vararg of strings, may be {@code null}.
   * @return {@code true} if the string is equal (case-sensitive) to any other element of {@code searchStrings};
   * {@code false} if {@code searchStrings} is null or contains no matches.
   */
  public static boolean equalsAny(CharSequence string, CharSequence... searchStrings) {
    if (ArrayUtils.isNotEmpty(searchStrings)) {
      for (CharSequence next : searchStrings) {
        if (equals(string, next)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * <p>Compares given {@code string} to a CharSequences vararg of {@code searchStrings},
   * returning {@code true} if the {@code string} is equal to any of the {@code searchStrings}, ignoring case.</p>
   *
   * <pre>
   * StringUtils.equalsAnyIgnoreCase(null, (CharSequence[]) null) = false
   * StringUtils.equalsAnyIgnoreCase(null, null, null)    = true
   * StringUtils.equalsAnyIgnoreCase(null, "abc", "def")  = false
   * StringUtils.equalsAnyIgnoreCase("abc", null, "def")  = false
   * StringUtils.equalsAnyIgnoreCase("abc", "abc", "def") = true
   * StringUtils.equalsAnyIgnoreCase("abc", "ABC", "DEF") = true
   * </pre>
   *
   * @param string        to compare, may be {@code null}.
   * @param searchStrings a vararg of strings, may be {@code null}.
   * @return {@code true} if the string is equal (case-insensitive) to any other element of {@code searchStrings};
   * {@code false} if {@code searchStrings} is null or contains no matches.
   */
  public static boolean equalsAnyIgnoreCase(CharSequence string, CharSequence... searchStrings) {
    if (ArrayUtils.isNotEmpty(searchStrings)) {
      for (CharSequence next : searchStrings) {
        if (equalsIgnoreCase(string, next)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * <p>Compares two CharSequences, returning {@code true} if they represent
   * equal sequences of characters, ignoring case.</p>
   *
   * <p>{@code null}s are handled without exceptions. Two {@code null}
   * references are considered equal. The comparison is <strong>case insensitive</strong>.</p>
   *
   * <pre>
   * StringUtils.equalsIgnoreCase(null, null)   = true
   * StringUtils.equalsIgnoreCase(null, "abc")  = false
   * StringUtils.equalsIgnoreCase("abc", null)  = false
   * StringUtils.equalsIgnoreCase("abc", "abc") = true
   * StringUtils.equalsIgnoreCase("abc", "ABC") = true
   * </pre>
   *
   * @param cs1 the first CharSequence, may be {@code null}
   * @param cs2 the second CharSequence, may be {@code null}
   * @return {@code true} if the CharSequences are equal (case-insensitive), or both {@code null}
   * @see #equals(CharSequence, CharSequence)
   */
  public static boolean equalsIgnoreCase(CharSequence cs1, CharSequence cs2) {
    if (cs1 == cs2) {
      return true;
    }
    if (cs1 == null || cs2 == null) {
      return false;
    }
    if (cs1.length() != cs2.length()) {
      return false;
    }
    return CharSequenceUtils.regionMatches(cs1, true, 0, cs2, 0, cs1.length());
  }

  /**
   * <p>Returns the first value in the array which is not empty (""),
   * {@code null} or whitespace only.</p>
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <p>If all values are blank or the array is {@code null}
   * or empty then {@code null} is returned.</p>
   *
   * <pre>
   * StringUtils.firstNonBlank(null, null, null)     = null
   * StringUtils.firstNonBlank(null, "", " ")        = null
   * StringUtils.firstNonBlank("abc")                = "abc"
   * StringUtils.firstNonBlank(null, "xyz")          = "xyz"
   * StringUtils.firstNonBlank(null, "", " ", "xyz") = "xyz"
   * StringUtils.firstNonBlank(null, "xyz", "abc")   = "xyz"
   * StringUtils.firstNonBlank()                     = null
   * </pre>
   *
   * @param <T>    the specific kind of CharSequence
   * @param values the values to test, may be {@code null} or empty
   * @return the first value from {@code values} which is not blank,
   * or {@code null} if there are no non-blank values
   */
  @SafeVarargs
  public static <T extends CharSequence> T firstNonBlank(T... values) {
    if (values != null) {
      for (T val : values) {
        if (isNotBlank(val)) {
          return val;
        }
      }
    }
    return null;
  }

  /**
   * <p>Returns the first value in the array which is not empty.</p>
   *
   * <p>If all values are empty or the array is {@code null}
   * or empty then {@code null} is returned.</p>
   *
   * <pre>
   * StringUtils.firstNonEmpty(null, null, null)   = null
   * StringUtils.firstNonEmpty(null, null, "")     = null
   * StringUtils.firstNonEmpty(null, "", " ")      = " "
   * StringUtils.firstNonEmpty("abc")              = "abc"
   * StringUtils.firstNonEmpty(null, "xyz")        = "xyz"
   * StringUtils.firstNonEmpty("", "xyz")          = "xyz"
   * StringUtils.firstNonEmpty(null, "xyz", "abc") = "xyz"
   * StringUtils.firstNonEmpty()                   = null
   * </pre>
   *
   * @param <T>    the specific kind of CharSequence
   * @param values the values to test, may be {@code null} or empty
   * @return the first value from {@code values} which is not empty,
   * or {@code null} if there are no non-empty values
   */
  @SafeVarargs
  public static <T extends CharSequence> T firstNonEmpty(T... values) {
    if (values != null) {
      for (T val : values) {
        if (isNotEmpty(val)) {
          return val;
        }
      }
    }
    return null;
  }

  /**
   * <p>Compares all Strings in an array and returns the initial sequence of
   * characters that is common to all of them.</p>
   *
   * <p>For example,
   * {@code getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) -&gt; "i am a "}</p>
   *
   * <pre>
   * StringUtils.getCommonPrefix(null) = ""
   * StringUtils.getCommonPrefix(new String[] {}) = ""
   * StringUtils.getCommonPrefix(new String[] {"abc"}) = "abc"
   * StringUtils.getCommonPrefix(new String[] {null, null}) = ""
   * StringUtils.getCommonPrefix(new String[] {"", ""}) = ""
   * StringUtils.getCommonPrefix(new String[] {"", null}) = ""
   * StringUtils.getCommonPrefix(new String[] {"abc", null, null}) = ""
   * StringUtils.getCommonPrefix(new String[] {null, null, "abc"}) = ""
   * StringUtils.getCommonPrefix(new String[] {"", "abc"}) = ""
   * StringUtils.getCommonPrefix(new String[] {"abc", ""}) = ""
   * StringUtils.getCommonPrefix(new String[] {"abc", "abc"}) = "abc"
   * StringUtils.getCommonPrefix(new String[] {"abc", "a"}) = "a"
   * StringUtils.getCommonPrefix(new String[] {"ab", "abxyz"}) = "ab"
   * StringUtils.getCommonPrefix(new String[] {"abcde", "abxyz"}) = "ab"
   * StringUtils.getCommonPrefix(new String[] {"abcde", "xyz"}) = ""
   * StringUtils.getCommonPrefix(new String[] {"xyz", "abcde"}) = ""
   * StringUtils.getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) = "i am a "
   * </pre>
   *
   * @param strs array of String objects, entries may be null
   * @return the initial sequence of characters that are common to all Strings
   * in the array; empty String if the array is null, the elements are all null
   * or if there is no common prefix.
   */
  public static String getCommonPrefix(String... strs) {
    if (ArrayUtils.isEmpty(strs)) {
      return EMPTY;
    }
    int smallestIndexOfDiff = indexOfDifference(strs);
    if (smallestIndexOfDiff == INDEX_NOT_FOUND) {
      // all strings were identical
      if (strs[0] == null) {
        return EMPTY;
      }
      return strs[0];
    } else if (smallestIndexOfDiff == 0) {
      // there were no common initial characters
      return EMPTY;
    } else {
      // we found a common initial character sequence
      return strs[0].substring(0, smallestIndexOfDiff);
    }
  }

  /**
   * <p>Checks if a String {@code str} contains Unicode digits,
   * if yes then concatenate all the digits in {@code str} and return it as a String.</p>
   *
   * <p>An empty ("") String will be returned if no digits found in {@code str}.</p>
   *
   * <pre>
   * StringUtils.getDigits(null)  = null
   * StringUtils.getDigits("")    = ""
   * StringUtils.getDigits("abc") = ""
   * StringUtils.getDigits("1000$") = "1000"
   * StringUtils.getDigits("1123~45") = "112345"
   * StringUtils.getDigits("(541) 754-3010") = "5417543010"
   * StringUtils.getDigits("\u0967\u0968\u0969") = "\u0967\u0968\u0969"
   * </pre>
   *
   * @param str the String to extract digits from, may be null
   * @return String with only digits,
   * or an empty ("") String if no digits found,
   * or {@code null} String if {@code str} is null
   */
  public static String getDigits(String str) {
    if (isEmpty(str)) {
      return str;
    }
    int sz = str.length();
    StringBuilder strDigits = new StringBuilder(sz);
    for (int i = 0; i < sz; i++) {
      char tempChar = str.charAt(i);
      if (Character.isDigit(tempChar)) {
        strDigits.append(tempChar);
      }
    }
    return strDigits.toString();
  }

  /**
   * <p>Finds the first index within a CharSequence, handling {@code null}.
   * This method uses {@link String#indexOf(String, int)} if possible.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.</p>
   *
   * <pre>
   * StringUtils.indexOf(null, *)          = -1
   * StringUtils.indexOf(*, null)          = -1
   * StringUtils.indexOf("", "")           = 0
   * StringUtils.indexOf("", *)            = -1 (except when * = "")
   * StringUtils.indexOf("aabaabaa", "a")  = 0
   * StringUtils.indexOf("aabaabaa", "b")  = 2
   * StringUtils.indexOf("aabaabaa", "ab") = 1
   * StringUtils.indexOf("aabaabaa", "")   = 0
   * </pre>
   *
   * @param seq       the CharSequence to check, may be null
   * @param searchSeq the CharSequence to find, may be null
   * @return the first index of the search CharSequence,
   * -1 if no match or {@code null} string input
   */
  public static int indexOf(CharSequence seq, CharSequence searchSeq) {
    if (seq == null || searchSeq == null) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.indexOf(seq, searchSeq, 0);
  }

  /**
   * <p>Finds the first index within a CharSequence, handling {@code null}.
   * This method uses {@link String#indexOf(String, int)} if possible.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A negative start position is treated as zero.
   * An empty ("") search CharSequence always matches.
   * A start position greater than the string length only matches
   * an empty search CharSequence.</p>
   *
   * <pre>
   * StringUtils.indexOf(null, *, *)          = -1
   * StringUtils.indexOf(*, null, *)          = -1
   * StringUtils.indexOf("", "", 0)           = 0
   * StringUtils.indexOf("", *, 0)            = -1 (except when * = "")
   * StringUtils.indexOf("aabaabaa", "a", 0)  = 0
   * StringUtils.indexOf("aabaabaa", "b", 0)  = 2
   * StringUtils.indexOf("aabaabaa", "ab", 0) = 1
   * StringUtils.indexOf("aabaabaa", "b", 3)  = 5
   * StringUtils.indexOf("aabaabaa", "b", 9)  = -1
   * StringUtils.indexOf("aabaabaa", "b", -1) = 2
   * StringUtils.indexOf("aabaabaa", "", 2)   = 2
   * StringUtils.indexOf("abc", "", 9)        = 3
   * </pre>
   *
   * @param seq       the CharSequence to check, may be null
   * @param searchSeq the CharSequence to find, may be null
   * @param startPos  the start position, negative treated as zero
   * @return the first index of the search CharSequence (always &ge; startPos),
   * -1 if no match or {@code null} string input
   */
  public static int indexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
    if (seq == null || searchSeq == null) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.indexOf(seq, searchSeq, startPos);
  }

  /**
   * Returns the index within {@code seq} of the first occurrence of
   * the specified character. If a character with value
   * {@code searchChar} occurs in the character sequence represented by
   * {@code seq} {@code CharSequence} object, then the index (in Unicode
   * code units) of the first such occurrence is returned. For
   * values of {@code searchChar} in the range from 0 to 0xFFFF
   * (inclusive), this is the smallest value <i>k</i> such that:
   * <blockquote><pre>
   * this.charAt(<i>k</i>) == searchChar
   * </pre></blockquote>
   * is true. For other values of {@code searchChar}, it is the
   * smallest value <i>k</i> such that:
   * <blockquote><pre>
   * this.codePointAt(<i>k</i>) == searchChar
   * </pre></blockquote>
   * is true. In either case, if no such character occurs in {@code seq},
   * then {@code INDEX_NOT_FOUND (-1)} is returned.
   *
   * <p>Furthermore, a {@code null} or empty ("") CharSequence will
   * return {@code INDEX_NOT_FOUND (-1)}.</p>
   *
   * <pre>
   * StringUtils.indexOf(null, *)         = -1
   * StringUtils.indexOf("", *)           = -1
   * StringUtils.indexOf("aabaabaa", 'a') = 0
   * StringUtils.indexOf("aabaabaa", 'b') = 2
   * </pre>
   *
   * @param seq        the CharSequence to check, may be null
   * @param searchChar the character to find
   * @return the first index of the search character,
   * -1 if no match or {@code null} string input
   */
  public static int indexOf(CharSequence seq, int searchChar) {
    if (isEmpty(seq)) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.indexOf(seq, searchChar, 0);
  }

  /**
   * Returns the index within {@code seq} of the first occurrence of the
   * specified character, starting the search at the specified index.
   * <p>
   * If a character with value {@code searchChar} occurs in the
   * character sequence represented by the {@code seq} {@code CharSequence}
   * object at an index no smaller than {@code startPos}, then
   * the index of the first such occurrence is returned. For values
   * of {@code searchChar} in the range from 0 to 0xFFFF (inclusive),
   * this is the smallest value <i>k</i> such that:
   * <blockquote><pre>
   * (this.charAt(<i>k</i>) == searchChar) &amp;&amp; (<i>k</i> &gt;= startPos)
   * </pre></blockquote>
   * is true. For other values of {@code searchChar}, it is the
   * smallest value <i>k</i> such that:
   * <blockquote><pre>
   * (this.codePointAt(<i>k</i>) == searchChar) &amp;&amp; (<i>k</i> &gt;= startPos)
   * </pre></blockquote>
   * is true. In either case, if no such character occurs in {@code seq}
   * at or after position {@code startPos}, then
   * {@code -1} is returned.
   *
   * <p>
   * There is no restriction on the value of {@code startPos}. If it
   * is negative, it has the same effect as if it were zero: this entire
   * string may be searched. If it is greater than the length of this
   * string, it has the same effect as if it were equal to the length of
   * this string: {@code (INDEX_NOT_FOUND) -1} is returned. Furthermore, a
   * {@code null} or empty ("") CharSequence will
   * return {@code (INDEX_NOT_FOUND) -1}.
   *
   * <p>All indices are specified in {@code char} values
   * (Unicode code units).
   *
   * <pre>
   * StringUtils.indexOf(null, *, *)          = -1
   * StringUtils.indexOf("", *, *)            = -1
   * StringUtils.indexOf("aabaabaa", 'b', 0)  = 2
   * StringUtils.indexOf("aabaabaa", 'b', 3)  = 5
   * StringUtils.indexOf("aabaabaa", 'b', 9)  = -1
   * StringUtils.indexOf("aabaabaa", 'b', -1) = 2
   * </pre>
   *
   * @param seq        the CharSequence to check, may be null
   * @param searchChar the character to find
   * @param startPos   the start position, negative treated as zero
   * @return the first index of the search character (always &ge; startPos),
   * -1 if no match or {@code null} string input
   */
  public static int indexOf(CharSequence seq, int searchChar, int startPos) {
    if (isEmpty(seq)) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.indexOf(seq, searchChar, startPos);
  }

  /**
   * <p>Search a CharSequence to find the first index of any
   * character in the given set of characters.</p>
   *
   * <p>A {@code null} String will return {@code -1}.
   * A {@code null} or zero length search array will return {@code -1}.</p>
   *
   * <pre>
   * StringUtils.indexOfAny(null, *)                  = -1
   * StringUtils.indexOfAny("", *)                    = -1
   * StringUtils.indexOfAny(*, null)                  = -1
   * StringUtils.indexOfAny(*, [])                    = -1
   * StringUtils.indexOfAny("zzabyycdxx", ['z', 'a']) = 0
   * StringUtils.indexOfAny("zzabyycdxx", ['b', 'y']) = 3
   * StringUtils.indexOfAny("aba", ['z'])             = -1
   * </pre>
   *
   * @param cs          the CharSequence to check, may be null
   * @param searchChars the chars to search for, may be null
   * @return the index of any of the chars, -1 if no match or null input
   */
  public static int indexOfAny(CharSequence cs, char... searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
      return INDEX_NOT_FOUND;
    }
    int csLen = cs.length();
    int csLast = csLen - 1;
    int searchLen = searchChars.length;
    int searchLast = searchLen - 1;
    for (int i = 0; i < csLen; i++) {
      char ch = cs.charAt(i);
      for (int j = 0; j < searchLen; j++) {
        if (searchChars[j] == ch) {
          if (i < csLast && j < searchLast && Character.isHighSurrogate(ch)) {
            // ch is a supplementary character
            if (searchChars[j + 1] == cs.charAt(i + 1)) {
              return i;
            }
          } else {
            return i;
          }
        }
      }
    }
    return INDEX_NOT_FOUND;
  }

  /**
   * <p>Find the first index of any of a set of potential substrings.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A {@code null} or zero length search array will return {@code -1}.
   * A {@code null} search array entry will be ignored, but a search
   * array containing "" will return {@code 0} if {@code str} is not
   * null. This method uses {@link String#indexOf(String)} if possible.</p>
   *
   * <pre>
   * StringUtils.indexOfAny(null, *)                      = -1
   * StringUtils.indexOfAny(*, null)                      = -1
   * StringUtils.indexOfAny(*, [])                        = -1
   * StringUtils.indexOfAny("zzabyycdxx", ["ab", "cd"])   = 2
   * StringUtils.indexOfAny("zzabyycdxx", ["cd", "ab"])   = 2
   * StringUtils.indexOfAny("zzabyycdxx", ["mn", "op"])   = -1
   * StringUtils.indexOfAny("zzabyycdxx", ["zab", "aby"]) = 1
   * StringUtils.indexOfAny("zzabyycdxx", [""])           = 0
   * StringUtils.indexOfAny("", [""])                     = 0
   * StringUtils.indexOfAny("", ["a"])                    = -1
   * </pre>
   *
   * @param str        the CharSequence to check, may be null
   * @param searchStrs the CharSequences to search for, may be null
   * @return the first index of any of the searchStrs in str, -1 if no match
   */
  public static int indexOfAny(CharSequence str, CharSequence... searchStrs) {
    if (str == null || searchStrs == null) {
      return INDEX_NOT_FOUND;
    }

    // String's can't have a MAX_VALUEth index.
    int ret = Integer.MAX_VALUE;

    int tmp;
    for (CharSequence search : searchStrs) {
      if (search == null) {
        continue;
      }
      tmp = CharSequenceUtils.indexOf(str, search, 0);
      if (tmp == INDEX_NOT_FOUND) {
        continue;
      }

      if (tmp < ret) {
        ret = tmp;
      }
    }

    return ret == Integer.MAX_VALUE ? INDEX_NOT_FOUND : ret;
  }

  /**
   * <p>Search a CharSequence to find the first index of any
   * character in the given set of characters.</p>
   *
   * <p>A {@code null} String will return {@code -1}.
   * A {@code null} search string will return {@code -1}.</p>
   *
   * <pre>
   * StringUtils.indexOfAny(null, *)            = -1
   * StringUtils.indexOfAny("", *)              = -1
   * StringUtils.indexOfAny(*, null)            = -1
   * StringUtils.indexOfAny(*, "")              = -1
   * StringUtils.indexOfAny("zzabyycdxx", "za") = 0
   * StringUtils.indexOfAny("zzabyycdxx", "by") = 3
   * StringUtils.indexOfAny("aba", "z")         = -1
   * </pre>
   *
   * @param cs          the CharSequence to check, may be null
   * @param searchChars the chars to search for, may be null
   * @return the index of any of the chars, -1 if no match or null input
   */
  public static int indexOfAny(CharSequence cs, String searchChars) {
    if (isEmpty(cs) || isEmpty(searchChars)) {
      return INDEX_NOT_FOUND;
    }
    return indexOfAny(cs, searchChars.toCharArray());
  }

  /**
   * <p>Searches a CharSequence to find the first index of any
   * character not in the given set of characters.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A {@code null} or zero length search array will return {@code -1}.</p>
   *
   * <pre>
   * StringUtils.indexOfAnyBut(null, *)                              = -1
   * StringUtils.indexOfAnyBut("", *)                                = -1
   * StringUtils.indexOfAnyBut(*, null)                              = -1
   * StringUtils.indexOfAnyBut(*, [])                                = -1
   * StringUtils.indexOfAnyBut("zzabyycdxx", new char[] {'z', 'a'} ) = 3
   * StringUtils.indexOfAnyBut("aba", new char[] {'z'} )             = 0
   * StringUtils.indexOfAnyBut("aba", new char[] {'a', 'b'} )        = -1
   *
   * </pre>
   *
   * @param cs          the CharSequence to check, may be null
   * @param searchChars the chars to search for, may be null
   * @return the index of any of the chars, -1 if no match or null input
   */
  public static int indexOfAnyBut(CharSequence cs, char... searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
      return INDEX_NOT_FOUND;
    }
    int csLen = cs.length();
    int csLast = csLen - 1;
    int searchLen = searchChars.length;
    int searchLast = searchLen - 1;
    outer:
    for (int i = 0; i < csLen; i++) {
      char ch = cs.charAt(i);
      for (int j = 0; j < searchLen; j++) {
        if (searchChars[j] == ch) {
          if (i < csLast && j < searchLast && Character.isHighSurrogate(ch)) {
            if (searchChars[j + 1] == cs.charAt(i + 1)) {
              continue outer;
            }
          } else {
            continue outer;
          }
        }
      }
      return i;
    }
    return INDEX_NOT_FOUND;
  }

  /**
   * <p>Search a CharSequence to find the first index of any
   * character not in the given set of characters.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A {@code null} or empty search string will return {@code -1}.</p>
   *
   * <pre>
   * StringUtils.indexOfAnyBut(null, *)            = -1
   * StringUtils.indexOfAnyBut("", *)              = -1
   * StringUtils.indexOfAnyBut(*, null)            = -1
   * StringUtils.indexOfAnyBut(*, "")              = -1
   * StringUtils.indexOfAnyBut("zzabyycdxx", "za") = 3
   * StringUtils.indexOfAnyBut("zzabyycdxx", "")   = -1
   * StringUtils.indexOfAnyBut("aba", "ab")        = -1
   * </pre>
   *
   * @param seq         the CharSequence to check, may be null
   * @param searchChars the chars to search for, may be null
   * @return the index of any of the chars, -1 if no match or null input
   */
  public static int indexOfAnyBut(CharSequence seq, CharSequence searchChars) {
    if (isEmpty(seq) || isEmpty(searchChars)) {
      return INDEX_NOT_FOUND;
    }
    int strLen = seq.length();
    for (int i = 0; i < strLen; i++) {
      char ch = seq.charAt(i);
      boolean chFound = CharSequenceUtils.indexOf(searchChars, ch, 0) >= 0;
      if (i + 1 < strLen && Character.isHighSurrogate(ch)) {
        char ch2 = seq.charAt(i + 1);
        if (chFound && CharSequenceUtils.indexOf(searchChars, ch2, 0) < 0) {
          return i;
        }
      } else if (!chFound) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }

  /**
   * <p>Compares all CharSequences in an array and returns the index at which the
   * CharSequences begin to differ.</p>
   *
   * <p>For example,
   * {@code indexOfDifference(new String[] {"i am a machine", "i am a robot"}) -> 7}</p>
   *
   * <pre>
   * StringUtils.indexOfDifference(null) = -1
   * StringUtils.indexOfDifference(new String[] {}) = -1
   * StringUtils.indexOfDifference(new String[] {"abc"}) = -1
   * StringUtils.indexOfDifference(new String[] {null, null}) = -1
   * StringUtils.indexOfDifference(new String[] {"", ""}) = -1
   * StringUtils.indexOfDifference(new String[] {"", null}) = 0
   * StringUtils.indexOfDifference(new String[] {"abc", null, null}) = 0
   * StringUtils.indexOfDifference(new String[] {null, null, "abc"}) = 0
   * StringUtils.indexOfDifference(new String[] {"", "abc"}) = 0
   * StringUtils.indexOfDifference(new String[] {"abc", ""}) = 0
   * StringUtils.indexOfDifference(new String[] {"abc", "abc"}) = -1
   * StringUtils.indexOfDifference(new String[] {"abc", "a"}) = 1
   * StringUtils.indexOfDifference(new String[] {"ab", "abxyz"}) = 2
   * StringUtils.indexOfDifference(new String[] {"abcde", "abxyz"}) = 2
   * StringUtils.indexOfDifference(new String[] {"abcde", "xyz"}) = 0
   * StringUtils.indexOfDifference(new String[] {"xyz", "abcde"}) = 0
   * StringUtils.indexOfDifference(new String[] {"i am a machine", "i am a robot"}) = 7
   * </pre>
   *
   * @param css array of CharSequences, entries may be null
   * @return the index where the strings begin to differ; -1 if they are all equal
   */
  public static int indexOfDifference(CharSequence... css) {
    if (ArrayUtils.getLength(css) <= 1) {
      return INDEX_NOT_FOUND;
    }
    boolean anyStringNull = false;
    boolean allStringsNull = true;
    int arrayLen = css.length;
    int shortestStrLen = Integer.MAX_VALUE;
    int longestStrLen = 0;

    // find the min and max string lengths; this avoids checking to make
    // sure we are not exceeding the length of the string each time through
    // the bottom loop.
    for (CharSequence cs : css) {
      if (cs == null) {
        anyStringNull = true;
        shortestStrLen = 0;
      } else {
        allStringsNull = false;
        shortestStrLen = Math.min(cs.length(), shortestStrLen);
        longestStrLen = Math.max(cs.length(), longestStrLen);
      }
    }

    // handle lists containing all nulls or all empty strings
    if (allStringsNull || longestStrLen == 0 && !anyStringNull) {
      return INDEX_NOT_FOUND;
    }

    // handle lists containing some nulls or some empty strings
    if (shortestStrLen == 0) {
      return 0;
    }

    // find the position with the first difference across all strings
    int firstDiff = -1;
    for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
      char comparisonChar = css[0].charAt(stringPos);
      for (int arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
        if (css[arrayPos].charAt(stringPos) != comparisonChar) {
          firstDiff = stringPos;
          break;
        }
      }
      if (firstDiff != -1) {
        break;
      }
    }

    if (firstDiff == -1 && shortestStrLen != longestStrLen) {
      // we compared all of the characters up to the length of the
      // shortest string and didn't find a match, but the string lengths
      // vary, so return the length of the shortest string.
      return shortestStrLen;
    }
    return firstDiff;
  }

  /**
   * <p>Compares two CharSequences, and returns the index at which the
   * CharSequences begin to differ.</p>
   *
   * <p>For example,
   * {@code indexOfDifference("i am a machine", "i am a robot") -> 7}</p>
   *
   * <pre>
   * StringUtils.indexOfDifference(null, null) = -1
   * StringUtils.indexOfDifference("", "") = -1
   * StringUtils.indexOfDifference("", "abc") = 0
   * StringUtils.indexOfDifference("abc", "") = 0
   * StringUtils.indexOfDifference("abc", "abc") = -1
   * StringUtils.indexOfDifference("ab", "abxyz") = 2
   * StringUtils.indexOfDifference("abcde", "abxyz") = 2
   * StringUtils.indexOfDifference("abcde", "xyz") = 0
   * </pre>
   *
   * @param cs1 the first CharSequence, may be null
   * @param cs2 the second CharSequence, may be null
   * @return the index where cs1 and cs2 begin to differ; -1 if they are equal
   * indexOfDifference(CharSequence, CharSequence)
   */
  public static int indexOfDifference(CharSequence cs1, CharSequence cs2) {
    if (cs1 == cs2) {
      return INDEX_NOT_FOUND;
    }
    if (cs1 == null || cs2 == null) {
      return 0;
    }
    int i;
    for (i = 0; i < cs1.length() && i < cs2.length(); ++i) {
      if (cs1.charAt(i) != cs2.charAt(i)) {
        break;
      }
    }
    if (i < cs2.length() || i < cs1.length()) {
      return i;
    }
    return INDEX_NOT_FOUND;
  }

  /**
   * <p>Case in-sensitive find of the first index within a CharSequence.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A negative start position is treated as zero.
   * An empty ("") search CharSequence always matches.
   * A start position greater than the string length only matches
   * an empty search CharSequence.</p>
   *
   * <pre>
   * StringUtils.indexOfIgnoreCase(null, *)          = -1
   * StringUtils.indexOfIgnoreCase(*, null)          = -1
   * StringUtils.indexOfIgnoreCase("", "")           = 0
   * StringUtils.indexOfIgnoreCase("aabaabaa", "a")  = 0
   * StringUtils.indexOfIgnoreCase("aabaabaa", "b")  = 2
   * StringUtils.indexOfIgnoreCase("aabaabaa", "ab") = 1
   * </pre>
   *
   * @param str       the CharSequence to check, may be null
   * @param searchStr the CharSequence to find, may be null
   * @return the first index of the search CharSequence,
   * -1 if no match or {@code null} string input
   */
  public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
    return indexOfIgnoreCase(str, searchStr, 0);
  }

  /**
   * <p>Case in-sensitive find of the first index within a CharSequence
   * from the specified position.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A negative start position is treated as zero.
   * An empty ("") search CharSequence always matches.
   * A start position greater than the string length only matches
   * an empty search CharSequence.</p>
   *
   * <pre>
   * StringUtils.indexOfIgnoreCase(null, *, *)          = -1
   * StringUtils.indexOfIgnoreCase(*, null, *)          = -1
   * StringUtils.indexOfIgnoreCase("", "", 0)           = 0
   * StringUtils.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
   * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
   * StringUtils.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
   * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
   * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
   * StringUtils.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
   * StringUtils.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
   * StringUtils.indexOfIgnoreCase("abc", "", 9)        = -1
   * </pre>
   *
   * @param str       the CharSequence to check, may be null
   * @param searchStr the CharSequence to find, may be null
   * @param startPos  the start position, negative treated as zero
   * @return the first index of the search CharSequence (always &ge; startPos),
   * -1 if no match or {@code null} string input
   */
  public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
    if (str == null || searchStr == null) {
      return INDEX_NOT_FOUND;
    }
    if (startPos < 0) {
      startPos = 0;
    }
    int endLimit = str.length() - searchStr.length() + 1;
    if (startPos > endLimit) {
      return INDEX_NOT_FOUND;
    }
    if (searchStr.length() == 0) {
      return startPos;
    }
    for (int i = startPos; i < endLimit; i++) {
      if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }

  /**
   * <p>Checks if all of the CharSequences are empty (""), null or whitespace only.</p>
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.isAllBlank(null)             = true
   * StringUtils.isAllBlank(null, "foo")      = false
   * StringUtils.isAllBlank(null, null)       = true
   * StringUtils.isAllBlank("", "bar")        = false
   * StringUtils.isAllBlank("bob", "")        = false
   * StringUtils.isAllBlank("  bob  ", null)  = false
   * StringUtils.isAllBlank(" ", "bar")       = false
   * StringUtils.isAllBlank("foo", "bar")     = false
   * StringUtils.isAllBlank(new String[] {})  = true
   * </pre>
   *
   * @param css the CharSequences to check, may be null or empty
   * @return {@code true} if all of the CharSequences are empty or null or whitespace only
   */
  public static boolean isAllBlank(CharSequence... css) {
    if (ArrayUtils.isEmpty(css)) {
      return true;
    }
    for (CharSequence cs : css) {
      if (isNotBlank(cs)) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>Checks if all of the CharSequences are empty ("") or null.</p>
   *
   * <pre>
   * StringUtils.isAllEmpty(null)             = true
   * StringUtils.isAllEmpty(null, "")         = true
   * StringUtils.isAllEmpty(new String[] {})  = true
   * StringUtils.isAllEmpty(null, "foo")      = false
   * StringUtils.isAllEmpty("", "bar")        = false
   * StringUtils.isAllEmpty("bob", "")        = false
   * StringUtils.isAllEmpty("  bob  ", null)  = false
   * StringUtils.isAllEmpty(" ", "bar")       = false
   * StringUtils.isAllEmpty("foo", "bar")     = false
   * </pre>
   *
   * @param css the CharSequences to check, may be null or empty
   * @return {@code true} if all of the CharSequences are empty or null
   */
  public static boolean isAllEmpty(CharSequence... css) {
    if (ArrayUtils.isEmpty(css)) {
      return true;
    }
    for (CharSequence cs : css) {
      if (isNotEmpty(cs)) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>Checks if the CharSequence contains only lowercase characters.</p>
   *
   * <p>{@code null} will return {@code false}.
   * An empty CharSequence (length()=0) will return {@code false}.</p>
   *
   * <pre>
   * StringUtils.isAllLowerCase(null)   = false
   * StringUtils.isAllLowerCase("")     = false
   * StringUtils.isAllLowerCase("  ")   = false
   * StringUtils.isAllLowerCase("abc")  = true
   * StringUtils.isAllLowerCase("abC")  = false
   * StringUtils.isAllLowerCase("ab c") = false
   * StringUtils.isAllLowerCase("ab1c") = false
   * StringUtils.isAllLowerCase("ab/c") = false
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if only contains lowercase characters, and is non-null
   */
  public static boolean isAllLowerCase(CharSequence cs) {
    if (isEmpty(cs)) {
      return false;
    }
    int sz = cs.length();
    for (int i = 0; i < sz; i++) {
      if (!Character.isLowerCase(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>Checks if the CharSequence contains only uppercase characters.</p>
   *
   * <p>{@code null} will return {@code false}.
   * An empty String (length()=0) will return {@code false}.</p>
   *
   * <pre>
   * StringUtils.isAllUpperCase(null)   = false
   * StringUtils.isAllUpperCase("")     = false
   * StringUtils.isAllUpperCase("  ")   = false
   * StringUtils.isAllUpperCase("ABC")  = true
   * StringUtils.isAllUpperCase("aBC")  = false
   * StringUtils.isAllUpperCase("A C")  = false
   * StringUtils.isAllUpperCase("A1C")  = false
   * StringUtils.isAllUpperCase("A/C")  = false
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if only contains uppercase characters, and is non-null
   */
  public static boolean isAllUpperCase(CharSequence cs) {
    if (isEmpty(cs)) {
      return false;
    }
    int sz = cs.length();
    for (int i = 0; i < sz; i++) {
      if (!Character.isUpperCase(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>Checks if the CharSequence contains only Unicode letters.</p>
   *
   * <p>{@code null} will return {@code false}.
   * An empty CharSequence (length()=0) will return {@code false}.</p>
   *
   * <pre>
   * StringUtils.isAlpha(null)   = false
   * StringUtils.isAlpha("")     = false
   * StringUtils.isAlpha("  ")   = false
   * StringUtils.isAlpha("abc")  = true
   * StringUtils.isAlpha("ab2c") = false
   * StringUtils.isAlpha("ab-c") = false
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if only contains letters, and is non-null
   */
  public static boolean isAlpha(CharSequence cs) {
    if (isEmpty(cs)) {
      return false;
    }
    int sz = cs.length();
    for (int i = 0; i < sz; i++) {
      if (!Character.isLetter(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>Checks if the CharSequence contains only Unicode letters or digits.</p>
   *
   * <p>{@code null} will return {@code false}.
   * An empty CharSequence (length()=0) will return {@code false}.</p>
   *
   * <pre>
   * StringUtils.isAlphanumeric(null)   = false
   * StringUtils.isAlphanumeric("")     = false
   * StringUtils.isAlphanumeric("  ")   = false
   * StringUtils.isAlphanumeric("abc")  = true
   * StringUtils.isAlphanumeric("ab c") = false
   * StringUtils.isAlphanumeric("ab2c") = true
   * StringUtils.isAlphanumeric("ab-c") = false
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if only contains letters or digits,
   * and is non-null
   */
  public static boolean isAlphanumeric(CharSequence cs) {
    if (isEmpty(cs)) {
      return false;
    }
    int sz = cs.length();
    for (int i = 0; i < sz; i++) {
      if (!Character.isLetterOrDigit(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>Checks if the CharSequence contains only Unicode letters, digits
   * or space ({@code ' '}).</p>
   *
   * <p>{@code null} will return {@code false}.
   * An empty CharSequence (length()=0) will return {@code true}.</p>
   *
   * <pre>
   * StringUtils.isAlphanumericSpace(null)   = false
   * StringUtils.isAlphanumericSpace("")     = true
   * StringUtils.isAlphanumericSpace("  ")   = true
   * StringUtils.isAlphanumericSpace("abc")  = true
   * StringUtils.isAlphanumericSpace("ab c") = true
   * StringUtils.isAlphanumericSpace("ab2c") = true
   * StringUtils.isAlphanumericSpace("ab-c") = false
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if only contains letters, digits or space,
   * and is non-null
   */
  public static boolean isAlphanumericSpace(CharSequence cs) {
    if (cs == null) {
      return false;
    }
    int sz = cs.length();
    for (int i = 0; i < sz; i++) {
      char nowChar = cs.charAt(i);
      if (nowChar != ' ' && !Character.isLetterOrDigit(nowChar)) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>Checks if the CharSequence contains only Unicode letters and
   * space (' ').</p>
   *
   * <p>{@code null} will return {@code false}
   * An empty CharSequence (length()=0) will return {@code true}.</p>
   *
   * <pre>
   * StringUtils.isAlphaSpace(null)   = false
   * StringUtils.isAlphaSpace("")     = true
   * StringUtils.isAlphaSpace("  ")   = true
   * StringUtils.isAlphaSpace("abc")  = true
   * StringUtils.isAlphaSpace("ab c") = true
   * StringUtils.isAlphaSpace("ab2c") = false
   * StringUtils.isAlphaSpace("ab-c") = false
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if only contains letters and space,
   * and is non-null
   */
  public static boolean isAlphaSpace(CharSequence cs) {
    if (cs == null) {
      return false;
    }
    int sz = cs.length();
    for (int i = 0; i < sz; i++) {
      char nowChar = cs.charAt(i);
      if (nowChar != ' ' && !Character.isLetter(nowChar)) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>Checks if any of the CharSequences are empty ("") or null or whitespace only.</p>
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.isAnyBlank((String) null)    = true
   * StringUtils.isAnyBlank((String[]) null)  = false
   * StringUtils.isAnyBlank(null, "foo")      = true
   * StringUtils.isAnyBlank(null, null)       = true
   * StringUtils.isAnyBlank("", "bar")        = true
   * StringUtils.isAnyBlank("bob", "")        = true
   * StringUtils.isAnyBlank("  bob  ", null)  = true
   * StringUtils.isAnyBlank(" ", "bar")       = true
   * StringUtils.isAnyBlank(new String[] {})  = false
   * StringUtils.isAnyBlank(new String[]{""}) = true
   * StringUtils.isAnyBlank("foo", "bar")     = false
   * </pre>
   *
   * @param css the CharSequences to check, may be null or empty
   * @return {@code true} if any of the CharSequences are empty or null or whitespace only
   */
  public static boolean isAnyBlank(CharSequence... css) {
    if (ArrayUtils.isEmpty(css)) {
      return false;
    }
    for (CharSequence cs : css) {
      if (isBlank(cs)) {
        return true;
      }
    }
    return false;
  }

  /**
   * <p>Checks if any of the CharSequences are empty ("") or null.</p>
   *
   * <pre>
   * StringUtils.isAnyEmpty((String) null)    = true
   * StringUtils.isAnyEmpty((String[]) null)  = false
   * StringUtils.isAnyEmpty(null, "foo")      = true
   * StringUtils.isAnyEmpty("", "bar")        = true
   * StringUtils.isAnyEmpty("bob", "")        = true
   * StringUtils.isAnyEmpty("  bob  ", null)  = true
   * StringUtils.isAnyEmpty(" ", "bar")       = false
   * StringUtils.isAnyEmpty("foo", "bar")     = false
   * StringUtils.isAnyEmpty(new String[]{})   = false
   * StringUtils.isAnyEmpty(new String[]{""}) = true
   * </pre>
   *
   * @param css the CharSequences to check, may be null or empty
   * @return {@code true} if any of the CharSequences are empty or null
   */
  public static boolean isAnyEmpty(CharSequence... css) {
    if (ArrayUtils.isEmpty(css)) {
      return false;
    }
    for (CharSequence cs : css) {
      if (isEmpty(cs)) {
        return true;
      }
    }
    return false;
  }

  /**
   * <p>Checks if a CharSequence is empty (""), null or whitespace only.</p>
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.isBlank(null)      = true
   * StringUtils.isBlank("")        = true
   * StringUtils.isBlank(" ")       = true
   * StringUtils.isBlank("bob")     = false
   * StringUtils.isBlank("  bob  ") = false
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if the CharSequence is null, empty or whitespace only
   */
  public static boolean isBlank(@Nullable CharSequence cs) {
    int strLen;
    if (cs == null || (strLen = cs.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>Checks if a CharSequence is empty ("") or null.</p>
   *
   * <pre>
   * StringUtils.isEmpty(null)      = true
   * StringUtils.isEmpty("")        = true
   * StringUtils.isEmpty(" ")       = false
   * StringUtils.isEmpty("bob")     = false
   * StringUtils.isEmpty("  bob  ") = false
   * </pre>
   *
   * <p>NOTE: This method changed in Lang version 2.0.
   * It no longer trims the CharSequence.
   * That functionality is available in isBlank().</p>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if the CharSequence is empty or null
   */
  public static boolean isEmpty(CharSequence cs) {
    return cs == null || cs.length() == 0;
  }

  /**
   * <p>Checks if the CharSequence contains mixed casing of both uppercase and lowercase characters.</p>
   *
   * <p>{@code null} will return {@code false}. An empty CharSequence ({@code length()=0}) will return
   * {@code false}.</p>
   *
   * <pre>
   * StringUtils.isMixedCase(null)    = false
   * StringUtils.isMixedCase("")      = false
   * StringUtils.isMixedCase("ABC")   = false
   * StringUtils.isMixedCase("abc")   = false
   * StringUtils.isMixedCase("aBc")   = true
   * StringUtils.isMixedCase("A c")   = true
   * StringUtils.isMixedCase("A1c")   = true
   * StringUtils.isMixedCase("a/C")   = true
   * StringUtils.isMixedCase("aC\t")  = true
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if the CharSequence contains both uppercase and lowercase characters
   */
  public static boolean isMixedCase(CharSequence cs) {
    if (isEmpty(cs) || cs.length() == 1) {
      return false;
    }
    boolean containsUppercase = false;
    boolean containsLowercase = false;
    int sz = cs.length();
    for (int i = 0; i < sz; i++) {
      if (containsUppercase && containsLowercase) {
        return true;
      } else if (Character.isUpperCase(cs.charAt(i))) {
        containsUppercase = true;
      } else if (Character.isLowerCase(cs.charAt(i))) {
        containsLowercase = true;
      }
    }
    return containsUppercase && containsLowercase;
  }

  /**
   * <p>Checks if none of the CharSequences are empty (""), null or whitespace only.</p>
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.isNoneBlank((String) null)    = false
   * StringUtils.isNoneBlank((String[]) null)  = true
   * StringUtils.isNoneBlank(null, "foo")      = false
   * StringUtils.isNoneBlank(null, null)       = false
   * StringUtils.isNoneBlank("", "bar")        = false
   * StringUtils.isNoneBlank("bob", "")        = false
   * StringUtils.isNoneBlank("  bob  ", null)  = false
   * StringUtils.isNoneBlank(" ", "bar")       = false
   * StringUtils.isNoneBlank(new String[] {})  = true
   * StringUtils.isNoneBlank(new String[]{""}) = false
   * StringUtils.isNoneBlank("foo", "bar")     = true
   * </pre>
   *
   * @param css the CharSequences to check, may be null or empty
   * @return {@code true} if none of the CharSequences are empty or null or whitespace only
   */
  public static boolean isNoneBlank(CharSequence... css) {
    return !isAnyBlank(css);
  }

  /**
   * <p>Checks if none of the CharSequences are empty ("") or null.</p>
   *
   * <pre>
   * StringUtils.isNoneEmpty((String) null)    = false
   * StringUtils.isNoneEmpty((String[]) null)  = true
   * StringUtils.isNoneEmpty(null, "foo")      = false
   * StringUtils.isNoneEmpty("", "bar")        = false
   * StringUtils.isNoneEmpty("bob", "")        = false
   * StringUtils.isNoneEmpty("  bob  ", null)  = false
   * StringUtils.isNoneEmpty(new String[] {})  = true
   * StringUtils.isNoneEmpty(new String[]{""}) = false
   * StringUtils.isNoneEmpty(" ", "bar")       = true
   * StringUtils.isNoneEmpty("foo", "bar")     = true
   * </pre>
   *
   * @param css the CharSequences to check, may be null or empty
   * @return {@code true} if none of the CharSequences are empty or null
   */
  public static boolean isNoneEmpty(CharSequence... css) {
    return !isAnyEmpty(css);
  }

  /**
   * <p>Checks if a CharSequence is not empty (""), not null and not whitespace only.</p>
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.isNotBlank(null)      = false
   * StringUtils.isNotBlank("")        = false
   * StringUtils.isNotBlank(" ")       = false
   * StringUtils.isNotBlank("bob")     = true
   * StringUtils.isNotBlank("  bob  ") = true
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if the CharSequence is
   * not empty and not null and not whitespace only
   */
  public static boolean isNotBlank(@Nullable CharSequence cs) {
    return !isBlank(cs);
  }

  /**
   * <p>Checks if a CharSequence is not empty ("") and not null.</p>
   *
   * <pre>
   * StringUtils.isNotEmpty(null)      = false
   * StringUtils.isNotEmpty("")        = false
   * StringUtils.isNotEmpty(" ")       = true
   * StringUtils.isNotEmpty("bob")     = true
   * StringUtils.isNotEmpty("  bob  ") = true
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if the CharSequence is not empty and not null
   */
  public static boolean isNotEmpty(CharSequence cs) {
    return !isEmpty(cs);
  }

  /**
   * <p>Checks if the CharSequence contains only Unicode digits.
   * A decimal point is not a Unicode digit and returns false.</p>
   *
   * <p>{@code null} will return {@code false}.
   * An empty CharSequence (length()=0) will return {@code false}.</p>
   *
   * <p>Note that the method does not allow for a leading sign, either positive or negative.
   * Also, if a String passes the numeric test, it may still generate a NumberFormatException
   * when parsed by Integer.parseInt or Long.parseLong, e.g. if the value is outside the range
   * for int or long respectively.</p>
   *
   * <pre>
   * StringUtils.isNumeric(null)   = false
   * StringUtils.isNumeric("")     = false
   * StringUtils.isNumeric("  ")   = false
   * StringUtils.isNumeric("123")  = true
   * StringUtils.isNumeric("\u0967\u0968\u0969")  = true
   * StringUtils.isNumeric("12 3") = false
   * StringUtils.isNumeric("ab2c") = false
   * StringUtils.isNumeric("12-3") = false
   * StringUtils.isNumeric("12.3") = false
   * StringUtils.isNumeric("-123") = false
   * StringUtils.isNumeric("+123") = false
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if only contains digits, and is non-null
   */
  public static boolean isNumeric(CharSequence cs) {
    if (isEmpty(cs)) {
      return false;
    }
    int sz = cs.length();
    for (int i = 0; i < sz; i++) {
      if (!Character.isDigit(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>Checks if the CharSequence contains only Unicode digits or space
   * ({@code ' '}).
   * A decimal point is not a Unicode digit and returns false.</p>
   *
   * <p>{@code null} will return {@code false}.
   * An empty CharSequence (length()=0) will return {@code true}.</p>
   *
   * <pre>
   * StringUtils.isNumericSpace(null)   = false
   * StringUtils.isNumericSpace("")     = true
   * StringUtils.isNumericSpace("  ")   = true
   * StringUtils.isNumericSpace("123")  = true
   * StringUtils.isNumericSpace("12 3") = true
   * StringUtils.isNumericSpace("\u0967\u0968\u0969")  = true
   * StringUtils.isNumericSpace("\u0967\u0968 \u0969")  = true
   * StringUtils.isNumericSpace("ab2c") = false
   * StringUtils.isNumericSpace("12-3") = false
   * StringUtils.isNumericSpace("12.3") = false
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if only contains digits or space,
   * and is non-null
   */
  public static boolean isNumericSpace(CharSequence cs) {
    if (cs == null) {
      return false;
    }
    int sz = cs.length();
    for (int i = 0; i < sz; i++) {
      char nowChar = cs.charAt(i);
      if (nowChar != ' ' && !Character.isDigit(nowChar)) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>Checks if the CharSequence contains only whitespace.</p>
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <p>{@code null} will return {@code false}.
   * An empty CharSequence (length()=0) will return {@code true}.</p>
   *
   * <pre>
   * StringUtils.isWhitespace(null)   = false
   * StringUtils.isWhitespace("")     = true
   * StringUtils.isWhitespace("  ")   = true
   * StringUtils.isWhitespace("abc")  = false
   * StringUtils.isWhitespace("ab2c") = false
   * StringUtils.isWhitespace("ab-c") = false
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if only contains whitespace, and is non-null
   */
  public static boolean isWhitespace(CharSequence cs) {
    if (cs == null) {
      return false;
    }
    int sz = cs.length();
    for (int i = 0; i < sz; i++) {
      if (!Character.isWhitespace(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)              = null
   * StringUtils.join([], *)                = ""
   * StringUtils.join([null], *)            = ""
   * StringUtils.join([false, false], ';')  = "false;false"
   * </pre>
   *
   * @param array     the array of values to join together, may be null
   * @param delimiter the separator character to use
   * @return the joined String, {@code null} if null array input
   */
  public static String join(boolean[] array, char delimiter) {
    if (array == null) {
      return null;
    }
    return join(array, delimiter, 0, array.length);
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)                   = null
   * StringUtils.join([], *)                     = ""
   * StringUtils.join([null], *)                 = ""
   * StringUtils.join([true, false, true], ';')  = "true;false;true"
   * </pre>
   *
   * @param array      the array of values to join together, may be null
   * @param delimiter  the separator character to use
   * @param startIndex the first index to start joining from. It is an error to pass in a start index past the end of the
   *                   array
   * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of
   *                   the array
   * @return the joined String, {@code null} if null array input
   */
  public static String join(boolean[] array, char delimiter, int startIndex, int endIndex) {
    if (array == null) {
      return null;
    }
    if (endIndex - startIndex <= 0) {
      return EMPTY;
    }
    StringJoiner joiner = newStringJoiner(delimiter);
    for (int i = startIndex; i < endIndex; i++) {
      joiner.add(String.valueOf(array[i]));
    }
    return joiner.toString();
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
   * StringUtils.join([1, 2, 3], null) = "123"
   * </pre>
   *
   * @param array     the array of values to join together, may be null
   * @param delimiter the separator character to use
   * @return the joined String, {@code null} if null array input
   */
  public static String join(byte[] array, char delimiter) {
    if (array == null) {
      return null;
    }
    return join(array, delimiter, 0, array.length);
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
   * StringUtils.join([1, 2, 3], null) = "123"
   * </pre>
   *
   * @param array      the array of values to join together, may be null
   * @param delimiter  the separator character to use
   * @param startIndex the first index to start joining from. It is an error to pass in a start index past the end of the
   *                   array
   * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of
   *                   the array
   * @return the joined String, {@code null} if null array input
   */
  public static String join(byte[] array, char delimiter, int startIndex, int endIndex) {
    if (array == null) {
      return null;
    }
    if (endIndex - startIndex <= 0) {
      return EMPTY;
    }
    StringJoiner joiner = newStringJoiner(delimiter);
    for (int i = startIndex; i < endIndex; i++) {
      joiner.add(String.valueOf(array[i]));
    }
    return joiner.toString();
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
   * StringUtils.join([1, 2, 3], null) = "123"
   * </pre>
   *
   * @param array     the array of values to join together, may be null
   * @param delimiter the separator character to use
   * @return the joined String, {@code null} if null array input
   */
  public static String join(char[] array, char delimiter) {
    if (array == null) {
      return null;
    }
    return join(array, delimiter, 0, array.length);
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
   * StringUtils.join([1, 2, 3], null) = "123"
   * </pre>
   *
   * @param array      the array of values to join together, may be null
   * @param delimiter  the separator character to use
   * @param startIndex the first index to start joining from. It is an error to pass in a start index past the end of the
   *                   array
   * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of
   *                   the array
   * @return the joined String, {@code null} if null array input
   */
  public static String join(char[] array, char delimiter, int startIndex, int endIndex) {
    if (array == null) {
      return null;
    }
    if (endIndex - startIndex <= 0) {
      return EMPTY;
    }
    StringJoiner joiner = newStringJoiner(delimiter);
    for (int i = startIndex; i < endIndex; i++) {
      joiner.add(String.valueOf(array[i]));
    }
    return joiner.toString();
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
   * StringUtils.join([1, 2, 3], null) = "123"
   * </pre>
   *
   * @param array     the array of values to join together, may be null
   * @param delimiter the separator character to use
   * @return the joined String, {@code null} if null array input
   */
  public static String join(double[] array, char delimiter) {
    if (array == null) {
      return null;
    }
    return join(array, delimiter, 0, array.length);
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
   * StringUtils.join([1, 2, 3], null) = "123"
   * </pre>
   *
   * @param array      the array of values to join together, may be null
   * @param delimiter  the separator character to use
   * @param startIndex the first index to start joining from. It is an error to pass in a start index past the end of the
   *                   array
   * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of
   *                   the array
   * @return the joined String, {@code null} if null array input
   */
  public static String join(double[] array, char delimiter, int startIndex, int endIndex) {
    if (array == null) {
      return null;
    }
    if (endIndex - startIndex <= 0) {
      return EMPTY;
    }
    StringJoiner joiner = newStringJoiner(delimiter);
    for (int i = startIndex; i < endIndex; i++) {
      joiner.add(String.valueOf(array[i]));
    }
    return joiner.toString();
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
   * StringUtils.join([1, 2, 3], null) = "123"
   * </pre>
   *
   * @param array     the array of values to join together, may be null
   * @param delimiter the separator character to use
   * @return the joined String, {@code null} if null array input
   */
  public static String join(float[] array, char delimiter) {
    if (array == null) {
      return null;
    }
    return join(array, delimiter, 0, array.length);
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
   * StringUtils.join([1, 2, 3], null) = "123"
   * </pre>
   *
   * @param array      the array of values to join together, may be null
   * @param delimiter  the separator character to use
   * @param startIndex the first index to start joining from. It is an error to pass in a start index past the end of the
   *                   array
   * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of
   *                   the array
   * @return the joined String, {@code null} if null array input
   */
  public static String join(float[] array, char delimiter, int startIndex, int endIndex) {
    if (array == null) {
      return null;
    }
    if (endIndex - startIndex <= 0) {
      return EMPTY;
    }
    StringJoiner joiner = newStringJoiner(delimiter);
    for (int i = startIndex; i < endIndex; i++) {
      joiner.add(String.valueOf(array[i]));
    }
    return joiner.toString();
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
   * StringUtils.join([1, 2, 3], null) = "123"
   * </pre>
   *
   * @param array     the array of values to join together, may be null
   * @param separator the separator character to use
   * @return the joined String, {@code null} if null array input
   */
  public static String join(int[] array, char separator) {
    if (array == null) {
      return null;
    }
    return join(array, separator, 0, array.length);
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
   * StringUtils.join([1, 2, 3], null) = "123"
   * </pre>
   *
   * @param array      the array of values to join together, may be null
   * @param delimiter  the separator character to use
   * @param startIndex the first index to start joining from. It is an error to pass in a start index past the end of the
   *                   array
   * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of
   *                   the array
   * @return the joined String, {@code null} if null array input
   */
  public static String join(int[] array, char delimiter, int startIndex, int endIndex) {
    if (array == null) {
      return null;
    }
    if (endIndex - startIndex <= 0) {
      return EMPTY;
    }
    StringJoiner joiner = newStringJoiner(delimiter);
    for (int i = startIndex; i < endIndex; i++) {
      joiner.add(String.valueOf(array[i]));
    }
    return joiner.toString();
  }

  /**
   * <p>Joins the elements of the provided {@code Iterable} into
   * a single String containing the provided elements.</p>
   *
   * <p>No delimiter is added before or after the list. Null objects or empty
   * strings within the iteration are represented by empty strings.</p>
   *
   * <p>See the examples here: {@link #join(Object[], char)}. </p>
   *
   * @param iterable  the {@code Iterable} providing the values to join together, may be null
   * @param separator the separator character to use
   * @return the joined String, {@code null} if null iterator input
   */
  public static String join(Iterable<?> iterable, char separator) {
    if (iterable == null) {
      return null;
    }
    return join(iterable.iterator(), separator);
  }

  /**
   * <p>Joins the elements of the provided {@code Iterable} into
   * a single String containing the provided elements.</p>
   *
   * <p>No delimiter is added before or after the list.
   * A {@code null} separator is the same as an empty String ("").</p>
   *
   * <p>See the examples here: {@link #join(Object[], String)}. </p>
   *
   * @param iterable  the {@code Iterable} providing the values to join together, may be null
   * @param separator the separator character to use, null treated as ""
   * @return the joined String, {@code null} if null iterator input
   */
  public static String join(Iterable<?> iterable, String separator) {
    if (iterable == null) {
      return null;
    }
    return join(iterable.iterator(), separator);
  }

  /**
   * <p>Joins the elements of the provided {@code Iterator} into
   * a single String containing the provided elements.</p>
   *
   * <p>No delimiter is added before or after the list. Null objects or empty
   * strings within the iteration are represented by empty strings.</p>
   *
   * <p>See the examples here: {@link #join(Object[], char)}. </p>
   *
   * @param iterator  the {@code Iterator} of values to join together, may be null
   * @param separator the separator character to use
   * @return the joined String, {@code null} if null iterator input
   */
  public static String join(Iterator<?> iterator, char separator) {

    // handle null, zero and one elements before building a buffer
    if (iterator == null) {
      return null;
    }
    if (!iterator.hasNext()) {
      return EMPTY;
    }
    Object first = iterator.next();
    if (!iterator.hasNext()) {
      return toStringOrEmpty(first);
    }

    // two or more elements
    StringBuilder buf = new StringBuilder(STRING_BUILDER_SIZE); // Java default is 16, probably too small
    if (first != null) {
      buf.append(first);
    }

    while (iterator.hasNext()) {
      buf.append(separator);
      Object obj = iterator.next();
      if (obj != null) {
        buf.append(obj);
      }
    }

    return buf.toString();
  }

  /**
   * <p>Joins the elements of the provided {@code Iterator} into
   * a single String containing the provided elements.</p>
   *
   * <p>No delimiter is added before or after the list.
   * A {@code null} separator is the same as an empty String ("").</p>
   *
   * <p>See the examples here: {@link #join(Object[], String)}. </p>
   *
   * @param iterator  the {@code Iterator} of values to join together, may be null
   * @param separator the separator character to use, null treated as ""
   * @return the joined String, {@code null} if null iterator input
   */
  public static String join(Iterator<?> iterator, String separator) {

    // handle null, zero and one elements before building a buffer
    if (iterator == null) {
      return null;
    }
    if (!iterator.hasNext()) {
      return EMPTY;
    }
    Object first = iterator.next();
    if (!iterator.hasNext()) {
      return Objects.toString(first, "");
    }

    // two or more elements
    StringBuilder buf = new StringBuilder(STRING_BUILDER_SIZE); // Java default is 16, probably too small
    if (first != null) {
      buf.append(first);
    }

    while (iterator.hasNext()) {
      if (separator != null) {
        buf.append(separator);
      }
      Object obj = iterator.next();
      if (obj != null) {
        buf.append(obj);
      }
    }
    return buf.toString();
  }

  /**
   * <p>Joins the elements of the provided {@code List} into a single String
   * containing the provided list of elements.</p>
   *
   * <p>No delimiter is added before or after the list.
   * Null objects or empty strings within the array are represented by
   * empty strings.</p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
   * StringUtils.join(["a", "b", "c"], null) = "abc"
   * StringUtils.join([null, "", "a"], ';')  = ";;a"
   * </pre>
   *
   * @param list       the {@code List} of values to join together, may be null
   * @param separator  the separator character to use
   * @param startIndex the first index to start joining from.  It is
   *                   an error to pass in a start index past the end of the list
   * @param endIndex   the index to stop joining from (exclusive). It is
   *                   an error to pass in an end index past the end of the list
   * @return the joined String, {@code null} if null list input
   */
  public static String join(List<?> list, char separator, int startIndex, int endIndex) {
    if (list == null) {
      return null;
    }
    int noOfItems = endIndex - startIndex;
    if (noOfItems <= 0) {
      return EMPTY;
    }
    List<?> subList = list.subList(startIndex, endIndex);
    return join(subList.iterator(), separator);
  }

  /**
   * <p>Joins the elements of the provided {@code List} into a single String
   * containing the provided list of elements.</p>
   *
   * <p>No delimiter is added before or after the list.
   * Null objects or empty strings within the array are represented by
   * empty strings.</p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
   * StringUtils.join(["a", "b", "c"], null) = "abc"
   * StringUtils.join([null, "", "a"], ';')  = ";;a"
   * </pre>
   *
   * @param list       the {@code List} of values to join together, may be null
   * @param separator  the separator character to use
   * @param startIndex the first index to start joining from.  It is
   *                   an error to pass in a start index past the end of the list
   * @param endIndex   the index to stop joining from (exclusive). It is
   *                   an error to pass in an end index past the end of the list
   * @return the joined String, {@code null} if null list input
   */
  public static String join(List<?> list, String separator, int startIndex, int endIndex) {
    if (list == null) {
      return null;
    }
    int noOfItems = endIndex - startIndex;
    if (noOfItems <= 0) {
      return EMPTY;
    }
    List<?> subList = list.subList(startIndex, endIndex);
    return join(subList.iterator(), separator);
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
   * StringUtils.join([1, 2, 3], null) = "123"
   * </pre>
   *
   * @param array     the array of values to join together, may be null
   * @param separator the separator character to use
   * @return the joined String, {@code null} if null array input
   */
  public static String join(long[] array, char separator) {
    if (array == null) {
      return null;
    }
    return join(array, separator, 0, array.length);
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
   * StringUtils.join([1, 2, 3], null) = "123"
   * </pre>
   *
   * @param array      the array of values to join together, may be null
   * @param delimiter  the separator character to use
   * @param startIndex the first index to start joining from. It is an error to pass in a start index past the end of the
   *                   array
   * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of
   *                   the array
   * @return the joined String, {@code null} if null array input
   */
  public static String join(long[] array, char delimiter, int startIndex, int endIndex) {
    if (array == null) {
      return null;
    }
    if (endIndex - startIndex <= 0) {
      return EMPTY;
    }
    StringJoiner joiner = newStringJoiner(delimiter);
    for (int i = startIndex; i < endIndex; i++) {
      joiner.add(String.valueOf(array[i]));
    }
    return joiner.toString();
  }

  /**
   * <p>Joins the elements of the provided array into a single String
   * containing the provided list of elements.</p>
   *
   * <p>No delimiter is added before or after the list.
   * Null objects or empty strings within the array are represented by
   * empty strings.</p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
   * StringUtils.join(["a", "b", "c"], null) = "abc"
   * StringUtils.join([null, "", "a"], ';')  = ";;a"
   * </pre>
   *
   * @param array     the array of values to join together, may be null
   * @param delimiter the separator character to use
   * @return the joined String, {@code null} if null array input
   */
  public static String join(Object[] array, char delimiter) {
    if (array == null) {
      return null;
    }
    return join(array, delimiter, 0, array.length);
  }

  /**
   * <p>Joins the elements of the provided array into a single String
   * containing the provided list of elements.</p>
   *
   * <p>No delimiter is added before or after the list.
   * Null objects or empty strings within the array are represented by
   * empty strings.</p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
   * StringUtils.join(["a", "b", "c"], null) = "abc"
   * StringUtils.join([null, "", "a"], ';')  = ";;a"
   * </pre>
   *
   * @param array      the array of values to join together, may be null
   * @param delimiter  the separator character to use
   * @param startIndex the first index to start joining from.  It is
   *                   an error to pass in a start index past the end of the array
   * @param endIndex   the index to stop joining from (exclusive). It is
   *                   an error to pass in an end index past the end of the array
   * @return the joined String, {@code null} if null array input
   */
  public static String join(Object[] array, char delimiter, int startIndex, int endIndex) {
    if (array == null) {
      return null;
    }
    if (endIndex - startIndex <= 0) {
      return EMPTY;
    }
    StringJoiner joiner = newStringJoiner(delimiter);
    for (int i = startIndex; i < endIndex; i++) {
      joiner.add(toStringOrEmpty(array[i]));
    }
    return joiner.toString();
  }

  /**
   * <p>Joins the elements of the provided array into a single String
   * containing the provided list of elements.</p>
   *
   * <p>No delimiter is added before or after the list.
   * A {@code null} separator is the same as an empty String ("").
   * Null objects or empty strings within the array are represented by
   * empty strings.</p>
   *
   * <pre>
   * StringUtils.join(null, *)                = null
   * StringUtils.join([], *)                  = ""
   * StringUtils.join([null], *)              = ""
   * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
   * StringUtils.join(["a", "b", "c"], null)  = "abc"
   * StringUtils.join(["a", "b", "c"], "")    = "abc"
   * StringUtils.join([null, "", "a"], ',')   = ",,a"
   * </pre>
   *
   * @param array     the array of values to join together, may be null
   * @param delimiter the separator character to use, null treated as ""
   * @return the joined String, {@code null} if null array input
   */
  public static String join(Object[] array, String delimiter) {
    if (array == null) {
      return null;
    }
    return join(array, delimiter, 0, array.length);
  }

  /**
   * <p>Joins the elements of the provided array into a single String
   * containing the provided list of elements.</p>
   *
   * <p>No delimiter is added before or after the list.
   * A {@code null} separator is the same as an empty String ("").
   * Null objects or empty strings within the array are represented by
   * empty strings.</p>
   *
   * <pre>
   * StringUtils.join(null, *, *, *)                = null
   * StringUtils.join([], *, *, *)                  = ""
   * StringUtils.join([null], *, *, *)              = ""
   * StringUtils.join(["a", "b", "c"], "--", 0, 3)  = "a--b--c"
   * StringUtils.join(["a", "b", "c"], "--", 1, 3)  = "b--c"
   * StringUtils.join(["a", "b", "c"], "--", 2, 3)  = "c"
   * StringUtils.join(["a", "b", "c"], "--", 2, 2)  = ""
   * StringUtils.join(["a", "b", "c"], null, 0, 3)  = "abc"
   * StringUtils.join(["a", "b", "c"], "", 0, 3)    = "abc"
   * StringUtils.join([null, "", "a"], ',', 0, 3)   = ",,a"
   * </pre>
   *
   * @param array      the array of values to join together, may be null
   * @param delimiter  the separator character to use, null treated as ""
   * @param startIndex the first index to start joining from.
   * @param endIndex   the index to stop joining from (exclusive).
   * @return the joined String, {@code null} if null array input; or the empty string
   * if {@code endIndex - startIndex <= 0}. The number of joined entries is given by
   * {@code endIndex - startIndex}
   * @throws ArrayIndexOutOfBoundsException ife<br>
   *                                        {@code startIndex < 0} or <br>
   *                                        {@code startIndex >= array.length()} or <br>
   *                                        {@code endIndex < 0} or <br>
   *                                        {@code endIndex > array.length()}
   */
  public static String join(Object[] array, String delimiter, int startIndex, int endIndex) {
    if (array == null) {
      return null;
    }
    if (endIndex - startIndex <= 0) {
      return EMPTY;
    }
    StringJoiner joiner = new StringJoiner(toStringOrEmpty(delimiter));
    for (int i = startIndex; i < endIndex; i++) {
      joiner.add(toStringOrEmpty(array[i]));
    }
    return joiner.toString();
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
   * StringUtils.join([1, 2, 3], null) = "123"
   * </pre>
   *
   * @param array     the array of values to join together, may be null
   * @param delimiter the separator character to use
   * @return the joined String, {@code null} if null array input
   */
  public static String join(short[] array, char delimiter) {
    if (array == null) {
      return null;
    }
    return join(array, delimiter, 0, array.length);
  }

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No delimiter is added before or after the list. Null objects or empty strings within the array are represented
   * by empty strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join([1, 2, 3], ';')  = "1;2;3"
   * StringUtils.join([1, 2, 3], null) = "123"
   * </pre>
   *
   * @param array      the array of values to join together, may be null
   * @param delimiter  the separator character to use
   * @param startIndex the first index to start joining from. It is an error to pass in a start index past the end of the
   *                   array
   * @param endIndex   the index to stop joining from (exclusive). It is an error to pass in an end index past the end of
   *                   the array
   * @return the joined String, {@code null} if null array input
   */
  public static String join(short[] array, char delimiter, int startIndex, int endIndex) {
    if (array == null) {
      return null;
    }
    if (endIndex - startIndex <= 0) {
      return EMPTY;
    }
    StringJoiner joiner = newStringJoiner(delimiter);
    for (int i = startIndex; i < endIndex; i++) {
      joiner.add(String.valueOf(array[i]));
    }
    return joiner.toString();
  }

  /**
   * <p>Joins the elements of the provided array into a single String
   * containing the provided list of elements.</p>
   *
   * <p>No separator is added to the joined String.
   * Null objects or empty strings within the array are represented by
   * empty strings.</p>
   *
   * <pre>
   * StringUtils.join(null)            = null
   * StringUtils.join([])              = ""
   * StringUtils.join([null])          = ""
   * StringUtils.join(["a", "b", "c"]) = "abc"
   * StringUtils.join([null, "", "a"]) = "a"
   * </pre>
   *
   * @param <T>      the specific type of values to join together
   * @param elements the values to join together, may be null
   * @return the joined String, {@code null} if null array input
   */
  @SafeVarargs
  public static <T> String join(T... elements) {
    return join(elements, null);
  }

  /**
   * <p>Joins the elements of the provided varargs into a
   * single String containing the provided elements.</p>
   *
   * <p>No delimiter is added before or after the list.
   * {@code null} elements and separator are treated as empty Strings ("").</p>
   *
   * <pre>
   * StringUtils.joinWith(",", {"a", "b"})        = "a,b"
   * StringUtils.joinWith(",", {"a", "b",""})     = "a,b,"
   * StringUtils.joinWith(",", {"a", null, "b"})  = "a,,b"
   * StringUtils.joinWith(null, {"a", "b"})       = "ab"
   * </pre>
   *
   * @param delimiter the separator character to use, null treated as ""
   * @param array     the varargs providing the values to join together. {@code null} elements are treated as ""
   * @return the joined String.
   * @throws IllegalArgumentException if a null varargs is provided
   */
  public static String joinWith(String delimiter, Object... array) {
    if (array == null) {
      throw new IllegalArgumentException("Object varargs must not be null");
    }
    return join(array, delimiter);
  }

  /**
   * <p>Finds the last index within a CharSequence, handling {@code null}.
   * This method uses {@link String#lastIndexOf(String)} if possible.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.</p>
   *
   * <pre>
   * StringUtils.lastIndexOf(null, *)          = -1
   * StringUtils.lastIndexOf(*, null)          = -1
   * StringUtils.lastIndexOf("", "")           = 0
   * StringUtils.lastIndexOf("aabaabaa", "a")  = 7
   * StringUtils.lastIndexOf("aabaabaa", "b")  = 5
   * StringUtils.lastIndexOf("aabaabaa", "ab") = 4
   * StringUtils.lastIndexOf("aabaabaa", "")   = 8
   * </pre>
   *
   * @param seq       the CharSequence to check, may be null
   * @param searchSeq the CharSequence to find, may be null
   * @return the last index of the search String,
   * -1 if no match or {@code null} string input
   */
  public static int lastIndexOf(CharSequence seq, CharSequence searchSeq) {
    if (seq == null) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.lastIndexOf(seq, searchSeq, seq.length());
  }

  /**
   * <p>Finds the last index within a CharSequence, handling {@code null}.
   * This method uses {@link String#lastIndexOf(String, int)} if possible.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A negative start position returns {@code -1}.
   * An empty ("") search CharSequence always matches unless the start position is negative.
   * A start position greater than the string length searches the whole string.
   * The search starts at the startPos and works backwards; matches starting after the start
   * position are ignored.
   * </p>
   *
   * <pre>
   * StringUtils.lastIndexOf(null, *, *)          = -1
   * StringUtils.lastIndexOf(*, null, *)          = -1
   * StringUtils.lastIndexOf("aabaabaa", "a", 8)  = 7
   * StringUtils.lastIndexOf("aabaabaa", "b", 8)  = 5
   * StringUtils.lastIndexOf("aabaabaa", "ab", 8) = 4
   * StringUtils.lastIndexOf("aabaabaa", "b", 9)  = 5
   * StringUtils.lastIndexOf("aabaabaa", "b", -1) = -1
   * StringUtils.lastIndexOf("aabaabaa", "a", 0)  = 0
   * StringUtils.lastIndexOf("aabaabaa", "b", 0)  = -1
   * StringUtils.lastIndexOf("aabaabaa", "b", 1)  = -1
   * StringUtils.lastIndexOf("aabaabaa", "b", 2)  = 2
   * StringUtils.lastIndexOf("aabaabaa", "ba", 2)  = 2
   * </pre>
   *
   * @param seq       the CharSequence to check, may be null
   * @param searchSeq the CharSequence to find, may be null
   * @param startPos  the start position, negative treated as zero
   * @return the last index of the search CharSequence (always &le; startPos),
   * -1 if no match or {@code null} string input
   */
  public static int lastIndexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
    return CharSequenceUtils.lastIndexOf(seq, searchSeq, startPos);
  }

  /**
   * Returns the index within {@code seq} of the last occurrence of
   * the specified character. For values of {@code searchChar} in the
   * range from 0 to 0xFFFF (inclusive), the index (in Unicode code
   * units) returned is the largest value <i>k</i> such that:
   * <blockquote><pre>
   * this.charAt(<i>k</i>) == searchChar
   * </pre></blockquote>
   * is true. For other values of {@code searchChar}, it is the
   * largest value <i>k</i> such that:
   * <blockquote><pre>
   * this.codePointAt(<i>k</i>) == searchChar
   * </pre></blockquote>
   * is true.  In either case, if no such character occurs in this
   * string, then {@code -1} is returned. Furthermore, a {@code null} or empty ("")
   * {@code CharSequence} will return {@code -1}. The
   * {@code seq} {@code CharSequence} object is searched backwards
   * starting at the last character.
   *
   * <pre>
   * StringUtils.lastIndexOf(null, *)         = -1
   * StringUtils.lastIndexOf("", *)           = -1
   * StringUtils.lastIndexOf("aabaabaa", 'a') = 7
   * StringUtils.lastIndexOf("aabaabaa", 'b') = 5
   * </pre>
   *
   * @param seq        the {@code CharSequence} to check, may be null
   * @param searchChar the character to find
   * @return the last index of the search character,
   * -1 if no match or {@code null} string input
   */
  public static int lastIndexOf(CharSequence seq, int searchChar) {
    if (isEmpty(seq)) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.lastIndexOf(seq, searchChar, seq.length());
  }

  /**
   * Returns the index within {@code seq} of the last occurrence of
   * the specified character, searching backward starting at the
   * specified index. For values of {@code searchChar} in the range
   * from 0 to 0xFFFF (inclusive), the index returned is the largest
   * value <i>k</i> such that:
   * <blockquote><pre>
   * (this.charAt(<i>k</i>) == searchChar) &amp;&amp; (<i>k</i> &lt;= startPos)
   * </pre></blockquote>
   * is true. For other values of {@code searchChar}, it is the
   * largest value <i>k</i> such that:
   * <blockquote><pre>
   * (this.codePointAt(<i>k</i>) == searchChar) &amp;&amp; (<i>k</i> &lt;= startPos)
   * </pre></blockquote>
   * is true. In either case, if no such character occurs in {@code seq}
   * at or before position {@code startPos}, then
   * {@code -1} is returned. Furthermore, a {@code null} or empty ("")
   * {@code CharSequence} will return {@code -1}. A start position greater
   * than the string length searches the whole string.
   * The search starts at the {@code startPos} and works backwards;
   * matches starting after the start position are ignored.
   *
   * <p>All indices are specified in {@code char} values
   * (Unicode code units).
   *
   * <pre>
   * StringUtils.lastIndexOf(null, *, *)          = -1
   * StringUtils.lastIndexOf("", *,  *)           = -1
   * StringUtils.lastIndexOf("aabaabaa", 'b', 8)  = 5
   * StringUtils.lastIndexOf("aabaabaa", 'b', 4)  = 2
   * StringUtils.lastIndexOf("aabaabaa", 'b', 0)  = -1
   * StringUtils.lastIndexOf("aabaabaa", 'b', 9)  = 5
   * StringUtils.lastIndexOf("aabaabaa", 'b', -1) = -1
   * StringUtils.lastIndexOf("aabaabaa", 'a', 0)  = 0
   * </pre>
   *
   * @param seq        the CharSequence to check, may be null
   * @param searchChar the character to find
   * @param startPos   the start position
   * @return the last index of the search character (always &le; startPos),
   * -1 if no match or {@code null} string input
   */
  public static int lastIndexOf(CharSequence seq, int searchChar, int startPos) {
    if (isEmpty(seq)) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.lastIndexOf(seq, searchChar, startPos);
  }

  /**
   * <p>Find the latest index of any substring in a set of potential substrings.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A {@code null} search array will return {@code -1}.
   * A {@code null} or zero length search array entry will be ignored,
   * but a search array containing "" will return the length of {@code str}
   * if {@code str} is not null. This method uses {@link String#indexOf(String)} if possible</p>
   *
   * <pre>
   * StringUtils.lastIndexOfAny(null, *)                    = -1
   * StringUtils.lastIndexOfAny(*, null)                    = -1
   * StringUtils.lastIndexOfAny(*, [])                      = -1
   * StringUtils.lastIndexOfAny(*, [null])                  = -1
   * StringUtils.lastIndexOfAny("zzabyycdxx", ["ab", "cd"]) = 6
   * StringUtils.lastIndexOfAny("zzabyycdxx", ["cd", "ab"]) = 6
   * StringUtils.lastIndexOfAny("zzabyycdxx", ["mn", "op"]) = -1
   * StringUtils.lastIndexOfAny("zzabyycdxx", ["mn", "op"]) = -1
   * StringUtils.lastIndexOfAny("zzabyycdxx", ["mn", ""])   = 10
   * </pre>
   *
   * @param str        the CharSequence to check, may be null
   * @param searchStrs the CharSequences to search for, may be null
   * @return the last index of any of the CharSequences, -1 if no match
   */
  public static int lastIndexOfAny(CharSequence str, CharSequence... searchStrs) {
    if (str == null || searchStrs == null) {
      return INDEX_NOT_FOUND;
    }
    int ret = INDEX_NOT_FOUND;
    int tmp;
    for (CharSequence search : searchStrs) {
      if (search == null) {
        continue;
      }
      tmp = CharSequenceUtils.lastIndexOf(str, search, str.length());
      if (tmp > ret) {
        ret = tmp;
      }
    }
    return ret;
  }

  /**
   * <p>Case in-sensitive find of the last index within a CharSequence.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A negative start position returns {@code -1}.
   * An empty ("") search CharSequence always matches unless the start position is negative.
   * A start position greater than the string length searches the whole string.</p>
   *
   * <pre>
   * StringUtils.lastIndexOfIgnoreCase(null, *)          = -1
   * StringUtils.lastIndexOfIgnoreCase(*, null)          = -1
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A")  = 7
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B")  = 5
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "AB") = 4
   * </pre>
   *
   * @param str       the CharSequence to check, may be null
   * @param searchStr the CharSequence to find, may be null
   * @return the first index of the search CharSequence,
   * -1 if no match or {@code null} string input
   */
  public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
    if (str == null || searchStr == null) {
      return INDEX_NOT_FOUND;
    }
    return lastIndexOfIgnoreCase(str, searchStr, str.length());
  }

  /**
   * <p>Case in-sensitive find of the last index within a CharSequence
   * from the specified position.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A negative start position returns {@code -1}.
   * An empty ("") search CharSequence always matches unless the start position is negative.
   * A start position greater than the string length searches the whole string.
   * The search starts at the startPos and works backwards; matches starting after the start
   * position are ignored.
   * </p>
   *
   * <pre>
   * StringUtils.lastIndexOfIgnoreCase(null, *, *)          = -1
   * StringUtils.lastIndexOfIgnoreCase(*, null, *)          = -1
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A", 8)  = 7
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 8)  = 5
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "AB", 8) = 4
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 9)  = 5
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", -1) = -1
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A", 0)  = 0
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 0)  = -1
   * </pre>
   *
   * @param str       the CharSequence to check, may be null
   * @param searchStr the CharSequence to find, may be null
   * @param startPos  the start position
   * @return the last index of the search CharSequence (always &le; startPos),
   * -1 if no match or {@code null} input
   */
  public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
    if (str == null || searchStr == null) {
      return INDEX_NOT_FOUND;
    }
    int searchStrLength = searchStr.length();
    int strLength = str.length();
    if (startPos > strLength - searchStrLength) {
      startPos = strLength - searchStrLength;
    }
    if (startPos < 0) {
      return INDEX_NOT_FOUND;
    }
    if (searchStrLength == 0) {
      return startPos;
    }

    for (int i = startPos; i >= 0; i--) {
      if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStrLength)) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }

  /**
   * <p>Finds the n-th last index within a String, handling {@code null}.
   * This method uses {@link String#lastIndexOf(String)}.</p>
   *
   * <p>A {@code null} String will return {@code -1}.</p>
   *
   * <pre>
   * StringUtils.lastOrdinalIndexOf(null, *, *)          = -1
   * StringUtils.lastOrdinalIndexOf(*, null, *)          = -1
   * StringUtils.lastOrdinalIndexOf("", "", *)           = 0
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "a", 1)  = 7
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "a", 2)  = 6
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "b", 1)  = 5
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "b", 2)  = 2
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "ab", 1) = 4
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "ab", 2) = 1
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "", 1)   = 8
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "", 2)   = 8
   * </pre>
   *
   * <p>Note that 'tail(CharSequence str, int n)' may be implemented as: </p>
   *
   * <pre>
   *   str.substring(lastOrdinalIndexOf(str, "\n", n) + 1)
   * </pre>
   *
   * @param str       the CharSequence to check, may be null
   * @param searchStr the CharSequence to find, may be null
   * @param ordinal   the n-th last {@code searchStr} to find
   * @return the n-th last index of the search CharSequence,
   * {@code -1} ({@code INDEX_NOT_FOUND}) if no match or {@code null} string input
   */
  public static int lastOrdinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
    return ordinalIndexOf(str, searchStr, ordinal, true);
  }

  /**
   * <p>Gets the leftmost {@code len} characters of a String.</p>
   *
   * <p>If {@code len} characters are not available, or the
   * String is {@code null}, the String will be returned without
   * an exception. An empty String is returned if len is negative.</p>
   *
   * <pre>
   * StringUtils.left(null, *)    = null
   * StringUtils.left(*, -ve)     = ""
   * StringUtils.left("", *)      = ""
   * StringUtils.left("abc", 0)   = ""
   * StringUtils.left("abc", 2)   = "ab"
   * StringUtils.left("abc", 4)   = "abc"
   * </pre>
   *
   * @param str the String to get the leftmost characters from, may be null
   * @param len the length of the required String
   * @return the leftmost characters, {@code null} if null String input
   */
  public static String left(String str, int len) {
    if (str == null) {
      return null;
    }
    if (len < 0) {
      return EMPTY;
    }
    if (str.length() <= len) {
      return str;
    }
    return str.substring(0, len);
  }

  /**
   * <p>Left pad a String with spaces (' ').</p>
   *
   * <p>The String is padded to the size of {@code size}.</p>
   *
   * <pre>
   * StringUtils.leftPad(null, *)   = null
   * StringUtils.leftPad("", 3)     = "   "
   * StringUtils.leftPad("bat", 3)  = "bat"
   * StringUtils.leftPad("bat", 5)  = "  bat"
   * StringUtils.leftPad("bat", 1)  = "bat"
   * StringUtils.leftPad("bat", -1) = "bat"
   * </pre>
   *
   * @param str  the String to pad out, may be null
   * @param size the size to pad to
   * @return left padded String or original String if no padding is necessary,
   * {@code null} if null String input
   */
  public static String leftPad(String str, int size) {
    return leftPad(str, size, ' ');
  }

  /**
   * <p>Left pad a String with a specified character.</p>
   *
   * <p>Pad to a size of {@code size}.</p>
   *
   * <pre>
   * StringUtils.leftPad(null, *, *)     = null
   * StringUtils.leftPad("", 3, 'z')     = "zzz"
   * StringUtils.leftPad("bat", 3, 'z')  = "bat"
   * StringUtils.leftPad("bat", 5, 'z')  = "zzbat"
   * StringUtils.leftPad("bat", 1, 'z')  = "bat"
   * StringUtils.leftPad("bat", -1, 'z') = "bat"
   * </pre>
   *
   * @param str     the String to pad out, may be null
   * @param size    the size to pad to
   * @param padChar the character to pad with
   * @return left padded String or original String if no padding is necessary,
   * {@code null} if null String input
   */
  public static String leftPad(String str, int size, char padChar) {
    if (str == null) {
      return null;
    }
    int pads = size - str.length();
    if (pads <= 0) {
      return str; // returns original String when possible
    }
    if (pads > PAD_LIMIT) {
      return leftPad(str, size, String.valueOf(padChar));
    }
    return repeat(padChar, pads).concat(str);
  }

  /**
   * <p>Left pad a String with a specified String.</p>
   *
   * <p>Pad to a size of {@code size}.</p>
   *
   * <pre>
   * StringUtils.leftPad(null, *, *)      = null
   * StringUtils.leftPad("", 3, "z")      = "zzz"
   * StringUtils.leftPad("bat", 3, "yz")  = "bat"
   * StringUtils.leftPad("bat", 5, "yz")  = "yzbat"
   * StringUtils.leftPad("bat", 8, "yz")  = "yzyzybat"
   * StringUtils.leftPad("bat", 1, "yz")  = "bat"
   * StringUtils.leftPad("bat", -1, "yz") = "bat"
   * StringUtils.leftPad("bat", 5, null)  = "  bat"
   * StringUtils.leftPad("bat", 5, "")    = "  bat"
   * </pre>
   *
   * @param str    the String to pad out, may be null
   * @param size   the size to pad to
   * @param padStr the String to pad with, null or empty treated as single space
   * @return left padded String or original String if no padding is necessary,
   * {@code null} if null String input
   */
  public static String leftPad(String str, int size, String padStr) {
    if (str == null) {
      return null;
    }
    if (isEmpty(padStr)) {
      padStr = SPACE;
    }
    int padLen = padStr.length();
    int strLen = str.length();
    int pads = size - strLen;
    if (pads <= 0) {
      return str; // returns original String when possible
    }
    if (padLen == 1 && pads <= PAD_LIMIT) {
      return leftPad(str, size, padStr.charAt(0));
    }

    if (pads == padLen) {
      return padStr.concat(str);
    } else if (pads < padLen) {
      return padStr.substring(0, pads).concat(str);
    } else {
      char[] padding = new char[pads];
      char[] padChars = padStr.toCharArray();
      for (int i = 0; i < pads; i++) {
        padding[i] = padChars[i % padLen];
      }
      return new String(padding).concat(str);
    }
  }

  /**
   * Gets a CharSequence length or {@code 0} if the CharSequence is
   * {@code null}.
   *
   * @param cs a CharSequence or {@code null}
   * @return CharSequence length or {@code 0} if the CharSequence is
   * {@code null}.
   */
  public static int length(CharSequence cs) {
    return cs == null ? 0 : cs.length();
  }

  /**
   * <p>Converts a String to lower case as per {@link String#toLowerCase()}.</p>
   *
   * <p>A {@code null} input String returns {@code null}.</p>
   *
   * <pre>
   * StringUtils.lowerCase(null)  = null
   * StringUtils.lowerCase("")    = ""
   * StringUtils.lowerCase("aBc") = "abc"
   * </pre>
   *
   * <p><strong>Note:</strong> As described in the documentation for {@link String#toLowerCase()},
   * the result of this method is affected by the current locale.
   * For platform-independent case transformations, the method {@link #lowerCase(String, Locale)}
   * should be used with a specific locale (e.g. {@link Locale#ENGLISH}).</p>
   *
   * @param str the String to lower case, may be null
   * @return the lower cased String, {@code null} if null String input
   */
  public static String lowerCase(String str) {
    if (str == null) {
      return null;
    }
    return str.toLowerCase();
  }

  /**
   * <p>Converts a String to lower case as per {@link String#toLowerCase(Locale)}.</p>
   *
   * <p>A {@code null} input String returns {@code null}.</p>
   *
   * <pre>
   * StringUtils.lowerCase(null, Locale.ENGLISH)  = null
   * StringUtils.lowerCase("", Locale.ENGLISH)    = ""
   * StringUtils.lowerCase("aBc", Locale.ENGLISH) = "abc"
   * </pre>
   *
   * @param str    the String to lower case, may be null
   * @param locale the locale that defines the case transformation rules, must not be null
   * @return the lower cased String, {@code null} if null String input
   */
  public static String lowerCase(String str, Locale locale) {
    if (str == null) {
      return null;
    }
    return str.toLowerCase(locale);
  }

  private static int[] matches(CharSequence first, CharSequence second) {
    CharSequence max;
    CharSequence min;
    if (first.length() > second.length()) {
      max = first;
      min = second;
    } else {
      max = second;
      min = first;
    }
    int range = Math.max(max.length() / 2 - 1, 0);
    int[] matchIndexes = new int[min.length()];
    Arrays.fill(matchIndexes, -1);
    boolean[] matchFlags = new boolean[max.length()];
    int matches = 0;
    for (int mi = 0; mi < min.length(); mi++) {
      char c1 = min.charAt(mi);
      for (int xi = Math.max(mi - range, 0), xn = Math.min(mi + range + 1, max.length()); xi < xn; xi++) {
        if (!matchFlags[xi] && c1 == max.charAt(xi)) {
          matchIndexes[mi] = xi;
          matchFlags[xi] = true;
          matches++;
          break;
        }
      }
    }
    char[] ms1 = new char[matches];
    char[] ms2 = new char[matches];
    for (int i = 0, si = 0; i < min.length(); i++) {
      if (matchIndexes[i] != -1) {
        ms1[si] = min.charAt(i);
        si++;
      }
    }
    for (int i = 0, si = 0; i < max.length(); i++) {
      if (matchFlags[i]) {
        ms2[si] = max.charAt(i);
        si++;
      }
    }
    int transpositions = 0;
    for (int mi = 0; mi < ms1.length; mi++) {
      if (ms1[mi] != ms2[mi]) {
        transpositions++;
      }
    }
    int prefix = 0;
    for (int mi = 0; mi < min.length(); mi++) {
      if (first.charAt(mi) == second.charAt(mi)) {
        prefix++;
      } else {
        break;
      }
    }
    return new int[]{matches, transpositions / 2, prefix, max.length()};
  }

  /**
   * <p>Gets {@code len} characters from the middle of a String.</p>
   *
   * <p>If {@code len} characters are not available, the remainder
   * of the String will be returned without an exception. If the
   * String is {@code null}, {@code null} will be returned.
   * An empty String is returned if len is negative or exceeds the
   * length of {@code str}.</p>
   *
   * <pre>
   * StringUtils.mid(null, *, *)    = null
   * StringUtils.mid(*, *, -ve)     = ""
   * StringUtils.mid("", 0, *)      = ""
   * StringUtils.mid("abc", 0, 2)   = "ab"
   * StringUtils.mid("abc", 0, 4)   = "abc"
   * StringUtils.mid("abc", 2, 4)   = "c"
   * StringUtils.mid("abc", 4, 2)   = ""
   * StringUtils.mid("abc", -2, 2)  = "ab"
   * </pre>
   *
   * @param str the String to get the characters from, may be null
   * @param pos the position to start from, negative treated as zero
   * @param len the length of the required String
   * @return the middle characters, {@code null} if null String input
   */
  public static String mid(String str, int pos, int len) {
    if (str == null) {
      return null;
    }
    if (len < 0 || pos > str.length()) {
      return EMPTY;
    }
    if (pos < 0) {
      pos = 0;
    }
    if (str.length() <= pos + len) {
      return str.substring(pos);
    }
    return str.substring(pos, pos + len);
  }

  private static StringJoiner newStringJoiner(char delimiter) {
    return new StringJoiner(String.valueOf(delimiter));
  }

  /**
   * <p>
   * Similar to <a
   * href="http://www.w3.org/TR/xpath/#function-normalize-space">http://www.w3.org/TR/xpath/#function-normalize
   * -space</a>
   * </p>
   * <p>
   * The function returns the argument string with whitespace normalized by using
   * {@code {@link #trim(String)}} to remove leading and trailing whitespace
   * and then replacing sequences of whitespace characters by a single space.
   * </p>
   * In XML Whitespace characters are the same as those allowed by the <a
   * href="http://www.w3.org/TR/REC-xml/#NT-S">S</a> production, which is S ::= (#x20 | #x9 | #xD | #xA)+
   * <p>
   * Java's regexp pattern \s defines whitespace as [ \t\n\x0B\f\r]
   *
   * <p>For reference:</p>
   * <ul>
   * <li>\x0B = vertical tab</li>
   * <li>\f = #xC = form feed</li>
   * <li>#x20 = space</li>
   * <li>#x9 = \t</li>
   * <li>#xA = \n</li>
   * <li>#xD = \r</li>
   * </ul>
   *
   * <p>
   * The difference is that Java's whitespace includes vertical tab and form feed, which this functional will also
   * normalize. Additionally {@code {@link #trim(String)}} removes control characters (char &lt;= 32) from both
   * ends of this String.
   * </p>
   *
   * @param str the source String to normalize whitespaces from, may be null
   * @return the modified string with whitespace normalized, {@code null} if null String input
   * @see Pattern
   * @see #trim(String)
   * @see <a
   * href="http://www.w3.org/TR/xpath/#function-normalize-space">http://www.w3.org/TR/xpath/#function-normalize-space</a>
   */
  public static String normalizeSpace(String str) {
    // LANG-1020: Improved performance significantly by normalizing manually instead of using regex
    // See https://github.com/librucha/commons-lang-normalizespaces-benchmark for performance test
    if (isEmpty(str)) {
      return str;
    }
    int size = str.length();
    char[] newChars = new char[size];
    int count = 0;
    int whitespacesCount = 0;
    boolean startWhitespaces = true;
    for (int i = 0; i < size; i++) {
      char actualChar = str.charAt(i);
      boolean isWhitespace = Character.isWhitespace(actualChar);
      if (isWhitespace) {
        if (whitespacesCount == 0 && !startWhitespaces) {
          newChars[count++] = SPACE.charAt(0);
        }
        whitespacesCount++;
      } else {
        startWhitespaces = false;
        newChars[count++] = (actualChar == 160 ? 32 : actualChar);
        whitespacesCount = 0;
      }
    }
    if (startWhitespaces) {
      return EMPTY;
    }
    return new String(newChars, 0, count - (whitespacesCount > 0 ? 1 : 0)).trim();
  }

  /**
   * <p>Finds the n-th index within a CharSequence, handling {@code null}.
   * This method uses {@link String#indexOf(String)} if possible.</p>
   * <p><b>Note:</b> The code starts looking for a match at the start of the target,
   * incrementing the starting index by one after each successful match
   * (unless {@code searchStr} is an empty string in which case the position
   * is never incremented and {@code 0} is returned immediately).
   * This means that matches may overlap.</p>
   * <p>A {@code null} CharSequence will return {@code -1}.</p>
   *
   * <pre>
   * StringUtils.ordinalIndexOf(null, *, *)          = -1
   * StringUtils.ordinalIndexOf(*, null, *)          = -1
   * StringUtils.ordinalIndexOf("", "", *)           = 0
   * StringUtils.ordinalIndexOf("aabaabaa", "a", 1)  = 0
   * StringUtils.ordinalIndexOf("aabaabaa", "a", 2)  = 1
   * StringUtils.ordinalIndexOf("aabaabaa", "b", 1)  = 2
   * StringUtils.ordinalIndexOf("aabaabaa", "b", 2)  = 5
   * StringUtils.ordinalIndexOf("aabaabaa", "ab", 1) = 1
   * StringUtils.ordinalIndexOf("aabaabaa", "ab", 2) = 4
   * StringUtils.ordinalIndexOf("aabaabaa", "", 1)   = 0
   * StringUtils.ordinalIndexOf("aabaabaa", "", 2)   = 0
   * </pre>
   *
   * <p>Matches may overlap:</p>
   * <pre>
   * StringUtils.ordinalIndexOf("ababab", "aba", 1)   = 0
   * StringUtils.ordinalIndexOf("ababab", "aba", 2)   = 2
   * StringUtils.ordinalIndexOf("ababab", "aba", 3)   = -1
   *
   * StringUtils.ordinalIndexOf("abababab", "abab", 1) = 0
   * StringUtils.ordinalIndexOf("abababab", "abab", 2) = 2
   * StringUtils.ordinalIndexOf("abababab", "abab", 3) = 4
   * StringUtils.ordinalIndexOf("abababab", "abab", 4) = -1
   * </pre>
   *
   * <p>Note that 'head(CharSequence str, int n)' may be implemented as: </p>
   *
   * <pre>
   *   str.substring(0, lastOrdinalIndexOf(str, "\n", n))
   * </pre>
   *
   * @param str       the CharSequence to check, may be null
   * @param searchStr the CharSequence to find, may be null
   * @param ordinal   the n-th {@code searchStr} to find
   * @return the n-th index of the search CharSequence,
   * {@code -1} ({@code INDEX_NOT_FOUND}) if no match or {@code null} string input
   */
  public static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
    return ordinalIndexOf(str, searchStr, ordinal, false);
  }

  /**
   * <p>Finds the n-th index within a String, handling {@code null}.
   * This method uses {@link String#indexOf(String)} if possible.</p>
   * <p>Note that matches may overlap<p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.</p>
   *
   * @param str       the CharSequence to check, may be null
   * @param searchStr the CharSequence to find, may be null
   * @param ordinal   the n-th {@code searchStr} to find, overlapping matches are allowed.
   * @param lastIndex true if lastOrdinalIndexOf() otherwise false if ordinalIndexOf()
   * @return the n-th index of the search CharSequence,
   * {@code -1} ({@code INDEX_NOT_FOUND}) if no match or {@code null} string input
   */
  // Shared code between ordinalIndexOf(String, String, int) and lastOrdinalIndexOf(String, String, int)
  private static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal, boolean lastIndex) {
    if (str == null || searchStr == null || ordinal <= 0) {
      return INDEX_NOT_FOUND;
    }
    if (searchStr.length() == 0) {
      return lastIndex ? str.length() : 0;
    }
    int found = 0;
    // set the initial index beyond the end of the string
    // this is to allow for the initial index decrement/increment
    int index = lastIndex ? str.length() : INDEX_NOT_FOUND;
    do {
      if (lastIndex) {
        index = CharSequenceUtils.lastIndexOf(str, searchStr, index - 1); // step backwards thru string
      } else {
        index = CharSequenceUtils.indexOf(str, searchStr, index + 1); // step forwards through string
      }
      if (index < 0) {
        return index;
      }
      found++;
    } while (found < ordinal);
    return index;
  }

  /**
   * <p>Overlays part of a String with another String.</p>
   *
   * <p>A {@code null} string input returns {@code null}.
   * A negative index is treated as zero.
   * An index greater than the string length is treated as the string length.
   * The start index is always the smaller of the two indices.</p>
   *
   * <pre>
   * StringUtils.overlay(null, *, *, *)            = null
   * StringUtils.overlay("", "abc", 0, 0)          = "abc"
   * StringUtils.overlay("abcdef", null, 2, 4)     = "abef"
   * StringUtils.overlay("abcdef", "", 2, 4)       = "abef"
   * StringUtils.overlay("abcdef", "", 4, 2)       = "abef"
   * StringUtils.overlay("abcdef", "zzzz", 2, 4)   = "abzzzzef"
   * StringUtils.overlay("abcdef", "zzzz", 4, 2)   = "abzzzzef"
   * StringUtils.overlay("abcdef", "zzzz", -1, 4)  = "zzzzef"
   * StringUtils.overlay("abcdef", "zzzz", 2, 8)   = "abzzzz"
   * StringUtils.overlay("abcdef", "zzzz", -2, -3) = "zzzzabcdef"
   * StringUtils.overlay("abcdef", "zzzz", 8, 10)  = "abcdefzzzz"
   * </pre>
   *
   * @param str     the String to do overlaying in, may be null
   * @param overlay the String to overlay, may be null
   * @param start   the position to start overlaying at
   * @param end     the position to stop overlaying before
   * @return overlayed String, {@code null} if null String input
   */
  public static String overlay(String str, String overlay, int start, int end) {
    if (str == null) {
      return null;
    }
    if (overlay == null) {
      overlay = EMPTY;
    }
    int len = str.length();
    if (start < 0) {
      start = 0;
    }
    if (start > len) {
      start = len;
    }
    if (end < 0) {
      end = 0;
    }
    if (end > len) {
      end = len;
    }
    if (start > end) {
      int temp = start;
      start = end;
      end = temp;
    }
    return str.substring(0, start) +
      overlay +
      str.substring(end);
  }

  /**
   * Prepends the prefix to the start of the string if the string does not
   * already start with any of the prefixes.
   *
   * @param str        The string.
   * @param prefix     The prefix to prepend to the start of the string.
   * @param ignoreCase Indicates whether the compare should ignore case.
   * @param prefixes   Additional prefixes that are valid (optional).
   * @return A new String if prefix was prepended, the same string otherwise.
   */
  private static String prependIfMissing(String str, CharSequence prefix, boolean ignoreCase, CharSequence... prefixes) {
    if (str == null || isEmpty(prefix) || startsWith(str, prefix, ignoreCase)) {
      return str;
    }
    if (ArrayUtils.isNotEmpty(prefixes)) {
      for (CharSequence p : prefixes) {
        if (startsWith(str, p, ignoreCase)) {
          return str;
        }
      }
    }
    return prefix + str;
  }

  /**
   * Prepends the prefix to the start of the string if the string does not
   * already start with any of the prefixes.
   *
   * <pre>
   * StringUtils.prependIfMissing(null, null) = null
   * StringUtils.prependIfMissing("abc", null) = "abc"
   * StringUtils.prependIfMissing("", "xyz") = "xyz"
   * StringUtils.prependIfMissing("abc", "xyz") = "xyzabc"
   * StringUtils.prependIfMissing("xyzabc", "xyz") = "xyzabc"
   * StringUtils.prependIfMissing("XYZabc", "xyz") = "xyzXYZabc"
   * </pre>
   * <p>With additional prefixes,</p>
   * <pre>
   * StringUtils.prependIfMissing(null, null, null) = null
   * StringUtils.prependIfMissing("abc", null, null) = "abc"
   * StringUtils.prependIfMissing("", "xyz", null) = "xyz"
   * StringUtils.prependIfMissing("abc", "xyz", new CharSequence[]{null}) = "xyzabc"
   * StringUtils.prependIfMissing("abc", "xyz", "") = "abc"
   * StringUtils.prependIfMissing("abc", "xyz", "mno") = "xyzabc"
   * StringUtils.prependIfMissing("xyzabc", "xyz", "mno") = "xyzabc"
   * StringUtils.prependIfMissing("mnoabc", "xyz", "mno") = "mnoabc"
   * StringUtils.prependIfMissing("XYZabc", "xyz", "mno") = "xyzXYZabc"
   * StringUtils.prependIfMissing("MNOabc", "xyz", "mno") = "xyzMNOabc"
   * </pre>
   *
   * @param str      The string.
   * @param prefix   The prefix to prepend to the start of the string.
   * @param prefixes Additional prefixes that are valid.
   * @return A new String if prefix was prepended, the same string otherwise.
   */
  public static String prependIfMissing(String str, CharSequence prefix, CharSequence... prefixes) {
    return prependIfMissing(str, prefix, false, prefixes);
  }

  /**
   * Prepends the prefix to the start of the string if the string does not
   * already start, case insensitive, with any of the prefixes.
   *
   * <pre>
   * StringUtils.prependIfMissingIgnoreCase(null, null) = null
   * StringUtils.prependIfMissingIgnoreCase("abc", null) = "abc"
   * StringUtils.prependIfMissingIgnoreCase("", "xyz") = "xyz"
   * StringUtils.prependIfMissingIgnoreCase("abc", "xyz") = "xyzabc"
   * StringUtils.prependIfMissingIgnoreCase("xyzabc", "xyz") = "xyzabc"
   * StringUtils.prependIfMissingIgnoreCase("XYZabc", "xyz") = "XYZabc"
   * </pre>
   * <p>With additional prefixes,</p>
   * <pre>
   * StringUtils.prependIfMissingIgnoreCase(null, null, null) = null
   * StringUtils.prependIfMissingIgnoreCase("abc", null, null) = "abc"
   * StringUtils.prependIfMissingIgnoreCase("", "xyz", null) = "xyz"
   * StringUtils.prependIfMissingIgnoreCase("abc", "xyz", new CharSequence[]{null}) = "xyzabc"
   * StringUtils.prependIfMissingIgnoreCase("abc", "xyz", "") = "abc"
   * StringUtils.prependIfMissingIgnoreCase("abc", "xyz", "mno") = "xyzabc"
   * StringUtils.prependIfMissingIgnoreCase("xyzabc", "xyz", "mno") = "xyzabc"
   * StringUtils.prependIfMissingIgnoreCase("mnoabc", "xyz", "mno") = "mnoabc"
   * StringUtils.prependIfMissingIgnoreCase("XYZabc", "xyz", "mno") = "XYZabc"
   * StringUtils.prependIfMissingIgnoreCase("MNOabc", "xyz", "mno") = "MNOabc"
   * </pre>
   *
   * @param str      The string.
   * @param prefix   The prefix to prepend to the start of the string.
   * @param prefixes Additional prefixes that are valid (optional).
   * @return A new String if prefix was prepended, the same string otherwise.
   */
  public static String prependIfMissingIgnoreCase(String str, CharSequence prefix, CharSequence... prefixes) {
    return prependIfMissing(str, prefix, true, prefixes);
  }

  /**
   * <p>Removes all occurrences of a character from within the source string.</p>
   *
   * <p>A {@code null} source string will return {@code null}.
   * An empty ("") source string will return the empty string.</p>
   *
   * <pre>
   * StringUtils.remove(null, *)       = null
   * StringUtils.remove("", *)         = ""
   * StringUtils.remove("queued", 'u') = "qeed"
   * StringUtils.remove("queued", 'z') = "queued"
   * </pre>
   *
   * @param str    the source String to search, may be null
   * @param remove the char to search for and remove, may be null
   * @return the substring with the char removed if found,
   * {@code null} if null String input
   */
  public static String remove(String str, char remove) {
    if (isEmpty(str) || str.indexOf(remove) == INDEX_NOT_FOUND) {
      return str;
    }
    char[] chars = str.toCharArray();
    int pos = 0;
    for (int i = 0; i < chars.length; i++) {
      if (chars[i] != remove) {
        chars[pos++] = chars[i];
      }
    }
    return new String(chars, 0, pos);
  }

  /**
   * <p>Removes all occurrences of a substring from within the source string.</p>
   *
   * <p>A {@code null} source string will return {@code null}.
   * An empty ("") source string will return the empty string.
   * A {@code null} remove string will return the source string.
   * An empty ("") remove string will return the source string.</p>
   *
   * <pre>
   * StringUtils.remove(null, *)        = null
   * StringUtils.remove("", *)          = ""
   * StringUtils.remove(*, null)        = *
   * StringUtils.remove(*, "")          = *
   * StringUtils.remove("queued", "ue") = "qd"
   * StringUtils.remove("queued", "zz") = "queued"
   * </pre>
   *
   * @param str    the source String to search, may be null
   * @param remove the String to search for and remove, may be null
   * @return the substring with the string removed if found,
   * {@code null} if null String input
   */
  public static String remove(String str, String remove) {
    if (isEmpty(str) || isEmpty(remove)) {
      return str;
    }
    return replace(str, remove, EMPTY, -1);
  }

  /**
   * <p>Removes a substring only if it is at the end of a source string,
   * otherwise returns the source string.</p>
   *
   * <p>A {@code null} source string will return {@code null}.
   * An empty ("") source string will return the empty string.
   * A {@code null} search string will return the source string.</p>
   *
   * <pre>
   * StringUtils.removeEnd(null, *)      = null
   * StringUtils.removeEnd("", *)        = ""
   * StringUtils.removeEnd(*, null)      = *
   * StringUtils.removeEnd("www.domain.com", ".com.")  = "www.domain.com"
   * StringUtils.removeEnd("www.domain.com", ".com")   = "www.domain"
   * StringUtils.removeEnd("www.domain.com", "domain") = "www.domain.com"
   * StringUtils.removeEnd("abc", "")    = "abc"
   * </pre>
   *
   * @param str    the source String to search, may be null
   * @param remove the String to search for and remove, may be null
   * @return the substring with the string removed if found,
   * {@code null} if null String input
   */
  public static String removeEnd(String str, String remove) {
    if (isEmpty(str) || isEmpty(remove)) {
      return str;
    }
    if (str.endsWith(remove)) {
      return str.substring(0, str.length() - remove.length());
    }
    return str;
  }

  /**
   * <p>Case insensitive removal of a substring if it is at the end of a source string,
   * otherwise returns the source string.</p>
   *
   * <p>A {@code null} source string will return {@code null}.
   * An empty ("") source string will return the empty string.
   * A {@code null} search string will return the source string.</p>
   *
   * <pre>
   * StringUtils.removeEndIgnoreCase(null, *)      = null
   * StringUtils.removeEndIgnoreCase("", *)        = ""
   * StringUtils.removeEndIgnoreCase(*, null)      = *
   * StringUtils.removeEndIgnoreCase("www.domain.com", ".com.")  = "www.domain.com"
   * StringUtils.removeEndIgnoreCase("www.domain.com", ".com")   = "www.domain"
   * StringUtils.removeEndIgnoreCase("www.domain.com", "domain") = "www.domain.com"
   * StringUtils.removeEndIgnoreCase("abc", "")    = "abc"
   * StringUtils.removeEndIgnoreCase("www.domain.com", ".COM") = "www.domain")
   * StringUtils.removeEndIgnoreCase("www.domain.COM", ".com") = "www.domain")
   * </pre>
   *
   * @param str    the source String to search, may be null
   * @param remove the String to search for (case insensitive) and remove, may be null
   * @return the substring with the string removed if found,
   * {@code null} if null String input
   */
  public static String removeEndIgnoreCase(String str, String remove) {
    if (isEmpty(str) || isEmpty(remove)) {
      return str;
    }
    if (endsWithIgnoreCase(str, remove)) {
      return str.substring(0, str.length() - remove.length());
    }
    return str;
  }

  /**
   * <p>
   * Case insensitive removal of all occurrences of a substring from within
   * the source string.
   * </p>
   *
   * <p>
   * A {@code null} source string will return {@code null}. An empty ("")
   * source string will return the empty string. A {@code null} remove string
   * will return the source string. An empty ("") remove string will return
   * the source string.
   * </p>
   *
   * <pre>
   * StringUtils.removeIgnoreCase(null, *)        = null
   * StringUtils.removeIgnoreCase("", *)          = ""
   * StringUtils.removeIgnoreCase(*, null)        = *
   * StringUtils.removeIgnoreCase(*, "")          = *
   * StringUtils.removeIgnoreCase("queued", "ue") = "qd"
   * StringUtils.removeIgnoreCase("queued", "zz") = "queued"
   * StringUtils.removeIgnoreCase("quEUed", "UE") = "qd"
   * StringUtils.removeIgnoreCase("queued", "zZ") = "queued"
   * </pre>
   *
   * @param str    the source String to search, may be null
   * @param remove the String to search for (case insensitive) and remove, may be
   *               null
   * @return the substring with the string removed if found, {@code null} if
   * null String input
   */
  public static String removeIgnoreCase(String str, String remove) {
    return replaceIgnoreCase(str, remove, EMPTY, -1);
  }

  /**
   * <p>Removes a substring only if it is at the beginning of a source string,
   * otherwise returns the source string.</p>
   *
   * <p>A {@code null} source string will return {@code null}.
   * An empty ("") source string will return the empty string.
   * A {@code null} search string will return the source string.</p>
   *
   * <pre>
   * StringUtils.removeStart(null, *)      = null
   * StringUtils.removeStart("", *)        = ""
   * StringUtils.removeStart(*, null)      = *
   * StringUtils.removeStart("www.domain.com", "www.")   = "domain.com"
   * StringUtils.removeStart("domain.com", "www.")       = "domain.com"
   * StringUtils.removeStart("www.domain.com", "domain") = "www.domain.com"
   * StringUtils.removeStart("abc", "")    = "abc"
   * </pre>
   *
   * @param str    the source String to search, may be null
   * @param remove the String to search for and remove, may be null
   * @return the substring with the string removed if found,
   * {@code null} if null String input
   */
  public static String removeStart(String str, String remove) {
    if (isEmpty(str) || isEmpty(remove)) {
      return str;
    }
    if (str.startsWith(remove)) {
      return str.substring(remove.length());
    }
    return str;
  }

  /**
   * <p>Case insensitive removal of a substring if it is at the beginning of a source string,
   * otherwise returns the source string.</p>
   *
   * <p>A {@code null} source string will return {@code null}.
   * An empty ("") source string will return the empty string.
   * A {@code null} search string will return the source string.</p>
   *
   * <pre>
   * StringUtils.removeStartIgnoreCase(null, *)      = null
   * StringUtils.removeStartIgnoreCase("", *)        = ""
   * StringUtils.removeStartIgnoreCase(*, null)      = *
   * StringUtils.removeStartIgnoreCase("www.domain.com", "www.")   = "domain.com"
   * StringUtils.removeStartIgnoreCase("www.domain.com", "WWW.")   = "domain.com"
   * StringUtils.removeStartIgnoreCase("domain.com", "www.")       = "domain.com"
   * StringUtils.removeStartIgnoreCase("www.domain.com", "domain") = "www.domain.com"
   * StringUtils.removeStartIgnoreCase("abc", "")    = "abc"
   * </pre>
   *
   * @param str    the source String to search, may be null
   * @param remove the String to search for (case insensitive) and remove, may be null
   * @return the substring with the string removed if found,
   * {@code null} if null String input
   */
  public static String removeStartIgnoreCase(String str, String remove) {
    if (str != null && startsWithIgnoreCase(str, remove)) {
      return str.substring(length(remove));
    }
    return str;
  }

  /**
   * <p>Returns padding using the specified delimiter repeated
   * to a given length.</p>
   *
   * <pre>
   * StringUtils.repeat('e', 0)  = ""
   * StringUtils.repeat('e', 3)  = "eee"
   * StringUtils.repeat('e', -2) = ""
   * </pre>
   *
   * <p>Note: this method does not support padding with
   * <a href="http://www.unicode.org/glossary/#supplementary_character">Unicode Supplementary Characters</a>
   * as they require a pair of {@code char}s to be represented.
   * If you are needing to support full I18N of your applications
   * consider using {@link #repeat(String, int)} instead.
   * </p>
   *
   * @param ch     character to repeat
   * @param repeat number of times to repeat char, negative treated as zero
   * @return String with repeated character
   * @see #repeat(String, int)
   */
  public static String repeat(char ch, int repeat) {
    if (repeat <= 0) {
      return EMPTY;
    }
    char[] buf = new char[repeat];
    Arrays.fill(buf, ch);
    return new String(buf);
  }

  /**
   * <p>Repeat a String {@code repeat} times to form a
   * new String.</p>
   *
   * <pre>
   * StringUtils.repeat(null, 2) = null
   * StringUtils.repeat("", 0)   = ""
   * StringUtils.repeat("", 2)   = ""
   * StringUtils.repeat("a", 3)  = "aaa"
   * StringUtils.repeat("ab", 2) = "abab"
   * StringUtils.repeat("a", -2) = ""
   * </pre>
   *
   * @param str    the String to repeat, may be null
   * @param repeat number of times to repeat str, negative treated as zero
   * @return a new String consisting of the original String repeated,
   * {@code null} if null String input
   */
  public static String repeat(String str, int repeat) {
    // Performance tuned for 2.0 (JDK1.4)
    if (str == null) {
      return null;
    }
    if (repeat <= 0) {
      return EMPTY;
    }
    int inputLength = str.length();
    if (repeat == 1 || inputLength == 0) {
      return str;
    }
    if (inputLength == 1 && repeat <= PAD_LIMIT) {
      return repeat(str.charAt(0), repeat);
    }

    int outputLength = inputLength * repeat;
    switch (inputLength) {
      case 1 -> {
        return repeat(str.charAt(0), repeat);
      }
      case 2 -> {
        char ch0 = str.charAt(0);
        char ch1 = str.charAt(1);
        char[] output2 = new char[outputLength];
        for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
          output2[i] = ch0;
          output2[i + 1] = ch1;
        }
        return new String(output2);
      }
      default -> {
        return str.repeat(repeat);
      }
    }
  }

  /**
   * <p>Repeat a String {@code repeat} times to form a
   * new String, with a String separator injected each time. </p>
   *
   * <pre>
   * StringUtils.repeat(null, null, 2) = null
   * StringUtils.repeat(null, "x", 2)  = null
   * StringUtils.repeat("", null, 0)   = ""
   * StringUtils.repeat("", "", 2)     = ""
   * StringUtils.repeat("", "x", 3)    = "xxx"
   * StringUtils.repeat("?", ", ", 3)  = "?, ?, ?"
   * </pre>
   *
   * @param str       the String to repeat, may be null
   * @param separator the String to inject, may be null
   * @param repeat    number of times to repeat str, negative treated as zero
   * @return a new String consisting of the original String repeated,
   * {@code null} if null String input
   */
  public static String repeat(String str, String separator, int repeat) {
    if (str == null || separator == null) {
      return repeat(str, repeat);
    }
    // given that repeat(String, int) is quite optimized, better to rely on it than try and splice this into it
    String result = repeat(str + separator, repeat);
    return removeEnd(result, separator);
  }

  /**
   * <p>Replaces all occurrences of a String within another String.</p>
   *
   * <p>A {@code null} reference passed to this method is a no-op.</p>
   *
   * <pre>
   * StringUtils.replace(null, *, *)        = null
   * StringUtils.replace("", *, *)          = ""
   * StringUtils.replace("any", null, *)    = "any"
   * StringUtils.replace("any", *, null)    = "any"
   * StringUtils.replace("any", "", *)      = "any"
   * StringUtils.replace("aba", "a", null)  = "aba"
   * StringUtils.replace("aba", "a", "")    = "b"
   * StringUtils.replace("aba", "a", "z")   = "zbz"
   * </pre>
   *
   * @param text         text to search and replace in, may be null
   * @param searchString the String to search for, may be null
   * @param replacement  the String to replace it with, may be null
   * @return the text with any replacements processed,
   * {@code null} if null String input
   * @see #replace(String text, String searchString, String replacement, int max)
   */
  public static String replace(String text, String searchString, String replacement) {
    return replace(text, searchString, replacement, -1);
  }

  /**
   * <p>Replaces a String with another String inside a larger String,
   * for the first {@code max} values of the search String.</p>
   *
   * <p>A {@code null} reference passed to this method is a no-op.</p>
   *
   * <pre>
   * StringUtils.replace(null, *, *, *)         = null
   * StringUtils.replace("", *, *, *)           = ""
   * StringUtils.replace("any", null, *, *)     = "any"
   * StringUtils.replace("any", *, null, *)     = "any"
   * StringUtils.replace("any", "", *, *)       = "any"
   * StringUtils.replace("any", *, *, 0)        = "any"
   * StringUtils.replace("abaa", "a", null, -1) = "abaa"
   * StringUtils.replace("abaa", "a", "", -1)   = "b"
   * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
   * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
   * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
   * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
   * </pre>
   *
   * @param text         text to search and replace in, may be null
   * @param searchString the String to search for, may be null
   * @param replacement  the String to replace it with, may be null
   * @param max          maximum number of values to replace, or {@code -1} if no maximum
   * @return the text with any replacements processed,
   * {@code null} if null String input
   */
  public static String replace(String text, String searchString, String replacement, int max) {
    return replace(text, searchString, replacement, max, false);
  }

  /**
   * <p>Replaces a String with another String inside a larger String,
   * for the first {@code max} values of the search String,
   * case sensitively/insensitively based on {@code ignoreCase} value.</p>
   *
   * <p>A {@code null} reference passed to this method is a no-op.</p>
   *
   * <pre>
   * StringUtils.replace(null, *, *, *, false)         = null
   * StringUtils.replace("", *, *, *, false)           = ""
   * StringUtils.replace("any", null, *, *, false)     = "any"
   * StringUtils.replace("any", *, null, *, false)     = "any"
   * StringUtils.replace("any", "", *, *, false)       = "any"
   * StringUtils.replace("any", *, *, 0, false)        = "any"
   * StringUtils.replace("abaa", "a", null, -1, false) = "abaa"
   * StringUtils.replace("abaa", "a", "", -1, false)   = "b"
   * StringUtils.replace("abaa", "a", "z", 0, false)   = "abaa"
   * StringUtils.replace("abaa", "A", "z", 1, false)   = "abaa"
   * StringUtils.replace("abaa", "A", "z", 1, true)   = "zbaa"
   * StringUtils.replace("abAa", "a", "z", 2, true)   = "zbza"
   * StringUtils.replace("abAa", "a", "z", -1, true)  = "zbzz"
   * </pre>
   *
   * @param text         text to search and replace in, may be null
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace it with, may be null
   * @param max          maximum number of values to replace, or {@code -1} if no maximum
   * @param ignoreCase   if true replace is case insensitive, otherwise case sensitive
   * @return the text with any replacements processed,
   * {@code null} if null String input
   */
  private static String replace(String text, String searchString, String replacement, int max, boolean ignoreCase) {
    if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
      return text;
    }
    if (ignoreCase) {
      searchString = searchString.toLowerCase();
    }
    int start = 0;
    int end = ignoreCase ? indexOfIgnoreCase(text, searchString, start) : indexOf(text, searchString, start);
    if (end == INDEX_NOT_FOUND) {
      return text;
    }
    int replLength = searchString.length();
    int increase = Math.max(replacement.length() - replLength, 0);
    increase *= max < 0 ? 16 : Math.min(max, 64);
    StringBuilder buf = new StringBuilder(text.length() + increase);
    while (end != INDEX_NOT_FOUND) {
      buf.append(text, start, end).append(replacement);
      start = end + replLength;
      if (--max == 0) {
        break;
      }
      end = ignoreCase ? indexOfIgnoreCase(text, searchString, start) : indexOf(text, searchString, start);
    }
    buf.append(text, start, text.length());
    return buf.toString();
  }

  /**
   * <p>Replaces all occurrences of a character in a String with another.
   * This is a null-safe version of {@link String#replace(char, char)}.</p>
   *
   * <p>A {@code null} string input returns {@code null}.
   * An empty ("") string input returns an empty string.</p>
   *
   * <pre>
   * StringUtils.replaceChars(null, *, *)        = null
   * StringUtils.replaceChars("", *, *)          = ""
   * StringUtils.replaceChars("abcba", 'b', 'y') = "aycya"
   * StringUtils.replaceChars("abcba", 'z', 'y') = "abcba"
   * </pre>
   *
   * @param str         String to replace characters in, may be null
   * @param searchChar  the character to search for, may be null
   * @param replaceChar the character to replace, may be null
   * @return modified String, {@code null} if null string input
   */
  public static String replaceChars(String str, char searchChar, char replaceChar) {
    if (str == null) {
      return null;
    }
    return str.replace(searchChar, replaceChar);
  }

  /**
   * <p>Replaces multiple characters in a String in one go.
   * This method can also be used to delete characters.</p>
   *
   * <p>For example:<br>
   * {@code replaceChars(&quot;hello&quot;, &quot;ho&quot;, &quot;jy&quot;) = jelly}.</p>
   *
   * <p>A {@code null} string input returns {@code null}.
   * An empty ("") string input returns an empty string.
   * A null or empty set of search characters returns the input string.</p>
   *
   * <p>The length of the search characters should normally equal the length
   * of the replace characters.
   * If the search characters is longer, then the extra search characters
   * are deleted.
   * If the search characters is shorter, then the extra replace characters
   * are ignored.</p>
   *
   * <pre>
   * StringUtils.replaceChars(null, *, *)           = null
   * StringUtils.replaceChars("", *, *)             = ""
   * StringUtils.replaceChars("abc", null, *)       = "abc"
   * StringUtils.replaceChars("abc", "", *)         = "abc"
   * StringUtils.replaceChars("abc", "b", null)     = "ac"
   * StringUtils.replaceChars("abc", "b", "")       = "ac"
   * StringUtils.replaceChars("abcba", "bc", "yz")  = "ayzya"
   * StringUtils.replaceChars("abcba", "bc", "y")   = "ayya"
   * StringUtils.replaceChars("abcba", "bc", "yzx") = "ayzya"
   * </pre>
   *
   * @param str          String to replace characters in, may be null
   * @param searchChars  a set of characters to search for, may be null
   * @param replaceChars a set of characters to replace, may be null
   * @return modified String, {@code null} if null string input
   */
  public static String replaceChars(String str, String searchChars, String replaceChars) {
    if (isEmpty(str) || isEmpty(searchChars)) {
      return str;
    }
    if (replaceChars == null) {
      replaceChars = EMPTY;
    }
    boolean modified = false;
    int replaceCharsLength = replaceChars.length();
    int strLength = str.length();
    StringBuilder buf = new StringBuilder(strLength);
    for (int i = 0; i < strLength; i++) {
      char ch = str.charAt(i);
      int index = searchChars.indexOf(ch);
      if (index >= 0) {
        modified = true;
        if (index < replaceCharsLength) {
          buf.append(replaceChars.charAt(index));
        }
      } else {
        buf.append(ch);
      }
    }
    if (modified) {
      return buf.toString();
    }
    return str;
  }

  /**
   * <p>Case insensitively replaces all occurrences of a String within another String.</p>
   *
   * <p>A {@code null} reference passed to this method is a no-op.</p>
   *
   * <pre>
   * StringUtils.replaceIgnoreCase(null, *, *)        = null
   * StringUtils.replaceIgnoreCase("", *, *)          = ""
   * StringUtils.replaceIgnoreCase("any", null, *)    = "any"
   * StringUtils.replaceIgnoreCase("any", *, null)    = "any"
   * StringUtils.replaceIgnoreCase("any", "", *)      = "any"
   * StringUtils.replaceIgnoreCase("aba", "a", null)  = "aba"
   * StringUtils.replaceIgnoreCase("abA", "A", "")    = "b"
   * StringUtils.replaceIgnoreCase("aba", "A", "z")   = "zbz"
   * </pre>
   *
   * @param text         text to search and replace in, may be null
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace it with, may be null
   * @return the text with any replacements processed,
   * {@code null} if null String input
   * @see #replaceIgnoreCase(String text, String searchString, String replacement, int max)
   */
  public static String replaceIgnoreCase(String text, String searchString, String replacement) {
    return replaceIgnoreCase(text, searchString, replacement, -1);
  }

  /**
   * <p>Case insensitively replaces a String with another String inside a larger String,
   * for the first {@code max} values of the search String.</p>
   *
   * <p>A {@code null} reference passed to this method is a no-op.</p>
   *
   * <pre>
   * StringUtils.replaceIgnoreCase(null, *, *, *)         = null
   * StringUtils.replaceIgnoreCase("", *, *, *)           = ""
   * StringUtils.replaceIgnoreCase("any", null, *, *)     = "any"
   * StringUtils.replaceIgnoreCase("any", *, null, *)     = "any"
   * StringUtils.replaceIgnoreCase("any", "", *, *)       = "any"
   * StringUtils.replaceIgnoreCase("any", *, *, 0)        = "any"
   * StringUtils.replaceIgnoreCase("abaa", "a", null, -1) = "abaa"
   * StringUtils.replaceIgnoreCase("abaa", "a", "", -1)   = "b"
   * StringUtils.replaceIgnoreCase("abaa", "a", "z", 0)   = "abaa"
   * StringUtils.replaceIgnoreCase("abaa", "A", "z", 1)   = "zbaa"
   * StringUtils.replaceIgnoreCase("abAa", "a", "z", 2)   = "zbza"
   * StringUtils.replaceIgnoreCase("abAa", "a", "z", -1)  = "zbzz"
   * </pre>
   *
   * @param text         text to search and replace in, may be null
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace it with, may be null
   * @param max          maximum number of values to replace, or {@code -1} if no maximum
   * @return the text with any replacements processed,
   * {@code null} if null String input
   */
  public static String replaceIgnoreCase(String text, String searchString, String replacement, int max) {
    return replace(text, searchString, replacement, max, true);
  }

  /**
   * <p>Replaces a String with another String inside a larger String, once.</p>
   *
   * <p>A {@code null} reference passed to this method is a no-op.</p>
   *
   * <pre>
   * StringUtils.replaceOnce(null, *, *)        = null
   * StringUtils.replaceOnce("", *, *)          = ""
   * StringUtils.replaceOnce("any", null, *)    = "any"
   * StringUtils.replaceOnce("any", *, null)    = "any"
   * StringUtils.replaceOnce("any", "", *)      = "any"
   * StringUtils.replaceOnce("aba", "a", null)  = "aba"
   * StringUtils.replaceOnce("aba", "a", "")    = "ba"
   * StringUtils.replaceOnce("aba", "a", "z")   = "zba"
   * </pre>
   *
   * @param text         text to search and replace in, may be null
   * @param searchString the String to search for, may be null
   * @param replacement  the String to replace with, may be null
   * @return the text with any replacements processed,
   * {@code null} if null String input
   * @see #replace(String text, String searchString, String replacement, int max)
   */
  public static String replaceOnce(String text, String searchString, String replacement) {
    return replace(text, searchString, replacement, 1);
  }

  /**
   * <p>Case insensitively replaces a String with another String inside a larger String, once.</p>
   *
   * <p>A {@code null} reference passed to this method is a no-op.</p>
   *
   * <pre>
   * StringUtils.replaceOnceIgnoreCase(null, *, *)        = null
   * StringUtils.replaceOnceIgnoreCase("", *, *)          = ""
   * StringUtils.replaceOnceIgnoreCase("any", null, *)    = "any"
   * StringUtils.replaceOnceIgnoreCase("any", *, null)    = "any"
   * StringUtils.replaceOnceIgnoreCase("any", "", *)      = "any"
   * StringUtils.replaceOnceIgnoreCase("aba", "a", null)  = "aba"
   * StringUtils.replaceOnceIgnoreCase("aba", "a", "")    = "ba"
   * StringUtils.replaceOnceIgnoreCase("aba", "a", "z")   = "zba"
   * StringUtils.replaceOnceIgnoreCase("FoOFoofoo", "foo", "") = "Foofoo"
   * </pre>
   *
   * @param text         text to search and replace in, may be null
   * @param searchString the String to search for (case insensitive), may be null
   * @param replacement  the String to replace with, may be null
   * @return the text with any replacements processed,
   * {@code null} if null String input
   * @see #replaceIgnoreCase(String text, String searchString, String replacement, int max)
   */
  public static String replaceOnceIgnoreCase(String text, String searchString, String replacement) {
    return replaceIgnoreCase(text, searchString, replacement, 1);
  }

  /**
   * <p>Reverses a String as per {@link StringBuilder#reverse()}.</p>
   *
   * <p>A {@code null} String returns {@code null}.</p>
   *
   * <pre>
   * StringUtils.reverse(null)  = null
   * StringUtils.reverse("")    = ""
   * StringUtils.reverse("bat") = "tab"
   * </pre>
   *
   * @param str the String to reverse, may be null
   * @return the reversed String, {@code null} if null String input
   */
  public static String reverse(String str) {
    if (str == null) {
      return null;
    }
    return new StringBuilder(str).reverse().toString();
  }

  /**
   * <p>Gets the rightmost {@code len} characters of a String.</p>
   *
   * <p>If {@code len} characters are not available, or the String
   * is {@code null}, the String will be returned without an
   * an exception. An empty String is returned if len is negative.</p>
   *
   * <pre>
   * StringUtils.right(null, *)    = null
   * StringUtils.right(*, -ve)     = ""
   * StringUtils.right("", *)      = ""
   * StringUtils.right("abc", 0)   = ""
   * StringUtils.right("abc", 2)   = "bc"
   * StringUtils.right("abc", 4)   = "abc"
   * </pre>
   *
   * @param str the String to get the rightmost characters from, may be null
   * @param len the length of the required String
   * @return the rightmost characters, {@code null} if null String input
   */
  public static String right(String str, int len) {
    if (str == null) {
      return null;
    }
    if (len < 0) {
      return EMPTY;
    }
    if (str.length() <= len) {
      return str;
    }
    return str.substring(str.length() - len);
  }

  /**
   * <p>Right pad a String with spaces (' ').</p>
   *
   * <p>The String is padded to the size of {@code size}.</p>
   *
   * <pre>
   * StringUtils.rightPad(null, *)   = null
   * StringUtils.rightPad("", 3)     = "   "
   * StringUtils.rightPad("bat", 3)  = "bat"
   * StringUtils.rightPad("bat", 5)  = "bat  "
   * StringUtils.rightPad("bat", 1)  = "bat"
   * StringUtils.rightPad("bat", -1) = "bat"
   * </pre>
   *
   * @param str  the String to pad out, may be null
   * @param size the size to pad to
   * @return right padded String or original String if no padding is necessary,
   * {@code null} if null String input
   */
  public static String rightPad(String str, int size) {
    return rightPad(str, size, ' ');
  }

  /**
   * <p>Right pad a String with a specified character.</p>
   *
   * <p>The String is padded to the size of {@code size}.</p>
   *
   * <pre>
   * StringUtils.rightPad(null, *, *)     = null
   * StringUtils.rightPad("", 3, 'z')     = "zzz"
   * StringUtils.rightPad("bat", 3, 'z')  = "bat"
   * StringUtils.rightPad("bat", 5, 'z')  = "batzz"
   * StringUtils.rightPad("bat", 1, 'z')  = "bat"
   * StringUtils.rightPad("bat", -1, 'z') = "bat"
   * </pre>
   *
   * @param str     the String to pad out, may be null
   * @param size    the size to pad to
   * @param padChar the character to pad with
   * @return right padded String or original String if no padding is necessary,
   * {@code null} if null String input
   */
  public static String rightPad(String str, int size, char padChar) {
    if (str == null) {
      return null;
    }
    int pads = size - str.length();
    if (pads <= 0) {
      return str; // returns original String when possible
    }
    if (pads > PAD_LIMIT) {
      return rightPad(str, size, String.valueOf(padChar));
    }
    return str.concat(repeat(padChar, pads));
  }

  /**
   * <p>Right pad a String with a specified String.</p>
   *
   * <p>The String is padded to the size of {@code size}.</p>
   *
   * <pre>
   * StringUtils.rightPad(null, *, *)      = null
   * StringUtils.rightPad("", 3, "z")      = "zzz"
   * StringUtils.rightPad("bat", 3, "yz")  = "bat"
   * StringUtils.rightPad("bat", 5, "yz")  = "batyz"
   * StringUtils.rightPad("bat", 8, "yz")  = "batyzyzy"
   * StringUtils.rightPad("bat", 1, "yz")  = "bat"
   * StringUtils.rightPad("bat", -1, "yz") = "bat"
   * StringUtils.rightPad("bat", 5, null)  = "bat  "
   * StringUtils.rightPad("bat", 5, "")    = "bat  "
   * </pre>
   *
   * @param str    the String to pad out, may be null
   * @param size   the size to pad to
   * @param padStr the String to pad with, null or empty treated as single space
   * @return right padded String or original String if no padding is necessary,
   * {@code null} if null String input
   */
  public static String rightPad(String str, int size, String padStr) {
    if (str == null) {
      return null;
    }
    if (isEmpty(padStr)) {
      padStr = SPACE;
    }
    int padLen = padStr.length();
    int strLen = str.length();
    int pads = size - strLen;
    if (pads <= 0) {
      return str; // returns original String when possible
    }
    if (padLen == 1 && pads <= PAD_LIMIT) {
      return rightPad(str, size, padStr.charAt(0));
    }

    if (pads == padLen) {
      return str.concat(padStr);
    } else if (pads < padLen) {
      return str.concat(padStr.substring(0, pads));
    } else {
      char[] padding = new char[pads];
      char[] padChars = padStr.toCharArray();
      for (int i = 0; i < pads; i++) {
        padding[i] = padChars[i % padLen];
      }
      return str.concat(new String(padding));
    }
  }

  /**
   * <p>Rotate (circular shift) a String of {@code shift} characters.</p>
   * <ul>
   *  <li>If {@code shift > 0}, right circular shift (ex : ABCDEF =&gt; FABCDE)</li>
   *  <li>If {@code shift < 0}, left circular shift (ex : ABCDEF =&gt; BCDEFA)</li>
   * </ul>
   *
   * <pre>
   * StringUtils.rotate(null, *)        = null
   * StringUtils.rotate("", *)          = ""
   * StringUtils.rotate("abcdefg", 0)   = "abcdefg"
   * StringUtils.rotate("abcdefg", 2)   = "fgabcde"
   * StringUtils.rotate("abcdefg", -2)  = "cdefgab"
   * StringUtils.rotate("abcdefg", 7)   = "abcdefg"
   * StringUtils.rotate("abcdefg", -7)  = "abcdefg"
   * StringUtils.rotate("abcdefg", 9)   = "fgabcde"
   * StringUtils.rotate("abcdefg", -9)  = "cdefgab"
   * </pre>
   *
   * @param str   the String to rotate, may be null
   * @param shift number of time to shift (positive : right shift, negative : left shift)
   * @return the rotated String,
   * or the original String if {@code shift == 0},
   * or {@code null} if null String input
   */
  public static String rotate(String str, int shift) {
    if (str == null) {
      return null;
    }

    int strLen = str.length();
    if (shift == 0 || strLen == 0 || shift % strLen == 0) {
      return str;
    }

    StringBuilder builder = new StringBuilder(strLen);
    int offset = -(shift % strLen);
    builder.append(substring(str, offset));
    builder.append(substring(str, 0, offset));
    return builder.toString();
  }

  /**
   * <p>Splits the provided text into an array, using whitespace as the
   * separator.
   * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <p>The separator is not included in the returned String array.
   * Adjacent separators are treated as one separator.
   * For more control over the split use the StrTokenizer class.</p>
   *
   * <p>A {@code null} input String returns {@code null}.</p>
   *
   * <pre>
   * StringUtils.split(null)       = null
   * StringUtils.split("")         = []
   * StringUtils.split("abc def")  = ["abc", "def"]
   * StringUtils.split("abc  def") = ["abc", "def"]
   * StringUtils.split(" abc ")    = ["abc"]
   * </pre>
   *
   * @param str the String to parse, may be null
   * @return an array of parsed Strings, {@code null} if null String input
   */
  public static String[] split(String str) {
    return split(str, null, -1);
  }

  /**
   * <p>Splits the provided text into an array, separator specified.
   * This is an alternative to using StringTokenizer.</p>
   *
   * <p>The separator is not included in the returned String array.
   * Adjacent separators are treated as one separator.
   * For more control over the split use the StrTokenizer class.</p>
   *
   * <p>A {@code null} input String returns {@code null}.</p>
   *
   * <pre>
   * StringUtils.split(null, *)         = null
   * StringUtils.split("", *)           = []
   * StringUtils.split("a.b.c", '.')    = ["a", "b", "c"]
   * StringUtils.split("a..b.c", '.')   = ["a", "b", "c"]
   * StringUtils.split("a:b:c", '.')    = ["a:b:c"]
   * StringUtils.split("a b c", ' ')    = ["a", "b", "c"]
   * </pre>
   *
   * @param str           the String to parse, may be null
   * @param separatorChar the character used as the delimiter
   * @return an array of parsed Strings, {@code null} if null String input
   */
  public static String[] split(String str, char separatorChar) {
    return splitWorker(str, separatorChar, false);
  }

  /**
   * <p>Splits the provided text into an array, separators specified.
   * This is an alternative to using StringTokenizer.</p>
   *
   * <p>The separator is not included in the returned String array.
   * Adjacent separators are treated as one separator.
   * For more control over the split use the StrTokenizer class.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * A {@code null} separatorChars splits on whitespace.</p>
   *
   * <pre>
   * StringUtils.split(null, *)         = null
   * StringUtils.split("", *)           = []
   * StringUtils.split("abc def", null) = ["abc", "def"]
   * StringUtils.split("abc def", " ")  = ["abc", "def"]
   * StringUtils.split("abc  def", " ") = ["abc", "def"]
   * StringUtils.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
   * </pre>
   *
   * @param str            the String to parse, may be null
   * @param separatorChars the characters used as the delimiters,
   *                       {@code null} splits on whitespace
   * @return an array of parsed Strings, {@code null} if null String input
   */
  public static String[] split(String str, String separatorChars) {
    return splitWorker(str, separatorChars, -1, false);
  }

  /**
   * <p>Splits the provided text into an array with a maximum length,
   * separators specified.</p>
   *
   * <p>The separator is not included in the returned String array.
   * Adjacent separators are treated as one separator.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * A {@code null} separatorChars splits on whitespace.</p>
   *
   * <p>If more than {@code max} delimited substrings are found, the last
   * returned string includes all characters after the first {@code max - 1}
   * returned strings (including separator characters).</p>
   *
   * <pre>
   * StringUtils.split(null, *, *)            = null
   * StringUtils.split("", *, *)              = []
   * StringUtils.split("ab cd ef", null, 0)   = ["ab", "cd", "ef"]
   * StringUtils.split("ab   cd ef", null, 0) = ["ab", "cd", "ef"]
   * StringUtils.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
   * StringUtils.split("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
   * </pre>
   *
   * @param str            the String to parse, may be null
   * @param separatorChars the characters used as the delimiters,
   *                       {@code null} splits on whitespace
   * @param max            the maximum number of elements to include in the
   *                       array. A zero or negative value implies no limit
   * @return an array of parsed Strings, {@code null} if null String input
   */
  public static String[] split(String str, String separatorChars, int max) {
    return splitWorker(str, separatorChars, max, false);
  }

  /**
   * <p>Splits a String by Character type as returned by
   * {@code java.lang.Character.getType(char)}. Groups of contiguous
   * characters of the same type are returned as complete tokens.
   * <pre>
   * StringUtils.splitByCharacterType(null)         = null
   * StringUtils.splitByCharacterType("")           = []
   * StringUtils.splitByCharacterType("ab de fg")   = ["ab", " ", "de", " ", "fg"]
   * StringUtils.splitByCharacterType("ab   de fg") = ["ab", "   ", "de", " ", "fg"]
   * StringUtils.splitByCharacterType("ab:cd:ef")   = ["ab", ":", "cd", ":", "ef"]
   * StringUtils.splitByCharacterType("number5")    = ["number", "5"]
   * StringUtils.splitByCharacterType("fooBar")     = ["foo", "B", "ar"]
   * StringUtils.splitByCharacterType("foo200Bar")  = ["foo", "200", "B", "ar"]
   * StringUtils.splitByCharacterType("ASFRules")   = ["ASFR", "ules"]
   * </pre>
   *
   * @param str the String to split, may be {@code null}
   * @return an array of parsed Strings, {@code null} if null String input
   */
  public static String[] splitByCharacterType(String str) {
    return splitByCharacterType(str, false);
  }

  /**
   * <p>Splits a String by Character type as returned by
   * {@code java.lang.Character.getType(char)}. Groups of contiguous
   * characters of the same type are returned as complete tokens, with the
   * following exception: if {@code camelCase} is {@code true},
   * the character of type {@code Character.UPPERCASE_LETTER}, if any,
   * immediately preceding a token of type {@code Character.LOWERCASE_LETTER}
   * will belong to the following token rather than to the preceding, if any,
   * {@code Character.UPPERCASE_LETTER} token.
   *
   * @param str       the String to split, may be {@code null}
   * @param camelCase whether to use so-called "camel-case" for letter types
   * @return an array of parsed Strings, {@code null} if null String input
   */
  private static String[] splitByCharacterType(String str, boolean camelCase) {
    if (str == null) {
      return null;
    }
    if (str.isEmpty()) {
      return ArrayUtils.EMPTY_STRING_ARRAY;
    }
    char[] c = str.toCharArray();
    List<String> list = new ArrayList<>();
    int tokenStart = 0;
    int currentType = Character.getType(c[tokenStart]);
    for (int pos = tokenStart + 1; pos < c.length; pos++) {
      int type = Character.getType(c[pos]);
      if (type == currentType) {
        continue;
      }
      if (camelCase && type == Character.LOWERCASE_LETTER && currentType == Character.UPPERCASE_LETTER) {
        int newTokenStart = pos - 1;
        if (newTokenStart != tokenStart) {
          list.add(new String(c, tokenStart, newTokenStart - tokenStart));
          tokenStart = newTokenStart;
        }
      } else {
        list.add(new String(c, tokenStart, pos - tokenStart));
        tokenStart = pos;
      }
      currentType = type;
    }
    list.add(new String(c, tokenStart, c.length - tokenStart));
    return list.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
  }

  /**
   * <p>Splits a String by Character type as returned by
   * {@code java.lang.Character.getType(char)}. Groups of contiguous
   * characters of the same type are returned as complete tokens, with the
   * following exception: the character of type
   * {@code Character.UPPERCASE_LETTER}, if any, immediately
   * preceding a token of type {@code Character.LOWERCASE_LETTER}
   * will belong to the following token rather than to the preceding, if any,
   * {@code Character.UPPERCASE_LETTER} token.
   * <pre>
   * StringUtils.splitByCharacterTypeCamelCase(null)         = null
   * StringUtils.splitByCharacterTypeCamelCase("")           = []
   * StringUtils.splitByCharacterTypeCamelCase("ab de fg")   = ["ab", " ", "de", " ", "fg"]
   * StringUtils.splitByCharacterTypeCamelCase("ab   de fg") = ["ab", "   ", "de", " ", "fg"]
   * StringUtils.splitByCharacterTypeCamelCase("ab:cd:ef")   = ["ab", ":", "cd", ":", "ef"]
   * StringUtils.splitByCharacterTypeCamelCase("number5")    = ["number", "5"]
   * StringUtils.splitByCharacterTypeCamelCase("fooBar")     = ["foo", "Bar"]
   * StringUtils.splitByCharacterTypeCamelCase("foo200Bar")  = ["foo", "200", "Bar"]
   * StringUtils.splitByCharacterTypeCamelCase("ASFRules")   = ["ASF", "Rules"]
   * </pre>
   *
   * @param str the String to split, may be {@code null}
   * @return an array of parsed Strings, {@code null} if null String input
   */
  public static String[] splitByCharacterTypeCamelCase(String str) {
    return splitByCharacterType(str, true);
  }

  /**
   * <p>Splits the provided text into an array, separator string specified.</p>
   *
   * <p>The separator(s) will not be included in the returned String array.
   * Adjacent separators are treated as one separator.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * A {@code null} separator splits on whitespace.</p>
   *
   * <pre>
   * StringUtils.splitByWholeSeparator(null, *)               = null
   * StringUtils.splitByWholeSeparator("", *)                 = []
   * StringUtils.splitByWholeSeparator("ab de fg", null)      = ["ab", "de", "fg"]
   * StringUtils.splitByWholeSeparator("ab   de fg", null)    = ["ab", "de", "fg"]
   * StringUtils.splitByWholeSeparator("ab:cd:ef", ":")       = ["ab", "cd", "ef"]
   * StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-") = ["ab", "cd", "ef"]
   * </pre>
   *
   * @param str       the String to parse, may be null
   * @param separator String containing the String to be used as a delimiter,
   *                  {@code null} splits on whitespace
   * @return an array of parsed Strings, {@code null} if null String was input
   */
  public static String[] splitByWholeSeparator(String str, String separator) {
    return splitByWholeSeparatorWorker(str, separator, -1, false);
  }

  /**
   * <p>Splits the provided text into an array, separator string specified.
   * Returns a maximum of {@code max} substrings.</p>
   *
   * <p>The separator(s) will not be included in the returned String array.
   * Adjacent separators are treated as one separator.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * A {@code null} separator splits on whitespace.</p>
   *
   * <pre>
   * StringUtils.splitByWholeSeparator(null, *, *)               = null
   * StringUtils.splitByWholeSeparator("", *, *)                 = []
   * StringUtils.splitByWholeSeparator("ab de fg", null, 0)      = ["ab", "de", "fg"]
   * StringUtils.splitByWholeSeparator("ab   de fg", null, 0)    = ["ab", "de", "fg"]
   * StringUtils.splitByWholeSeparator("ab:cd:ef", ":", 2)       = ["ab", "cd:ef"]
   * StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
   * StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
   * </pre>
   *
   * @param str       the String to parse, may be null
   * @param separator String containing the String to be used as a delimiter,
   *                  {@code null} splits on whitespace
   * @param max       the maximum number of elements to include in the returned
   *                  array. A zero or negative value implies no limit.
   * @return an array of parsed Strings, {@code null} if null String was input
   */
  public static String[] splitByWholeSeparator(String str, String separator, int max) {
    return splitByWholeSeparatorWorker(str, separator, max, false);
  }

  /**
   * <p>Splits the provided text into an array, separator string specified. </p>
   *
   * <p>The separator is not included in the returned String array.
   * Adjacent separators are treated as separators for empty tokens.
   * For more control over the split use the StrTokenizer class.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * A {@code null} separator splits on whitespace.</p>
   *
   * <pre>
   * StringUtils.splitByWholeSeparatorPreserveAllTokens(null, *)               = null
   * StringUtils.splitByWholeSeparatorPreserveAllTokens("", *)                 = []
   * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab de fg", null)      = ["ab", "de", "fg"]
   * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab   de fg", null)    = ["ab", "", "", "de", "fg"]
   * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab:cd:ef", ":")       = ["ab", "cd", "ef"]
   * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-") = ["ab", "cd", "ef"]
   * </pre>
   *
   * @param str       the String to parse, may be null
   * @param separator String containing the String to be used as a delimiter,
   *                  {@code null} splits on whitespace
   * @return an array of parsed Strings, {@code null} if null String was input
   */
  public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator) {
    return splitByWholeSeparatorWorker(str, separator, -1, true);
  }

  /**
   * <p>Splits the provided text into an array, separator string specified.
   * Returns a maximum of {@code max} substrings.</p>
   *
   * <p>The separator is not included in the returned String array.
   * Adjacent separators are treated as separators for empty tokens.
   * For more control over the split use the StrTokenizer class.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * A {@code null} separator splits on whitespace.</p>
   *
   * <pre>
   * StringUtils.splitByWholeSeparatorPreserveAllTokens(null, *, *)               = null
   * StringUtils.splitByWholeSeparatorPreserveAllTokens("", *, *)                 = []
   * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab de fg", null, 0)      = ["ab", "de", "fg"]
   * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab   de fg", null, 0)    = ["ab", "", "", "de", "fg"]
   * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab:cd:ef", ":", 2)       = ["ab", "cd:ef"]
   * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
   * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
   * </pre>
   *
   * @param str       the String to parse, may be null
   * @param separator String containing the String to be used as a delimiter,
   *                  {@code null} splits on whitespace
   * @param max       the maximum number of elements to include in the returned
   *                  array. A zero or negative value implies no limit.
   * @return an array of parsed Strings, {@code null} if null String was input
   */
  public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator, int max) {
    return splitByWholeSeparatorWorker(str, separator, max, true);
  }

  /**
   * Performs the logic for the {@code splitByWholeSeparatorPreserveAllTokens} methods.
   *
   * @param str               the String to parse, may be {@code null}
   * @param separator         String containing the String to be used as a delimiter,
   *                          {@code null} splits on whitespace
   * @param max               the maximum number of elements to include in the returned
   *                          array. A zero or negative value implies no limit.
   * @param preserveAllTokens if {@code true}, adjacent separators are
   *                          treated as empty token separators; if {@code false}, adjacent
   *                          separators are treated as one separator.
   * @return an array of parsed Strings, {@code null} if null String input
   */
  private static String[] splitByWholeSeparatorWorker(
    String str, String separator, int max, boolean preserveAllTokens) {
    if (str == null) {
      return null;
    }

    int len = str.length();

    if (len == 0) {
      return ArrayUtils.EMPTY_STRING_ARRAY;
    }

    if (separator == null || EMPTY.equals(separator)) {
      // Split on whitespace.
      return splitWorker(str, null, max, preserveAllTokens);
    }

    int separatorLength = separator.length();

    ArrayList<String> substrings = new ArrayList<>();
    int numberOfSubstrings = 0;
    int beg = 0;
    int end = 0;
    while (end < len) {
      end = str.indexOf(separator, beg);

      if (end > -1) {
        if (end > beg) {
          numberOfSubstrings += 1;

          if (numberOfSubstrings == max) {
            end = len;
            substrings.add(str.substring(beg));
          } else {
            // The following is OK, because String.substring( beg, end ) excludes
            // the character at the position 'end'.
            substrings.add(str.substring(beg, end));

            // Set the starting point for the next search.
            // The following is equivalent to beg = end + (separatorLength - 1) + 1,
            // which is the right calculation:
            beg = end + separatorLength;
          }
        } else {
          // We found a consecutive occurrence of the separator, so skip it.
          if (preserveAllTokens) {
            numberOfSubstrings += 1;
            if (numberOfSubstrings == max) {
              end = len;
              substrings.add(str.substring(beg));
            } else {
              substrings.add(EMPTY);
            }
          }
          beg = end + separatorLength;
        }
      } else {
        // String.substring( beg ) goes from 'beg' to the end of the String.
        substrings.add(str.substring(beg));
        end = len;
      }
    }

    return substrings.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
  }

  /**
   * <p>Splits the provided text into an array, using whitespace as the
   * separator, preserving all tokens, including empty tokens created by
   * adjacent separators. This is an alternative to using StringTokenizer.
   * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <p>The separator is not included in the returned String array.
   * Adjacent separators are treated as separators for empty tokens.
   * For more control over the split use the StrTokenizer class.</p>
   *
   * <p>A {@code null} input String returns {@code null}.</p>
   *
   * <pre>
   * StringUtils.splitPreserveAllTokens(null)       = null
   * StringUtils.splitPreserveAllTokens("")         = []
   * StringUtils.splitPreserveAllTokens("abc def")  = ["abc", "def"]
   * StringUtils.splitPreserveAllTokens("abc  def") = ["abc", "", "def"]
   * StringUtils.splitPreserveAllTokens(" abc ")    = ["", "abc", ""]
   * </pre>
   *
   * @param str the String to parse, may be {@code null}
   * @return an array of parsed Strings, {@code null} if null String input
   */
  public static String[] splitPreserveAllTokens(String str) {
    return splitWorker(str, null, -1, true);
  }

  /**
   * <p>Splits the provided text into an array, separator specified,
   * preserving all tokens, including empty tokens created by adjacent
   * separators. This is an alternative to using StringTokenizer.</p>
   *
   * <p>The separator is not included in the returned String array.
   * Adjacent separators are treated as separators for empty tokens.
   * For more control over the split use the StrTokenizer class.</p>
   *
   * <p>A {@code null} input String returns {@code null}.</p>
   *
   * <pre>
   * StringUtils.splitPreserveAllTokens(null, *)         = null
   * StringUtils.splitPreserveAllTokens("", *)           = []
   * StringUtils.splitPreserveAllTokens("a.b.c", '.')    = ["a", "b", "c"]
   * StringUtils.splitPreserveAllTokens("a..b.c", '.')   = ["a", "", "b", "c"]
   * StringUtils.splitPreserveAllTokens("a:b:c", '.')    = ["a:b:c"]
   * StringUtils.splitPreserveAllTokens("a\tb\nc", null) = ["a", "b", "c"]
   * StringUtils.splitPreserveAllTokens("a b c", ' ')    = ["a", "b", "c"]
   * StringUtils.splitPreserveAllTokens("a b c ", ' ')   = ["a", "b", "c", ""]
   * StringUtils.splitPreserveAllTokens("a b c  ", ' ')   = ["a", "b", "c", "", ""]
   * StringUtils.splitPreserveAllTokens(" a b c", ' ')   = ["", a", "b", "c"]
   * StringUtils.splitPreserveAllTokens("  a b c", ' ')  = ["", "", a", "b", "c"]
   * StringUtils.splitPreserveAllTokens(" a b c ", ' ')  = ["", a", "b", "c", ""]
   * </pre>
   *
   * @param str           the String to parse, may be {@code null}
   * @param separatorChar the character used as the delimiter,
   *                      {@code null} splits on whitespace
   * @return an array of parsed Strings, {@code null} if null String input
   */
  public static String[] splitPreserveAllTokens(String str, char separatorChar) {
    return splitWorker(str, separatorChar, true);
  }

  /**
   * <p>Splits the provided text into an array, separators specified,
   * preserving all tokens, including empty tokens created by adjacent
   * separators. This is an alternative to using StringTokenizer.</p>
   *
   * <p>The separator is not included in the returned String array.
   * Adjacent separators are treated as separators for empty tokens.
   * For more control over the split use the StrTokenizer class.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * A {@code null} separatorChars splits on whitespace.</p>
   *
   * <pre>
   * StringUtils.splitPreserveAllTokens(null, *)           = null
   * StringUtils.splitPreserveAllTokens("", *)             = []
   * StringUtils.splitPreserveAllTokens("abc def", null)   = ["abc", "def"]
   * StringUtils.splitPreserveAllTokens("abc def", " ")    = ["abc", "def"]
   * StringUtils.splitPreserveAllTokens("abc  def", " ")   = ["abc", "", def"]
   * StringUtils.splitPreserveAllTokens("ab:cd:ef", ":")   = ["ab", "cd", "ef"]
   * StringUtils.splitPreserveAllTokens("ab:cd:ef:", ":")  = ["ab", "cd", "ef", ""]
   * StringUtils.splitPreserveAllTokens("ab:cd:ef::", ":") = ["ab", "cd", "ef", "", ""]
   * StringUtils.splitPreserveAllTokens("ab::cd:ef", ":")  = ["ab", "", cd", "ef"]
   * StringUtils.splitPreserveAllTokens(":cd:ef", ":")     = ["", cd", "ef"]
   * StringUtils.splitPreserveAllTokens("::cd:ef", ":")    = ["", "", cd", "ef"]
   * StringUtils.splitPreserveAllTokens(":cd:ef:", ":")    = ["", cd", "ef", ""]
   * </pre>
   *
   * @param str            the String to parse, may be {@code null}
   * @param separatorChars the characters used as the delimiters,
   *                       {@code null} splits on whitespace
   * @return an array of parsed Strings, {@code null} if null String input
   */
  public static String[] splitPreserveAllTokens(String str, String separatorChars) {
    return splitWorker(str, separatorChars, -1, true);
  }

  /**
   * <p>Splits the provided text into an array with a maximum length,
   * separators specified, preserving all tokens, including empty tokens
   * created by adjacent separators.</p>
   *
   * <p>The separator is not included in the returned String array.
   * Adjacent separators are treated as separators for empty tokens.
   * Adjacent separators are treated as one separator.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * A {@code null} separatorChars splits on whitespace.</p>
   *
   * <p>If more than {@code max} delimited substrings are found, the last
   * returned string includes all characters after the first {@code max - 1}
   * returned strings (including separator characters).</p>
   *
   * <pre>
   * StringUtils.splitPreserveAllTokens(null, *, *)            = null
   * StringUtils.splitPreserveAllTokens("", *, *)              = []
   * StringUtils.splitPreserveAllTokens("ab de fg", null, 0)   = ["ab", "de", "fg"]
   * StringUtils.splitPreserveAllTokens("ab   de fg", null, 0) = ["ab", "", "", "de", "fg"]
   * StringUtils.splitPreserveAllTokens("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
   * StringUtils.splitPreserveAllTokens("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
   * StringUtils.splitPreserveAllTokens("ab   de fg", null, 2) = ["ab", "  de fg"]
   * StringUtils.splitPreserveAllTokens("ab   de fg", null, 3) = ["ab", "", " de fg"]
   * StringUtils.splitPreserveAllTokens("ab   de fg", null, 4) = ["ab", "", "", "de fg"]
   * </pre>
   *
   * @param str            the String to parse, may be {@code null}
   * @param separatorChars the characters used as the delimiters,
   *                       {@code null} splits on whitespace
   * @param max            the maximum number of elements to include in the
   *                       array. A zero or negative value implies no limit
   * @return an array of parsed Strings, {@code null} if null String input
   */
  public static String[] splitPreserveAllTokens(String str, String separatorChars, int max) {
    return splitWorker(str, separatorChars, max, true);
  }

  /**
   * Performs the logic for the {@code split} and
   * {@code splitPreserveAllTokens} methods that do not return a
   * maximum array length.
   *
   * @param str               the String to parse, may be {@code null}
   * @param separatorChar     the separate character
   * @param preserveAllTokens if {@code true}, adjacent separators are
   *                          treated as empty token separators; if {@code false}, adjacent
   *                          separators are treated as one separator.
   * @return an array of parsed Strings, {@code null} if null String input
   */
  private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
    // Performance tuned for 2.0 (JDK1.4)

    if (str == null) {
      return null;
    }
    int len = str.length();
    if (len == 0) {
      return ArrayUtils.EMPTY_STRING_ARRAY;
    }
    List<String> list = new ArrayList<>();
    int i = 0;
    int start = 0;
    boolean match = false;
    boolean lastMatch = false;
    while (i < len) {
      if (str.charAt(i) == separatorChar) {
        if (match || preserveAllTokens) {
          list.add(str.substring(start, i));
          match = false;
          lastMatch = true;
        }
        start = ++i;
        continue;
      }
      lastMatch = false;
      match = true;
      i++;
    }
    //noinspection ConstantConditions
    if (match || preserveAllTokens && lastMatch) {
      list.add(str.substring(start, i));
    }
    return list.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
  }

  /**
   * Performs the logic for the {@code split} and
   * {@code splitPreserveAllTokens} methods that return a maximum array
   * length.
   *
   * @param str               the String to parse, may be {@code null}
   * @param separatorChars    the separate character
   * @param max               the maximum number of elements to include in the
   *                          array. A zero or negative value implies no limit.
   * @param preserveAllTokens if {@code true}, adjacent separators are
   *                          treated as empty token separators; if {@code false}, adjacent
   *                          separators are treated as one separator.
   * @return an array of parsed Strings, {@code null} if null String input
   */
  private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
    // Performance tuned for 2.0 (JDK1.4)
    // Direct code is quicker than StringTokenizer.
    // Also, StringTokenizer uses isSpace() not isWhitespace()

    if (str == null) {
      return null;
    }
    int len = str.length();
    if (len == 0) {
      return ArrayUtils.EMPTY_STRING_ARRAY;
    }
    List<String> list = new ArrayList<>();
    int sizePlus1 = 1;
    int i = 0;
    int start = 0;
    boolean match = false;
    boolean lastMatch = false;
    if (separatorChars == null) {
      // Null separator means use whitespace
      while (i < len) {
        if (Character.isWhitespace(str.charAt(i))) {
          if (match || preserveAllTokens) {
            lastMatch = true;
            if (sizePlus1++ == max) {
              i = len;
              lastMatch = false;
            }
            list.add(str.substring(start, i));
            match = false;
          }
          start = ++i;
          continue;
        }
        lastMatch = false;
        match = true;
        i++;
      }
    } else if (separatorChars.length() == 1) {
      // Optimise 1 character case
      char sep = separatorChars.charAt(0);
      while (i < len) {
        if (str.charAt(i) == sep) {
          if (match || preserveAllTokens) {
            lastMatch = true;
            if (sizePlus1++ == max) {
              i = len;
              lastMatch = false;
            }
            list.add(str.substring(start, i));
            match = false;
          }
          start = ++i;
          continue;
        }
        lastMatch = false;
        match = true;
        i++;
      }
    } else {
      // standard case
      while (i < len) {
        if (separatorChars.indexOf(str.charAt(i)) >= 0) {
          if (match || preserveAllTokens) {
            lastMatch = true;
            if (sizePlus1++ == max) {
              i = len;
              lastMatch = false;
            }
            list.add(str.substring(start, i));
            match = false;
          }
          start = ++i;
          continue;
        }
        lastMatch = false;
        match = true;
        i++;
      }
    }
    if (match || preserveAllTokens && lastMatch) {
      list.add(str.substring(start, i));
    }
    return list.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
  }

  /**
   * <p>Check if a CharSequence starts with a specified prefix.</p>
   *
   * <p>{@code null}s are handled without exceptions. Two {@code null}
   * references are considered to be equal. The comparison is case sensitive.</p>
   *
   * <pre>
   * StringUtils.startsWith(null, null)      = true
   * StringUtils.startsWith(null, "abc")     = false
   * StringUtils.startsWith("abcdef", null)  = false
   * StringUtils.startsWith("abcdef", "abc") = true
   * StringUtils.startsWith("ABCDEF", "abc") = false
   * </pre>
   *
   * @param str    the CharSequence to check, may be null
   * @param prefix the prefix to find, may be null
   * @return {@code true} if the CharSequence starts with the prefix, case sensitive, or
   * both {@code null}
   * @see String#startsWith(String)
   */
  public static boolean startsWith(CharSequence str, CharSequence prefix) {
    return startsWith(str, prefix, false);
  }

  /**
   * <p>Check if a CharSequence starts with a specified prefix (optionally case insensitive).</p>
   *
   * @param str        the CharSequence to check, may be null
   * @param prefix     the prefix to find, may be null
   * @param ignoreCase indicates whether the compare should ignore case
   *                   (case insensitive) or not.
   * @return {@code true} if the CharSequence starts with the prefix or
   * both {@code null}
   * @see String#startsWith(String)
   */
  private static boolean startsWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
    if (str == null || prefix == null) {
      return str == prefix;
    }
    // Get length once instead of twice in the unlikely case that it changes.
    int preLen = prefix.length();
    if (preLen > str.length()) {
      return false;
    }
    return CharSequenceUtils.regionMatches(str, ignoreCase, 0, prefix, 0, preLen);
  }

  /**
   * <p>Check if a CharSequence starts with any of the provided case-sensitive prefixes.</p>
   *
   * <pre>
   * StringUtils.startsWithAny(null, null)      = false
   * StringUtils.startsWithAny(null, new String[] {"abc"})  = false
   * StringUtils.startsWithAny("abcxyz", null)     = false
   * StringUtils.startsWithAny("abcxyz", new String[] {""}) = true
   * StringUtils.startsWithAny("abcxyz", new String[] {"abc"}) = true
   * StringUtils.startsWithAny("abcxyz", new String[] {null, "xyz", "abc"}) = true
   * StringUtils.startsWithAny("abcxyz", null, "xyz", "ABCX") = false
   * StringUtils.startsWithAny("ABCXYZ", null, "xyz", "abc") = false
   * </pre>
   *
   * @param sequence      the CharSequence to check, may be null
   * @param searchStrings the case-sensitive CharSequence prefixes, may be empty or contain {@code null}
   * @return {@code true} if the input {@code sequence} is {@code null} AND no {@code searchStrings} are provided, or
   * the input {@code sequence} begins with any of the provided case-sensitive {@code searchStrings}.
   * @see StringUtils#startsWith(CharSequence, CharSequence)
   */
  public static boolean startsWithAny(CharSequence sequence, CharSequence... searchStrings) {
    if (isEmpty(sequence) || ArrayUtils.isEmpty(searchStrings)) {
      return false;
    }
    for (CharSequence searchString : searchStrings) {
      if (startsWith(sequence, searchString)) {
        return true;
      }
    }
    return false;
  }

  /**
   * <p>Case insensitive check if a CharSequence starts with a specified prefix.</p>
   *
   * <p>{@code null}s are handled without exceptions. Two {@code null}
   * references are considered to be equal. The comparison is case insensitive.</p>
   *
   * <pre>
   * StringUtils.startsWithIgnoreCase(null, null)      = true
   * StringUtils.startsWithIgnoreCase(null, "abc")     = false
   * StringUtils.startsWithIgnoreCase("abcdef", null)  = false
   * StringUtils.startsWithIgnoreCase("abcdef", "abc") = true
   * StringUtils.startsWithIgnoreCase("ABCDEF", "abc") = true
   * </pre>
   *
   * @param str    the CharSequence to check, may be null
   * @param prefix the prefix to find, may be null
   * @return {@code true} if the CharSequence starts with the prefix, case insensitive, or
   * both {@code null}
   * @see String#startsWith(String)
   */
  public static boolean startsWithIgnoreCase(CharSequence str, CharSequence prefix) {
    return startsWith(str, prefix, true);
  }

  /**
   * <p>Strips whitespace from the start and end of a String.</p>
   *
   * <p>This is similar to {@link #trim(String)} but removes whitespace.
   * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <p>A {@code null} input String returns {@code null}.</p>
   *
   * <pre>
   * StringUtils.strip(null)     = null
   * StringUtils.strip("")       = ""
   * StringUtils.strip("   ")    = ""
   * StringUtils.strip("abc")    = "abc"
   * StringUtils.strip("  abc")  = "abc"
   * StringUtils.strip("abc  ")  = "abc"
   * StringUtils.strip(" abc ")  = "abc"
   * StringUtils.strip(" ab c ") = "ab c"
   * </pre>
   *
   * @param str the String to remove whitespace from, may be null
   * @return the stripped String, {@code null} if null String input
   */
  public static String strip(String str) {
    return strip(str, null);
  }

  /**
   * <p>Strips any of a set of characters from the start and end of a String.
   * This is similar to {@link String#trim()} but allows the characters
   * to be stripped to be controlled.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * An empty string ("") input returns the empty string.</p>
   *
   * <p>If the stripChars String is {@code null}, whitespace is
   * stripped as defined by {@link Character#isWhitespace(char)}.
   * Alternatively use {@link #strip(String)}.</p>
   *
   * <pre>
   * StringUtils.strip(null, *)          = null
   * StringUtils.strip("", *)            = ""
   * StringUtils.strip("abc", null)      = "abc"
   * StringUtils.strip("  abc", null)    = "abc"
   * StringUtils.strip("abc  ", null)    = "abc"
   * StringUtils.strip(" abc ", null)    = "abc"
   * StringUtils.strip("  abcyx", "xyz") = "  abc"
   * </pre>
   *
   * @param str        the String to remove characters from, may be null
   * @param stripChars the characters to remove, null treated as whitespace
   * @return the stripped String, {@code null} if null String input
   */
  public static String strip(String str, String stripChars) {
    str = stripStart(str, stripChars);
    return stripEnd(str, stripChars);
  }

  /**
   * <p>Removes diacritics (~= accents) from a string. The case will not be altered.</p>
   * <p>For instance, '&agrave;' will be replaced by 'a'.</p>
   * <p>Note that ligatures will be left as is.</p>
   *
   * <pre>
   * StringUtils.stripAccents(null)                = null
   * StringUtils.stripAccents("")                  = ""
   * StringUtils.stripAccents("control")           = "control"
   * StringUtils.stripAccents("&eacute;clair")     = "eclair"
   * </pre>
   *
   * @param input String to be stripped
   * @return input text with diacritics removed
   */
  // See also Lucene's ASCIIFoldingFilter (Lucene 2.9) that replaces accented characters by their unaccented equivalent (and uncommitted bug fix: https://issues.apache.org/jira/browse/LUCENE-1343?focusedCommentId=12858907&page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#action_12858907).
  public static String stripAccents(String input) {
    if (input == null) {
      return null;
    }
    StringBuilder decomposed = new StringBuilder(Normalizer.normalize(input, Normalizer.Form.NFD));
    convertRemainingAccentCharacters(decomposed);
    // Note that this doesn't correctly remove ligatures...
    return STRIP_ACCENTS_PATTERN.matcher(decomposed).replaceAll(EMPTY);
  }

  /**
   * <p>Strips whitespace from the start and end of every String in an array.
   * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <p>A new array is returned each time, except for length zero.
   * A {@code null} array will return {@code null}.
   * An empty array will return itself.
   * A {@code null} array entry will be ignored.</p>
   *
   * <pre>
   * StringUtils.stripAll(null)             = null
   * StringUtils.stripAll([])               = []
   * StringUtils.stripAll(["abc", "  abc"]) = ["abc", "abc"]
   * StringUtils.stripAll(["abc  ", null])  = ["abc", null]
   * </pre>
   *
   * @param strs the array to remove whitespace from, may be null
   * @return the stripped Strings, {@code null} if null array input
   */
  public static String[] stripAll(String... strs) {
    return stripAll(strs, null);
  }

  /**
   * <p>Strips any of a set of characters from the start and end of every
   * String in an array.</p>
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <p>A new array is returned each time, except for length zero.
   * A {@code null} array will return {@code null}.
   * An empty array will return itself.
   * A {@code null} array entry will be ignored.
   * A {@code null} stripChars will strip whitespace as defined by
   * {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.stripAll(null, *)                = null
   * StringUtils.stripAll([], *)                  = []
   * StringUtils.stripAll(["abc", "  abc"], null) = ["abc", "abc"]
   * StringUtils.stripAll(["abc  ", null], null)  = ["abc", null]
   * StringUtils.stripAll(["abc  ", null], "yz")  = ["abc  ", null]
   * StringUtils.stripAll(["yabcz", null], "yz")  = ["abc", null]
   * </pre>
   *
   * @param strs       the array to remove characters from, may be null
   * @param stripChars the characters to remove, null treated as whitespace
   * @return the stripped Strings, {@code null} if null array input
   */
  public static String[] stripAll(String[] strs, String stripChars) {
    int strsLen = ArrayUtils.getLength(strs);
    if (strsLen == 0) {
      return strs;
    }
    String[] newArr = new String[strsLen];
    for (int i = 0; i < strsLen; i++) {
      newArr[i] = strip(strs[i], stripChars);
    }
    return newArr;
  }

  /**
   * <p>Strips any of a set of characters from the end of a String.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * An empty string ("") input returns the empty string.</p>
   *
   * <p>If the stripChars String is {@code null}, whitespace is
   * stripped as defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.stripEnd(null, *)          = null
   * StringUtils.stripEnd("", *)            = ""
   * StringUtils.stripEnd("abc", "")        = "abc"
   * StringUtils.stripEnd("abc", null)      = "abc"
   * StringUtils.stripEnd("  abc", null)    = "  abc"
   * StringUtils.stripEnd("abc  ", null)    = "abc"
   * StringUtils.stripEnd(" abc ", null)    = " abc"
   * StringUtils.stripEnd("  abcyx", "xyz") = "  abc"
   * StringUtils.stripEnd("120.00", ".0")   = "12"
   * </pre>
   *
   * @param str        the String to remove characters from, may be null
   * @param stripChars the set of characters to remove, null treated as whitespace
   * @return the stripped String, {@code null} if null String input
   */
  public static String stripEnd(String str, String stripChars) {
    int end = length(str);
    if (end == 0) {
      return str;
    }

    if (stripChars == null) {
      while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
        end--;
      }
    } else if (stripChars.isEmpty()) {
      return str;
    } else {
      while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != INDEX_NOT_FOUND) {
        end--;
      }
    }
    return str.substring(0, end);
  }

  /**
   * <p>Strips any of a set of characters from the start of a String.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * An empty string ("") input returns the empty string.</p>
   *
   * <p>If the stripChars String is {@code null}, whitespace is
   * stripped as defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.stripStart(null, *)          = null
   * StringUtils.stripStart("", *)            = ""
   * StringUtils.stripStart("abc", "")        = "abc"
   * StringUtils.stripStart("abc", null)      = "abc"
   * StringUtils.stripStart("  abc", null)    = "abc"
   * StringUtils.stripStart("abc  ", null)    = "abc  "
   * StringUtils.stripStart(" abc ", null)    = "abc "
   * StringUtils.stripStart("yxabc  ", "xyz") = "abc  "
   * </pre>
   *
   * @param str        the String to remove characters from, may be null
   * @param stripChars the characters to remove, null treated as whitespace
   * @return the stripped String, {@code null} if null String input
   */
  public static String stripStart(String str, String stripChars) {
    int strLen = length(str);
    if (strLen == 0) {
      return str;
    }
    int start = 0;
    if (stripChars == null) {
      while (start != strLen && Character.isWhitespace(str.charAt(start))) {
        start++;
      }
    } else if (stripChars.isEmpty()) {
      return str;
    } else {
      while (start != strLen && stripChars.indexOf(str.charAt(start)) != INDEX_NOT_FOUND) {
        start++;
      }
    }
    return str.substring(start);
  }

  /**
   * <p>Strips whitespace from the start and end of a String  returning
   * an empty String if {@code null} input.</p>
   *
   * <p>This is similar to {@link #trimToEmpty(String)} but removes whitespace.
   * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.stripToEmpty(null)     = ""
   * StringUtils.stripToEmpty("")       = ""
   * StringUtils.stripToEmpty("   ")    = ""
   * StringUtils.stripToEmpty("abc")    = "abc"
   * StringUtils.stripToEmpty("  abc")  = "abc"
   * StringUtils.stripToEmpty("abc  ")  = "abc"
   * StringUtils.stripToEmpty(" abc ")  = "abc"
   * StringUtils.stripToEmpty(" ab c ") = "ab c"
   * </pre>
   *
   * @param str the String to be stripped, may be null
   * @return the trimmed String, or an empty String if {@code null} input
   */
  public static String stripToEmpty(String str) {
    return str == null ? EMPTY : strip(str, null);
  }

  /**
   * <p>Strips whitespace from the start and end of a String  returning
   * {@code null} if the String is empty ("") after the strip.</p>
   *
   * <p>This is similar to {@link #trimToNull(String)} but removes whitespace.
   * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.stripToNull(null)     = null
   * StringUtils.stripToNull("")       = null
   * StringUtils.stripToNull("   ")    = null
   * StringUtils.stripToNull("abc")    = "abc"
   * StringUtils.stripToNull("  abc")  = "abc"
   * StringUtils.stripToNull("abc  ")  = "abc"
   * StringUtils.stripToNull(" abc ")  = "abc"
   * StringUtils.stripToNull(" ab c ") = "ab c"
   * </pre>
   *
   * @param str the String to be stripped, may be null
   * @return the stripped String,
   * {@code null} if whitespace, empty or null String input
   */
  public static String stripToNull(String str) {
    if (str == null) {
      return null;
    }
    str = strip(str, null);
    return str.isEmpty() ? null : str; // NOSONARLINT str cannot be null here
  }

  /**
   * <p>Gets a substring from the specified String avoiding exceptions.</p>
   *
   * <p>A negative start position can be used to start {@code n}
   * characters from the end of the String.</p>
   *
   * <p>A {@code null} String will return {@code null}.
   * An empty ("") String will return "".</p>
   *
   * <pre>
   * StringUtils.substring(null, *)   = null
   * StringUtils.substring("", *)     = ""
   * StringUtils.substring("abc", 0)  = "abc"
   * StringUtils.substring("abc", 2)  = "c"
   * StringUtils.substring("abc", 4)  = ""
   * StringUtils.substring("abc", -2) = "bc"
   * StringUtils.substring("abc", -4) = "abc"
   * </pre>
   *
   * @param str   the String to get the substring from, may be null
   * @param start the position to start from, negative means
   *              count back from the end of the String by this many characters
   * @return substring from start position, {@code null} if null String input
   */
  public static String substring(String str, int start) {
    if (str == null) {
      return null;
    }

    // handle negatives, which means last n characters
    if (start < 0) {
      start = str.length() + start; // remember start is negative
    }

    if (start < 0) {
      start = 0;
    }
    if (start > str.length()) {
      return EMPTY;
    }

    return str.substring(start);
  }

  /**
   * <p>Gets a substring from the specified String avoiding exceptions.</p>
   *
   * <p>A negative start position can be used to start/end {@code n}
   * characters from the end of the String.</p>
   *
   * <p>The returned substring starts with the character in the {@code start}
   * position and ends before the {@code end} position. All position counting is
   * zero-based -- i.e., to start at the beginning of the string use
   * {@code start = 0}. Negative start and end positions can be used to
   * specify offsets relative to the end of the String.</p>
   *
   * <p>If {@code start} is not strictly to the left of {@code end}, ""
   * is returned.</p>
   *
   * <pre>
   * StringUtils.substring(null, *, *)    = null
   * StringUtils.substring("", * ,  *)    = "";
   * StringUtils.substring("abc", 0, 2)   = "ab"
   * StringUtils.substring("abc", 2, 0)   = ""
   * StringUtils.substring("abc", 2, 4)   = "c"
   * StringUtils.substring("abc", 4, 6)   = ""
   * StringUtils.substring("abc", 2, 2)   = ""
   * StringUtils.substring("abc", -2, -1) = "b"
   * StringUtils.substring("abc", -4, 2)  = "ab"
   * </pre>
   *
   * @param str   the String to get the substring from, may be null
   * @param start the position to start from, negative means
   *              count back from the end of the String by this many characters
   * @param end   the position to end at (exclusive), negative means
   *              count back from the end of the String by this many characters
   * @return substring from start position to end position,
   * {@code null} if null String input
   */
  public static String substring(String str, int start, int end) {
    if (str == null) {
      return null;
    }

    // handle negatives
    if (end < 0) {
      end = str.length() + end; // remember end is negative
    }
    if (start < 0) {
      start = str.length() + start; // remember start is negative
    }

    // check length next
    if (end > str.length()) {
      end = str.length();
    }

    // if start is greater than end, return ""
    if (start > end) {
      return EMPTY;
    }

    if (start < 0) {
      start = 0;
    }
    if (end < 0) {
      end = 0;
    }

    return str.substring(start, end);
  }

  /**
   * <p>Gets the substring after the first occurrence of a separator.
   * The separator is not returned.</p>
   *
   * <p>A {@code null} string input will return {@code null}.
   * An empty ("") string input will return the empty string.
   *
   * <p>If nothing is found, the empty string is returned.</p>
   *
   * <pre>
   * StringUtils.substringAfter(null, *)      = null
   * StringUtils.substringAfter("", *)        = ""
   * StringUtils.substringAfter("abc", 'a')   = "bc"
   * StringUtils.substringAfter("abcba", 'b') = "cba"
   * StringUtils.substringAfter("abc", 'c')   = ""
   * StringUtils.substringAfter("abc", 'd')   = ""
   * StringUtils.substringAfter(" abc", 32)   = "abc"
   * </pre>
   *
   * @param str       the String to get a substring from, may be null
   * @param separator the character to search.
   * @return the substring after the first occurrence of the separator,
   * {@code null} if null String input
   */
  public static String substringAfter(String str, int separator) {
    if (isEmpty(str)) {
      return str;
    }
    int pos = str.indexOf(separator);
    if (pos == INDEX_NOT_FOUND) {
      return EMPTY;
    }
    return str.substring(pos + 1);
  }

  /**
   * <p>Gets the substring after the first occurrence of a separator.
   * The separator is not returned.</p>
   *
   * <p>A {@code null} string input will return {@code null}.
   * An empty ("") string input will return the empty string.
   * A {@code null} separator will return the empty string if the
   * input string is not {@code null}.</p>
   *
   * <p>If nothing is found, the empty string is returned.</p>
   *
   * <pre>
   * StringUtils.substringAfter(null, *)      = null
   * StringUtils.substringAfter("", *)        = ""
   * StringUtils.substringAfter(*, null)      = ""
   * StringUtils.substringAfter("abc", "a")   = "bc"
   * StringUtils.substringAfter("abcba", "b") = "cba"
   * StringUtils.substringAfter("abc", "c")   = ""
   * StringUtils.substringAfter("abc", "d")   = ""
   * StringUtils.substringAfter("abc", "")    = "abc"
   * </pre>
   *
   * @param str       the String to get a substring from, may be null
   * @param separator the String to search for, may be null
   * @return the substring after the first occurrence of the separator,
   * {@code null} if null String input
   */
  public static String substringAfter(String str, String separator) {
    if (isEmpty(str)) {
      return str;
    }
    if (separator == null) {
      return EMPTY;
    }
    int pos = str.indexOf(separator);
    if (pos == INDEX_NOT_FOUND) {
      return EMPTY;
    }
    return str.substring(pos + separator.length());
  }

  /**
   * <p>Gets the substring after the last occurrence of a separator.
   * The separator is not returned.</p>
   *
   * <p>A {@code null} string input will return {@code null}.
   * An empty ("") string input will return the empty string.
   *
   * <p>If nothing is found, the empty string is returned.</p>
   *
   * <pre>
   * StringUtils.substringAfterLast(null, *)      = null
   * StringUtils.substringAfterLast("", *)        = ""
   * StringUtils.substringAfterLast("abc", 'a')   = "bc"
   * StringUtils.substringAfterLast(" bc", 32)    = "bc"
   * StringUtils.substringAfterLast("abcba", 'b') = "a"
   * StringUtils.substringAfterLast("abc", 'c')   = ""
   * StringUtils.substringAfterLast("a", 'a')     = ""
   * StringUtils.substringAfterLast("a", 'z')     = ""
   * </pre>
   *
   * @param str       the String to get a substring from, may be null
   * @param separator the String to search for, may be null
   * @return the substring after the last occurrence of the separator,
   * {@code null} if null String input
   */
  public static String substringAfterLast(String str, int separator) {
    if (isEmpty(str)) {
      return str;
    }
    int pos = str.lastIndexOf(separator);
    if (pos == INDEX_NOT_FOUND || pos == str.length() - 1) {
      return EMPTY;
    }
    return str.substring(pos + 1);
  }

  /**
   * <p>Gets the substring after the last occurrence of a separator.
   * The separator is not returned.</p>
   *
   * <p>A {@code null} string input will return {@code null}.
   * An empty ("") string input will return the empty string.
   * An empty or {@code null} separator will return the empty string if
   * the input string is not {@code null}.</p>
   *
   * <p>If nothing is found, the empty string is returned.</p>
   *
   * <pre>
   * StringUtils.substringAfterLast(null, *)      = null
   * StringUtils.substringAfterLast("", *)        = ""
   * StringUtils.substringAfterLast(*, "")        = ""
   * StringUtils.substringAfterLast(*, null)      = ""
   * StringUtils.substringAfterLast("abc", "a")   = "bc"
   * StringUtils.substringAfterLast("abcba", "b") = "a"
   * StringUtils.substringAfterLast("abc", "c")   = ""
   * StringUtils.substringAfterLast("a", "a")     = ""
   * StringUtils.substringAfterLast("a", "z")     = ""
   * </pre>
   *
   * @param str       the String to get a substring from, may be null
   * @param separator the String to search for, may be null
   * @return the substring after the last occurrence of the separator,
   * {@code null} if null String input
   */
  public static String substringAfterLast(String str, String separator) {
    if (isEmpty(str)) {
      return str;
    }
    if (isEmpty(separator)) {
      return EMPTY;
    }
    int pos = str.lastIndexOf(separator);
    if (pos == INDEX_NOT_FOUND || pos == str.length() - separator.length()) {
      return EMPTY;
    }
    return str.substring(pos + separator.length());
  }

  /**
   * <p>
   * Gets the substring before the first occurrence of a separator. The separator is not returned.
   * </p>
   *
   * <p>
   * A {@code null} string input will return {@code null}. An empty ("") string input will return the empty string.
   * </p>
   *
   * <p>
   * If nothing is found, the string input is returned.
   * </p>
   *
   * <pre>
   * StringUtils.substringBefore(null, *)      = null
   * StringUtils.substringBefore("", *)        = ""
   * StringUtils.substringBefore("abc", 'a')   = ""
   * StringUtils.substringBefore("abcba", 'b') = "a"
   * StringUtils.substringBefore("abc", 'c')   = "ab"
   * StringUtils.substringBefore("abc", 'd')   = "abc"
   * </pre>
   *
   * @param str       the String to get a substring from, may be null
   * @param separator the String to search for, may be null
   * @return the substring before the first occurrence of the separator, {@code null} if null String input
   */
  public static String substringBefore(String str, int separator) {
    if (isEmpty(str)) {
      return str;
    }
    int pos = str.indexOf(separator);
    if (pos == INDEX_NOT_FOUND) {
      return str;
    }
    return str.substring(0, pos);
  }

  /**
   * <p>Gets the substring before the first occurrence of a separator.
   * The separator is not returned.</p>
   *
   * <p>A {@code null} string input will return {@code null}.
   * An empty ("") string input will return the empty string.
   * A {@code null} separator will return the input string.</p>
   *
   * <p>If nothing is found, the string input is returned.</p>
   *
   * <pre>
   * StringUtils.substringBefore(null, *)      = null
   * StringUtils.substringBefore("", *)        = ""
   * StringUtils.substringBefore("abc", "a")   = ""
   * StringUtils.substringBefore("abcba", "b") = "a"
   * StringUtils.substringBefore("abc", "c")   = "ab"
   * StringUtils.substringBefore("abc", "d")   = "abc"
   * StringUtils.substringBefore("abc", "")    = ""
   * StringUtils.substringBefore("abc", null)  = "abc"
   * </pre>
   *
   * @param str       the String to get a substring from, may be null
   * @param separator the String to search for, may be null
   * @return the substring before the first occurrence of the separator,
   * {@code null} if null String input
   */
  public static String substringBefore(String str, String separator) {
    if (isEmpty(str) || separator == null) {
      return str;
    }
    if (separator.isEmpty()) {
      return EMPTY;
    }
    int pos = str.indexOf(separator);
    if (pos == INDEX_NOT_FOUND) {
      return str;
    }
    return str.substring(0, pos);
  }

  /**
   * <p>Gets the substring before the last occurrence of a separator.
   * The separator is not returned.</p>
   *
   * <p>A {@code null} string input will return {@code null}.
   * An empty ("") string input will return the empty string.
   * An empty or {@code null} separator will return the input string.</p>
   *
   * <p>If nothing is found, the string input is returned.</p>
   *
   * <pre>
   * StringUtils.substringBeforeLast(null, *)      = null
   * StringUtils.substringBeforeLast("", *)        = ""
   * StringUtils.substringBeforeLast("abcba", "b") = "abc"
   * StringUtils.substringBeforeLast("abc", "c")   = "ab"
   * StringUtils.substringBeforeLast("a", "a")     = ""
   * StringUtils.substringBeforeLast("a", "z")     = "a"
   * StringUtils.substringBeforeLast("a", null)    = "a"
   * StringUtils.substringBeforeLast("a", "")      = "a"
   * </pre>
   *
   * @param str       the String to get a substring from, may be null
   * @param separator the String to search for, may be null
   * @return the substring before the last occurrence of the separator,
   * {@code null} if null String input
   */
  public static String substringBeforeLast(String str, String separator) {
    if (isEmpty(str) || isEmpty(separator)) {
      return str;
    }
    int pos = str.lastIndexOf(separator);
    if (pos == INDEX_NOT_FOUND) {
      return str;
    }
    return str.substring(0, pos);
  }

  /**
   * <p>Searches a String for substrings delimited by a start and end tag,
   * returning all matching substrings in an array.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * A {@code null} open/close returns {@code null} (no match).
   * An empty ("") open/close returns {@code null} (no match).</p>
   *
   * <pre>
   * StringUtils.substringsBetween("[a][b][c]", "[", "]") = ["a","b","c"]
   * StringUtils.substringsBetween(null, *, *)            = null
   * StringUtils.substringsBetween(*, null, *)            = null
   * StringUtils.substringsBetween(*, *, null)            = null
   * StringUtils.substringsBetween("", "[", "]")          = []
   * </pre>
   *
   * @param str   the String containing the substrings, null returns null, empty returns empty
   * @param open  the String identifying the start of the substring, empty returns null
   * @param close the String identifying the end of the substring, empty returns null
   * @return a String Array of substrings, or {@code null} if no match
   */
  public static String[] substringsBetween(String str, String open, String close) {
    if (str == null || isEmpty(open) || isEmpty(close)) {
      return null;
    }
    int strLen = str.length();
    if (strLen == 0) {
      return ArrayUtils.EMPTY_STRING_ARRAY;
    }
    int closeLen = close.length();
    int openLen = open.length();
    List<String> list = new ArrayList<>();
    int pos = 0;
    while (pos < strLen - closeLen) {
      int start = str.indexOf(open, pos);
      if (start < 0) {
        break;
      }
      start += openLen;
      int end = str.indexOf(close, start);
      if (end < 0) {
        break;
      }
      list.add(str.substring(start, end));
      pos = end + closeLen;
    }
    if (list.isEmpty()) {
      return null;
    }
    return list.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
  }

  /**
   * Converts the given source String as a lower-case using the {@link Locale#ROOT} locale in a null-safe manner.
   *
   * @param source A source String or null.
   * @return the given source String as a lower-case using the {@link Locale#ROOT} locale or null.
   */
  public static String toRootLowerCase(String source) {
    return source == null ? null : source.toLowerCase(Locale.ROOT);
  }

  /**
   * Converts the given source String as a upper-case using the {@link Locale#ROOT} locale in a null-safe manner.
   *
   * @param source A source String or null.
   * @return the given source String as a upper-case using the {@link Locale#ROOT} locale or null.
   */
  public static String toRootUpperCase(String source) {
    return source == null ? null : source.toUpperCase(Locale.ROOT);
  }

  private static String toStringOrEmpty(Object obj) {
    return Objects.toString(obj, EMPTY);
  }

  /**
   * <p>Removes control characters (char &lt;= 32) from both
   * ends of this String, handling {@code null} by returning
   * {@code null}.</p>
   *
   * <p>The String is trimmed using {@link String#trim()}.
   * Trim removes start and end characters &lt;= 32.
   * To strip whitespace use {@link #strip(String)}.</p>
   *
   * <p>To trim your choice of characters, use the
   * {@link #strip(String, String)} methods.</p>
   *
   * <pre>
   * StringUtils.trim(null)          = null
   * StringUtils.trim("")            = ""
   * StringUtils.trim("     ")       = ""
   * StringUtils.trim("abc")         = "abc"
   * StringUtils.trim("    abc    ") = "abc"
   * </pre>
   *
   * @param str the String to be trimmed, may be null
   * @return the trimmed string, {@code null} if null String input
   */
  public static String trim(String str) {
    return str == null ? null : str.trim();
  }

  /**
   * <p>Removes control characters (char &lt;= 32) from both
   * ends of this String returning an empty String ("") if the String
   * is empty ("") after the trim or if it is {@code null}.
   *
   * <p>The String is trimmed using {@link String#trim()}.
   * Trim removes start and end characters &lt;= 32.
   * To strip whitespace use {@link #stripToEmpty(String)}.</p>
   *
   * <pre>
   * StringUtils.trimToEmpty(null)          = ""
   * StringUtils.trimToEmpty("")            = ""
   * StringUtils.trimToEmpty("     ")       = ""
   * StringUtils.trimToEmpty("abc")         = "abc"
   * StringUtils.trimToEmpty("    abc    ") = "abc"
   * </pre>
   *
   * @param str the String to be trimmed, may be null
   * @return the trimmed String, or an empty String if {@code null} input
   */
  public static String trimToEmpty(String str) {
    return str == null ? EMPTY : str.trim();
  }

  /**
   * <p>Removes control characters (char &lt;= 32) from both
   * ends of this String returning {@code null} if the String is
   * empty ("") after the trim or if it is {@code null}.
   *
   * <p>The String is trimmed using {@link String#trim()}.
   * Trim removes start and end characters &lt;= 32.
   * To strip whitespace use {@link #stripToNull(String)}.</p>
   *
   * <pre>
   * StringUtils.trimToNull(null)          = null
   * StringUtils.trimToNull("")            = null
   * StringUtils.trimToNull("     ")       = null
   * StringUtils.trimToNull("abc")         = "abc"
   * StringUtils.trimToNull("    abc    ") = "abc"
   * </pre>
   *
   * @param str the String to be trimmed, may be null
   * @return the trimmed String,
   * {@code null} if only chars &lt;= 32, empty or null String input
   */
  public static String trimToNull(String str) {
    String ts = trim(str);
    return isEmpty(ts) ? null : ts;
  }

  /**
   * <p>Truncates a String. This will turn
   * "Now is the time for all good men" into "Now is the time for".</p>
   *
   * <p>Specifically:</p>
   * <ul>
   *   <li>If {@code str} is less than {@code maxWidth} characters
   *       long, return it.</li>
   *   <li>Else truncate it to {@code substring(str, 0, maxWidth)}.</li>
   *   <li>If {@code maxWidth} is less than {@code 0}, throw an
   *       {@code IllegalArgumentException}.</li>
   *   <li>In no case will it return a String of length greater than
   *       {@code maxWidth}.</li>
   * </ul>
   *
   * <pre>
   * StringUtils.truncate(null, 0)       = null
   * StringUtils.truncate(null, 2)       = null
   * StringUtils.truncate("", 4)         = ""
   * StringUtils.truncate("abcdefg", 4)  = "abcd"
   * StringUtils.truncate("abcdefg", 6)  = "abcdef"
   * StringUtils.truncate("abcdefg", 7)  = "abcdefg"
   * StringUtils.truncate("abcdefg", 8)  = "abcdefg"
   * StringUtils.truncate("abcdefg", -1) = throws an IllegalArgumentException
   * </pre>
   *
   * @param str      the String to truncate, may be null
   * @param maxWidth maximum length of result String, must be positive
   * @return truncated String, {@code null} if null String input
   * @throws IllegalArgumentException If {@code maxWidth} is less than {@code 0}
   */
  public static String truncate(String str, int maxWidth) {
    return truncate(str, 0, maxWidth);
  }

  /**
   * <p>Truncates a String. This will turn
   * "Now is the time for all good men" into "is the time for all".</p>
   *
   * <p>Works like {@code truncate(String, int)}, but allows you to specify
   * a "left edge" offset.
   *
   * <p>Specifically:</p>
   * <ul>
   *   <li>If {@code str} is less than {@code maxWidth} characters
   *       long, return it.</li>
   *   <li>Else truncate it to {@code substring(str, offset, maxWidth)}.</li>
   *   <li>If {@code maxWidth} is less than {@code 0}, throw an
   *       {@code IllegalArgumentException}.</li>
   *   <li>If {@code offset} is less than {@code 0}, throw an
   *       {@code IllegalArgumentException}.</li>
   *   <li>In no case will it return a String of length greater than
   *       {@code maxWidth}.</li>
   * </ul>
   *
   * <pre>
   * StringUtils.truncate(null, 0, 0) = null
   * StringUtils.truncate(null, 2, 4) = null
   * StringUtils.truncate("", 0, 10) = ""
   * StringUtils.truncate("", 2, 10) = ""
   * StringUtils.truncate("abcdefghij", 0, 3) = "abc"
   * StringUtils.truncate("abcdefghij", 5, 6) = "fghij"
   * StringUtils.truncate("raspberry peach", 10, 15) = "peach"
   * StringUtils.truncate("abcdefghijklmno", 0, 10) = "abcdefghij"
   * StringUtils.truncate("abcdefghijklmno", -1, 10) = throws an IllegalArgumentException
   * StringUtils.truncate("abcdefghijklmno", Integer.MIN_VALUE, 10) = throws an IllegalArgumentException
   * StringUtils.truncate("abcdefghijklmno", Integer.MIN_VALUE, Integer.MAX_VALUE) = throws an IllegalArgumentException
   * StringUtils.truncate("abcdefghijklmno", 0, Integer.MAX_VALUE) = "abcdefghijklmno"
   * StringUtils.truncate("abcdefghijklmno", 1, 10) = "bcdefghijk"
   * StringUtils.truncate("abcdefghijklmno", 2, 10) = "cdefghijkl"
   * StringUtils.truncate("abcdefghijklmno", 3, 10) = "defghijklm"
   * StringUtils.truncate("abcdefghijklmno", 4, 10) = "efghijklmn"
   * StringUtils.truncate("abcdefghijklmno", 5, 10) = "fghijklmno"
   * StringUtils.truncate("abcdefghijklmno", 5, 5) = "fghij"
   * StringUtils.truncate("abcdefghijklmno", 5, 3) = "fgh"
   * StringUtils.truncate("abcdefghijklmno", 10, 3) = "klm"
   * StringUtils.truncate("abcdefghijklmno", 10, Integer.MAX_VALUE) = "klmno"
   * StringUtils.truncate("abcdefghijklmno", 13, 1) = "n"
   * StringUtils.truncate("abcdefghijklmno", 13, Integer.MAX_VALUE) = "no"
   * StringUtils.truncate("abcdefghijklmno", 14, 1) = "o"
   * StringUtils.truncate("abcdefghijklmno", 14, Integer.MAX_VALUE) = "o"
   * StringUtils.truncate("abcdefghijklmno", 15, 1) = ""
   * StringUtils.truncate("abcdefghijklmno", 15, Integer.MAX_VALUE) = ""
   * StringUtils.truncate("abcdefghijklmno", Integer.MAX_VALUE, Integer.MAX_VALUE) = ""
   * StringUtils.truncate("abcdefghij", 3, -1) = throws an IllegalArgumentException
   * StringUtils.truncate("abcdefghij", -2, 4) = throws an IllegalArgumentException
   * </pre>
   *
   * @param str      the String to truncate, may be null
   * @param offset   left edge of source String
   * @param maxWidth maximum length of result String, must be positive
   * @return truncated String, {@code null} if null String input
   * @throws IllegalArgumentException If {@code offset} or {@code maxWidth} is less than {@code 0}
   */
  public static String truncate(String str, int offset, int maxWidth) {
    if (offset < 0) {
      throw new IllegalArgumentException("offset cannot be negative");
    }
    if (maxWidth < 0) {
      throw new IllegalArgumentException("maxWith cannot be negative");
    }
    if (str == null) {
      return null;
    }
    if (offset > str.length()) {
      return EMPTY;
    }
    if (str.length() > maxWidth) {
      int ix = Math.min(offset + maxWidth, str.length());
      return str.substring(offset, ix);
    }
    return str.substring(offset);
  }

  /**
   * <p>Converts a String to upper case as per {@link String#toUpperCase()}.</p>
   *
   * <p>A {@code null} input String returns {@code null}.</p>
   *
   * <pre>
   * StringUtils.upperCase(null)  = null
   * StringUtils.upperCase("")    = ""
   * StringUtils.upperCase("aBc") = "ABC"
   * </pre>
   *
   * <p><strong>Note:</strong> As described in the documentation for {@link String#toUpperCase()},
   * the result of this method is affected by the current locale.
   * For platform-independent case transformations, the method {@link #lowerCase(String, Locale)}
   * should be used with a specific locale (e.g. {@link Locale#ENGLISH}).</p>
   *
   * @param str the String to upper case, may be null
   * @return the upper cased String, {@code null} if null String input
   */
  public static String upperCase(String str) {
    if (str == null) {
      return null;
    }
    return str.toUpperCase();
  }

  /**
   * <p>Converts a String to upper case as per {@link String#toUpperCase(Locale)}.</p>
   *
   * <p>A {@code null} input String returns {@code null}.</p>
   *
   * <pre>
   * StringUtils.upperCase(null, Locale.ENGLISH)  = null
   * StringUtils.upperCase("", Locale.ENGLISH)    = ""
   * StringUtils.upperCase("aBc", Locale.ENGLISH) = "ABC"
   * </pre>
   *
   * @param str    the String to upper case, may be null
   * @param locale the locale that defines the case transformation rules, must not be null
   * @return the upper cased String, {@code null} if null String input
   */
  public static String upperCase(String str, Locale locale) {
    if (str == null) {
      return null;
    }
    return str.toUpperCase(locale);
  }

  /**
   * Returns the string representation of the {@code char} array or null.
   *
   * @param value the character array.
   * @return a String or null
   * @see String#valueOf(char[])
   */
  public static String valueOf(char[] value) {
    return value == null ? null : String.valueOf(value);
  }
}
