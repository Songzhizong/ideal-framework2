package cn.idealframework2.aot;

import com.github.luben.zstd.*;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ImportRuntimeHints;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author 宋志宗 on 2022/9/30
 */
@ConditionalOnClass(Zstd.class)
@ImportRuntimeHints(ZstdJniRuntimeHintsConfigure.ZstdJniRuntimeHints.class)
public class ZstdJniRuntimeHintsConfigure {

  public static class ZstdJniRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(@Nonnull RuntimeHints hints, @Nullable ClassLoader classLoader) {
      try {
        hints.jni()
          .registerTypes(Arrays.asList(
              TypeReference.of(Zstd.class),
              TypeReference.of(ZstdCompressCtx.class),
              TypeReference.of(ZstdDecompressCtx.class),
              TypeReference.of(ZstdDictCompress.class),
              TypeReference.of(ZstdDictDecompress.class),
              TypeReference.of(ZstdDirectBufferCompressingStream.class),
              TypeReference.of(ZstdDirectBufferCompressingStreamNoFinalizer.class),
              TypeReference.of(ZstdDirectBufferDecompressingStream.class),
              TypeReference.of(ZstdDirectBufferDecompressingStreamNoFinalizer.class),
              TypeReference.of(ZstdInputStreamNoFinalizer.class),
              TypeReference.of(ZstdOutputStream.class),
              TypeReference.of(ZstdOutputStreamNoFinalizer.class)
            ),
            builder -> builder.withMembers(MemberCategory.values())
          )
          .registerField(ZstdCompressCtx.class.getDeclaredField("nativePtr"))
          .registerField(ZstdDecompressCtx.class.getDeclaredField("nativePtr"))
          .registerField(ZstdDictCompress.class.getDeclaredField("nativePtr"))
          .registerField(ZstdDictDecompress.class.getDeclaredField("nativePtr"))
          .registerField(ZstdInputStreamNoFinalizer.class.getDeclaredField("dstPos"))
          .registerField(ZstdInputStreamNoFinalizer.class.getDeclaredField("srcPos"))
          .registerField(ZstdOutputStreamNoFinalizer.class.getDeclaredField("dstPos"))
          .registerField(ZstdOutputStreamNoFinalizer.class.getDeclaredField("srcPos"))
        ;
      } catch (NoSuchFieldException e) {
        throw new RuntimeException(e);
      }
      hints.resources()
        .registerPattern("darwin/aarch64/libzstd-jni-*.dylib")
        .registerPattern("darwin/x86_64/libzstd-jni-*.dylib")
//        .registerPattern("freebsd/amd64/libzstd-jni-*.so")
//        .registerPattern("freebsd/i386/libzstd-jni-*.so")
        .registerPattern("linux/aarch64/libzstd-jni-*.so")
        .registerPattern("linux/amd64/libzstd-jni-*.so")
//        .registerPattern("linux/arm/libzstd-jni-*.so")
//        .registerPattern("linux/i386/libzstd-jni-*.so")
//        .registerPattern("linux/loongarch64/libzstd-jni-*.so")
//        .registerPattern("linux/mips64/libzstd-jni-*.so")
//        .registerPattern("linux/ppc64/libzstd-jni-*.so")
//        .registerPattern("linux/ppc64le/libzstd-jni-*.so")
//        .registerPattern("linux/s390x/libzstd-jni-*.so")
        .registerPattern("win/amd64/libzstd-jni-*.dll")
//        .registerPattern("win/x86/libzstd-jni-*.dll")
      ;
    }
  }

}
