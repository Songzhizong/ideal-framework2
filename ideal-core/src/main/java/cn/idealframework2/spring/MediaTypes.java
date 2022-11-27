package cn.idealframework2.spring;

import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

/**
 * @author 宋志宗 on 2022/11/27
 */
public class MediaTypes {
  public static final MediaType APPLICATION_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);

  private MediaTypes() {
  }
}
