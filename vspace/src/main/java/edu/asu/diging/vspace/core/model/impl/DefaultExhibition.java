package edu.asu.diging.vspace.core.model.impl;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IDefaultExhibition;

@Entity
public class DefaultExhibition extends VSpaceElement implements IDefaultExhibition {

	@Id
	@GeneratedValue(generator = "id-generator")
	@GenericGenerator(name = "id-generator", 	
					parameters = @Parameter(name = "prefix", value = "EXH"), 
					strategy = "edu.asu.diging.vspace.core.data.IdGenerator"
			)
	private String id;
	
	@OneToOne(targetEntity=Space.class)
	private Space space;

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#getId()
	 */
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IDefaultExhibition#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#setId(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IDefaultExhibition#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#getSpaceLinks()
	 */
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IDefaultExhibition#getSpaceLinks()
	 */
	
	@Override
	public Space getSpace() {
		return this.space;
	}

	@Override
	public void setSpace(Space space) {
		this.space = space;
	}

}
