package cn.idealframework2.event;

import cn.idealframework2.idempotent.Idempotentable;

/**
 * 可进行幂等消费的事件接口
 *
 * @author 宋志宗 on 2022/11/15
 */
@SuppressWarnings("SpellCheckingInspection")
public interface IdempotentableEvent extends Event, Idempotentable {
}
