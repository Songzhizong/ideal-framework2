package cn.idealframework2.aot;

import cn.idealframework2.data.hibernate.JpaIDGenerator;
import cn.idealframework2.data.hibernate.ManualIDGenerator;
import org.hibernate.id.IdentityGenerator;
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
@ConditionalOnClass({IdentityGenerator.class})
@ImportRuntimeHints(HibernateRuntimeHintsConfigure.HibernateRuntimeHints.class)
public class HibernateRuntimeHintsConfigure {

  public static class HibernateRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(@Nonnull RuntimeHints hints, @Nullable ClassLoader classLoader) {
      hints.reflection()
        .registerTypes(
          Arrays.asList(
            TypeReference.of(JpaIDGenerator.class),
            TypeReference.of(ManualIDGenerator.class)
          ),
          builder -> builder.withMembers(MemberCategory.values())
        )
      ;
    }
  }

}
