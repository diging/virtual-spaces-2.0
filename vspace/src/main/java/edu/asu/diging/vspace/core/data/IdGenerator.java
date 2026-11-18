package edu.asu.diging.vspace.core.data;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.hibernate.type.descriptor.java.LongJavaType;
import org.hibernate.type.descriptor.jdbc.BigIntJdbcType;
import org.hibernate.type.internal.BasicTypeImpl;

public class IdGenerator extends SequenceStyleGenerator implements IdentifierGenerator, Configurable {

    private String prefix;

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry sr) throws MappingException {
        // The entity id is a String, but the underlying sequence produces longs.
        super.configure(new BasicTypeImpl<>(LongJavaType.INSTANCE, BigIntJdbcType.INSTANCE), properties, sr);
        prefix = properties.getProperty("prefix");
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        return prefix + String.format("%09d", super.generate(session, obj));
    }
}
