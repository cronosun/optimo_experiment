package ch.bedag.sie.dij.optimo.persistence;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.id.UUIDGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class IdGenerator extends IdentityGenerator implements Configurable {
    private final UUIDGenerator hibernateGenerator = new UUIDGenerator();

    public IdGenerator() {
    }

    public Serializable generate(SharedSessionContractImplementor s, Object obj) {
        if (obj == null) {
            throw new HibernateException(new NullPointerException());
        } else {
            BaseEntity baseEntity = (BaseEntity) obj;
            return baseEntity.getId() == null ? this.hibernateGenerator.generate(s, obj) : baseEntity.getId();
        }
    }

    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        this.hibernateGenerator.configure(type, properties, serviceRegistry);
    }
}
