package cn.idealframework2.data.hibernate;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2020/11/24
 */
public class ManualIDGenerator extends IdentityGenerator {

  @Override
  public Object generate(@Nonnull SharedSessionContractImplementor s, @Nonnull Object obj) {
    return s.getEntityPersister(null, obj).getIdentifier(obj, s);
  }
}
