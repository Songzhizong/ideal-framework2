package cn.idealframework2;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.ImportRuntimeHints;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/9/30
 */
@ImportRuntimeHints(IdealCoreRuntimeHints.class)
public class IdealCoreRuntimeHints implements RuntimeHintsRegistrar {

  @Override
  public void registerHints(@Nonnull RuntimeHints hints, @Nullable ClassLoader classLoader) {

  }
}
