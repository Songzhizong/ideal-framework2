package cn.idealframework2.aot;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.ImportRuntimeHints;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/9/30
 */
@ImportRuntimeHints(IdealCoreRuntimeHintsConfigure.IdealCoreRuntimeHints.class)
public class IdealCoreRuntimeHintsConfigure {

  public static class IdealCoreRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(@Nonnull RuntimeHints hints, @Nullable ClassLoader classLoader) {

    }
  }

}
