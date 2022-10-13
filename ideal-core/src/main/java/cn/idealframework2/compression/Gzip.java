package cn.idealframework2.compression;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP 压缩算法
 *
 * @author 宋志宗 on 2019/9/30
 */
@SuppressWarnings("SpellCheckingInspection")
public class Gzip {
  private static final int BUFFER_SIZE = 256;

  @Nonnull
  public static byte[] compress(@Nonnull byte[] bytes) {
    if (bytes.length == 0) {
      return bytes;
    }
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
         GZIPOutputStream gos = new GZIPOutputStream(baos)) {
      gos.write(bytes);
      gos.finish();
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Nonnull
  public static byte[] uncompress(@Nonnull byte[] bytes) {
    if (bytes.length == 0) {
      return bytes;
    }
    try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
         GZIPInputStream gis = new GZIPInputStream(bais);
         ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[BUFFER_SIZE];
      int n;
      while ((n = gis.read(buffer)) >= 0) {
        baos.write(buffer, 0, n);
      }
      baos.flush();
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Nonnull
  public static byte[] uncompress(@Nonnull byte[] bytes, int offset, int length) {
    if (bytes.length <= offset + length) {
      return new byte[0];
    }
    try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes, offset, length);
         GZIPInputStream gis = new GZIPInputStream(bais);
         ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[BUFFER_SIZE];
      int n;
      while ((n = gis.read(buffer)) >= 0) {
        baos.write(buffer, 0, n);
      }
      baos.flush();
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
