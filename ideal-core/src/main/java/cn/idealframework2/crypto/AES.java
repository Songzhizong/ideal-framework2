package cn.idealframework2.crypto;

import cn.idealframework2.lang.StringUtils;

import javax.annotation.Nonnull;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author 宋志宗 on 2019-05-14
 */
@SuppressWarnings({"unused", "AlibabaClassNamingShouldBeCamel"})
public class AES {
  private static final String SALT = "6MhsmWY_wvWzqdVRfWdnorziNsDzkVXK";

  /**
   * AES加密
   *
   * @param input  明文
   * @param secret 密钥 长度16|32
   * @return 密文
   */
  public static String encrypt(@Nonnull String input, @Nonnull String secret) {
    byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
    byte[] encrypt = encrypt(bytes, secret);
    return Base64.getEncoder().encodeToString(encrypt);
  }

  public static byte[] encrypt(byte[] input, @Nonnull String secret) {
    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "AES");
      cipher.init(Cipher.ENCRYPT_MODE, keySpec);
      return cipher.doFinal(input);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException
      | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * aes解密
   *
   * @param ciphertext 密文
   * @param secret     密钥 长度16|32
   * @return 明文
   */
  @Nonnull
  public static String decrypt(@Nonnull String ciphertext, @Nonnull String secret) {
    byte[] decode = Base64.getDecoder().decode(ciphertext);
    byte[] decrypt = decrypt(decode, secret);
    return new String(decrypt, StandardCharsets.UTF_8);
  }

  public static byte[] decrypt(byte[] cipherBytes, @Nonnull String secret) {
    try {
      Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "AES");
      aesCipher.init(Cipher.DECRYPT_MODE, keySpec);
      return aesCipher.doFinal(cipherBytes);
    } catch (NoSuchAlgorithmException | BadPaddingException
             | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException e) {
      throw new RuntimeException(e);
    }
  }

  @Nonnull
  public static String generateSecret(@Nonnull String text) {
    Base64.Encoder base64Encoder = Base64.getEncoder();
    String en1 = SHA256.encode(text + SALT);
    String en2 = base64Encoder.encodeToString(HmacSHA1.encode(SALT, text));
    String en3 = MD5.encode(en1 + SALT + en2 + text);
    String en3Base64 = base64Encoder.encodeToString(en3.getBytes(StandardCharsets.US_ASCII));
    String en1Base64 = base64Encoder.encodeToString(en1.getBytes(StandardCharsets.US_ASCII));
    String s = en3Base64 + en1Base64 + en2;
    String replace = StringUtils.replace(s, "=", "");
    String encode = base64Encoder.encodeToString(replace.getBytes(StandardCharsets.UTF_8));
    return encode.substring(0, 32);
  }
}
