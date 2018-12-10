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
		String query = String.format("SELECT * FROM %s ORDER BY creationDate DESC LIMIT 1",
	            obj.getClass().getSimpleName());
		String id = (String) session.createQuery(query).uniqueResult();
		int value = Integer.parseInt(id.substring(3));
		return prefix + ( value + 1 );
	}

}
