package cn.idealframework2.event;

import java.io.Serial;
import java.util.LinkedHashMap;

/**
 * @author 宋志宗 on 2021/5/1
 */
public class GeneralEvent extends LinkedHashMap<String, Object> implements Event {
  @Serial
  private static final long serialVersionUID = 362498820763181265L;
}
