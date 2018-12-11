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
	    int value = 0 ;
	    String query = String.format("SELECT id FROM %s ORDER BY creationDate DESC",
                obj.getClass().getSimpleName());
	    String prevId = (String) session.createQuery(query).setMaxResults(1).uniqueResult();
	    if(prevId == null) {
	        value = 1;
	        return this.prefix + String.format("%09d" , value);
	    } else {
    	    value = Integer.parseInt(prevId.substring(prevId.length() - 9)) + 1;
            String prefix = (String) prevId.subSequence(0, prevId.length() - 9);
            return  prefix + String.format("%09d" , value);
	    }
        
	}

}
/*
 * 
 * String prevId = (String) session.createQuery(query).setMaxResults(1).uniqueResult();
        System.out.println(prevId);
        int pre = prevId.length() - 9;
        int value = Integer.parseInt(prevId.substring(pre)) + 1;
        //int length = String.valueOf(value).length();
        
        String padded = String.format("%09d" , value);
        String pref = (String) prevId.subSequence(0, pre);
        return  pref + (padded);*/
