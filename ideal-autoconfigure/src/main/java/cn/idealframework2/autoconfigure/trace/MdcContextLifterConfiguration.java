package cn.idealframework2.autoconfigure.trace;

import cn.idealframework2.starter.model.webflux.WebfluxModel;
import cn.idealframework2.trace.TraceConstants;
import org.reactivestreams.Subscription;
import org.slf4j.MDC;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import reactor.core.CoreSubscriber;
import reactor.core.Disposable;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;
import reactor.util.context.Context;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 宋志宗 on 2022/11/17
 */
@Configuration
@ConditionalOnClass({MDC.class, CoreSubscriber.class, Context.class, WebfluxModel.class})
public class MdcContextLifterConfiguration implements SmartInitializingSingleton, Disposable {
  private static final String MDC_CONTEXT_REACTOR_KEY = MdcContextLifterConfiguration.class.getName();

  @Override
  public void afterSingletonsInstantiated() {
    Hooks.onEachOperator(MDC_CONTEXT_REACTOR_KEY, Operators.lift((s1, s2) -> new MdcContextLifter<>(s2)));
  }

  @Override
  public void dispose() {
    Hooks.resetOnEachOperator(MDC_CONTEXT_REACTOR_KEY);
  }

  public static class MdcContextLifter<T> implements CoreSubscriber<T> {
    private final CoreSubscriber<T> coreSubscriber;

    public MdcContextLifter(CoreSubscriber<T> coreSubscriber) {
      this.coreSubscriber = coreSubscriber;
    }

    @Override
    public void onSubscribe(@Nonnull Subscription subscription) {
      coreSubscriber.onSubscribe(subscription);
    }

    @Override
    public void onNext(T t) {
      Context context = coreSubscriber.currentContext();
      if (context.isEmpty()) {
        MDC.clear();
      } else {
        Map<String, String> ctx = new HashMap<>(16);
        context.forEach((k, v) -> {
          if (k == null || v == null) {
            return;
          }
          String key = k.toString();
          if (TraceConstants.CTX_KEY.equals(key)) {
            return;
          }
          ctx.put(key, v.toString());
        });
        MDC.setContextMap(ctx);
      }
      coreSubscriber.onNext(t);
    }

    @Override
    public void onError(Throwable throwable) {
      coreSubscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
      coreSubscriber.onComplete();
    }

    @Nonnull
    @Override
    public Context currentContext() {
      return coreSubscriber.currentContext();
    }
  }
}
