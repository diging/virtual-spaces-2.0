package edu.asu.diging.vspace.core.data;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class IdGenerator implements IdentifierGenerator, Configurable {
	
	private String prefix;

	@Override
	public void configure(Type type, Properties properties, ServiceRegistry sr) throws MappingException {
		prefix = properties.getProperty("prefix");
	}

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
		String query = String.format("select count(*) from %s",
	            obj.getClass().getSimpleName());
		long count = (Long) session.createQuery(query).uniqueResult();
		return prefix + (count + 1);
	}

}
