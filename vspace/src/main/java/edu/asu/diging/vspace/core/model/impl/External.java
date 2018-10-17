package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IExternal;

public class External extends VSpaceElement implements IExternal {

	@Id 
	@GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "external-id-generator", 
      parameters = @Parameter(name = "prefix", value = "EXT"), 
      strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
	private String id;
	
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IExternal#getId()
	 */
	@Override
	public String getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IExternal#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
}
