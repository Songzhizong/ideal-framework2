package cn.idealframework2.autoconfigure.id;

import cn.idealframework2.data.hibernate.JpaIDGenerator;
import cn.idealframework2.id.IDGenerator;
import cn.idealframework2.id.IDGeneratorFactory;
import org.hibernate.id.IdentityGenerator;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/10/14
 */
@ConditionalOnClass({IdentityGenerator.class})
public class JpaIDGeneratorInitializing implements SmartInitializingSingleton {
  @Nullable
  private final IDGeneratorFactory idGeneratorFactory;

  public JpaIDGeneratorInitializing(@Nullable @Autowired(required = false)
                                    IDGeneratorFactory idGeneratorFactory) {
    this.idGeneratorFactory = idGeneratorFactory;
  }

  @Override
  public void afterSingletonsInstantiated() {
    if (idGeneratorFactory != null) {
      IDGenerator idGenerator = idGeneratorFactory.getGenerator("jpa");
      JpaIDGenerator.setIdGenerator(idGenerator);
    }
  }
}
