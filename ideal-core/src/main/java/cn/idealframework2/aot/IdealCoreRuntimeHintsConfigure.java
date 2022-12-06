package cn.idealframework2.aot;

import cn.idealframework2.event.BaseEvent;
import cn.idealframework2.event.GeneralEvent;
import cn.idealframework2.event.jdbc.JdbcEventTemp;
import cn.idealframework2.event.mongo.MongoEventLock;
import cn.idealframework2.event.mongo.MongoEventTemp;
import cn.idealframework2.lang.Label;
import cn.idealframework2.lang.Pair;
import cn.idealframework2.lang.Triple;
import cn.idealframework2.lang.Tuple;
import cn.idealframework2.trace.OperationLog;
import cn.idealframework2.trace.Operator;
import cn.idealframework2.trace.TraceContext;
import cn.idealframework2.transmission.*;
import io.vertx.ext.mail.impl.MailClientImpl;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.context.annotation.ImportRuntimeHints;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author 宋志宗 on 2022/9/30
 */
@ImportRuntimeHints(IdealCoreRuntimeHintsConfigure.IdealCoreRuntimeHints.class)
public class IdealCoreRuntimeHintsConfigure {

  public static class IdealCoreRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(@Nonnull RuntimeHints hints, @Nullable ClassLoader classLoader) {
      hints.serialization()
        .registerType(java.util.HashSet.class)
        .registerType(java.util.LinkedHashSet.class);
      hints.reflection()
        .registerTypes(
          Arrays.asList(
            TypeReference.of(java.util.HashSet.class),
            TypeReference.of(java.util.LinkedHashSet.class),
            TypeReference.of(MailClientImpl.class),
            TypeReference.of(JdbcEventTemp.class),
            TypeReference.of(MongoEventLock.class),
            TypeReference.of(MongoEventTemp.class),
            TypeReference.of(BaseEvent.class),
            TypeReference.of(GeneralEvent.class),
            TypeReference.of(Label.class),
            TypeReference.of(Pair.class),
            TypeReference.of(Triple.class),
            TypeReference.of(Tuple.class),
            TypeReference.of(OperationLog.class),
            TypeReference.of(Operator.class),
            TypeReference.of(TraceContext.class),
            TypeReference.of(BasicResult.class),
            TypeReference.of(ListResult.class),
            TypeReference.of(PageResult.class),
            TypeReference.of(Paging.class),
            TypeReference.of(Result.class),
            TypeReference.of(Sort.class),
            TypeReference.of(SortablePaging.class)
          ),
          builder -> builder.withMembers(MemberCategory.values())
        )
      ;
    }
  }

}
