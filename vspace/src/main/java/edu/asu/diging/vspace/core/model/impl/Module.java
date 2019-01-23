package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IModule;

@Entity
public class Module extends VSpaceElement implements IModule {

	@Id 
	@GeneratedValue(generator = "id_generator")
    @GenericGenerator(name = "id_generator", 
      parameters = @Parameter(name = "prefix", value = "MOD"), 
      strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
	private String id;
	
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IModule#getId()
	 */
	@Override
	public String getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IModule#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
}
