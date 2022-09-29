package cn.idealframework2.crypto;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 encode
 *
 * @author 宋志宗 on 2018-12-30 02:00
 */
@SuppressWarnings("unused")
public class MD5 {
  private static final char[] HEX_CHARS
      = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  private static final Charset UTF8_CHARSET = StandardCharsets.UTF_8;

  private MD5() {
  }

  public static byte[] md5Digest(byte[] bytes) {
    return digest(bytes);
  }

  public static byte[] md5Digest(@Nonnull InputStream inputStream) {
    return digest(inputStream);
  }

  @Nonnull
  public static String encode(@Nonnull String string) {
    return encode(string.getBytes(UTF8_CHARSET));
  }

  @Nonnull
  public static String encode(byte[] bytes) {
    return digestAsHexString(bytes);
  }

  @Nonnull
  public static String encode(@Nonnull InputStream inputStream) {
    return digestAsHexString(inputStream);
  }

  @Nonnull
  private static MessageDigest getDigest() {
    String md5 = "MD5";
    try {
      return MessageDigest.getInstance(md5);
    } catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + md5 + "\"", ex);
    }
  }

  private static byte[] digest(byte[] bytes) {
    return getDigest().digest(bytes);
  }

  private static byte[] digest(@Nonnull InputStream inputStream) {
    try {
      MessageDigest messageDigest = getDigest();
      byte[] buffer = new byte[4096];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        messageDigest.update(buffer, 0, bytesRead);
      }
      return messageDigest.digest();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Nonnull
  private static String digestAsHexString(byte[] bytes) {
    char[] hexDigest = digestAsHexChars(bytes);
    return String.valueOf(hexDigest);
  }

  @Nonnull
  private static String digestAsHexString(@Nonnull InputStream inputStream) {
    char[] hexDigest = digestAsHexChars(inputStream);
    return new String(hexDigest);
  }

  private static char[] digestAsHexChars(byte[] bytes) {
    byte[] digest = digest(bytes);
    return encodeHex(digest);
  }

  private static char[] digestAsHexChars(@Nonnull InputStream inputStream) {
    byte[] digest = digest(inputStream);
    return encodeHex(digest);
  }

  private static char[] encodeHex(byte[] bytes) {
    char[] chars = new char[32];
    for (int i = 0; i < chars.length; i += 2) {
      byte b = bytes[i / 2];
      chars[i] = HEX_CHARS[b >>> 4 & 0xf];
      chars[i + 1] = HEX_CHARS[b & 0xf];
    }
    return chars;
  }
}
