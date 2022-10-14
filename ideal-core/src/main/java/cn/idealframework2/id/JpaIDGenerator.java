package cn.idealframework2.id;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2020/11/12
 */
public class JpaIDGenerator extends IdentityGenerator {
  private static IDGenerator idGenerator;

  public static IDGenerator getIdGenerator() {
    return idGenerator;
  }

  public static void setIdGenerator(@Nonnull IDGenerator idGenerator) {
    JpaIDGenerator.idGenerator = idGenerator;
  }

  @Override
  public Object generate(SharedSessionContractImplementor s, Object obj) {
    return idGenerator.generate();
  }
}
