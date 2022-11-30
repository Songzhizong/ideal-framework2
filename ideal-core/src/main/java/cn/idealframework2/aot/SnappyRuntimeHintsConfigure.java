package cn.idealframework2.aot;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.xerial.snappy.Snappy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/9/30
 */
@ConditionalOnClass(Snappy.class)
@ImportRuntimeHints(SnappyRuntimeHintsConfigure.SnappyRuntimeHints.class)
public class SnappyRuntimeHintsConfigure {

  public static class SnappyRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(@Nonnull RuntimeHints hints, @Nullable ClassLoader classLoader) {
      hints.resources()
//        .registerPattern("org/xerial/snappy/native/AIX/ppc/*.a")
//        .registerPattern("org/xerial/snappy/native/AIX/ppc64/*.a")
//        .registerPattern("org/xerial/snappy/native/FreeBSD/x86_64/*.so")
        .registerPattern("org/xerial/snappy/native/Linux/aarch64/*.so")
//        .registerPattern("org/xerial/snappy/native/Linux/android-arm/*.so")
//        .registerPattern("org/xerial/snappy/native/Linux/arm/*.so")
//        .registerPattern("org/xerial/snappy/native/Linux/armv6/*.so")
//        .registerPattern("org/xerial/snappy/native/Linux/armv7/*.so")
//        .registerPattern("org/xerial/snappy/native/Linux/ppc/*.so")
//        .registerPattern("org/xerial/snappy/native/Linux/ppc64/*.so")
//        .registerPattern("org/xerial/snappy/native/Linux/ppc64le/*.so")
//        .registerPattern("org/xerial/snappy/native/Linux/s390x/*.so")
//        .registerPattern("org/xerial/snappy/native/Linux/x86/*.so")
        .registerPattern("org/xerial/snappy/native/Linux/x86_64/*.so")
        .registerPattern("org/xerial/snappy/native/Mac/aarch64/*.dylib")
//        .registerPattern("org/xerial/snappy/native/Mac/x86/*.dylib")
        .registerPattern("org/xerial/snappy/native/Mac/x86_64/*.dylib")
//        .registerPattern("org/xerial/snappy/native/SunOS/sparc/*.so")
//        .registerPattern("org/xerial/snappy/native/SunOS/x86/*.so")
//        .registerPattern("org/xerial/snappy/native/SunOS/x86_64/*.so")
//        .registerPattern("org/xerial/snappy/native/Windows/x86/*.dll")
        .registerPattern("org/xerial/snappy/native/Windows/x86_64/*.dll");
    }
  }

}
