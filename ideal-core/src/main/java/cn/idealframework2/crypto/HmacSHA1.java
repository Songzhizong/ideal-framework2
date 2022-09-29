package cn.idealframework2.crypto;

import javax.annotation.Nonnull;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author 宋志宗 on 2019/9/8
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class HmacSHA1 {

  /**
   * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
   *
   * @param encryptText 被签名的字符串
   * @param encryptKey  密钥
   */
  public static byte[] encode(@Nonnull String encryptText, @Nonnull String encryptKey) {
    try {
      SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
      Mac mac = Mac.getInstance("HmacSHA1");
      mac.init(secretKey);
      return mac.doFinal(encryptText.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
