package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.ISpace;

@Entity
public class ExternalLink extends VSpaceElement implements IExternalLink {
	
	@Id 
	@GeneratedValue(generator = "extlink-id-generator")
	@GenericGenerator(name = "extlink-id-generator", 
          parameters = @Parameter(name = "prefix", value = "EXL000000000"), 
          strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
	private String id;
	
	@ManyToOne(targetEntity=Space.class)
	@JoinColumn(name="space_id", nullable=false)
	private ISpace space;

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IExternalLink#getId()
	 */
	@Override
	public String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IExternalLink#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IExternalLink#getSpace()
	 */
	@Override
	public ISpace getSpace() {
		return space;
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IExternalLink#setSpace(edu.asu.diging.vspace.core.model.ISpace)
	 */
	@Override
	public void setSpace(ISpace space) {
		this.space = space;
	}
}
