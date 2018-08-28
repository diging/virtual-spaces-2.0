package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IDefaultExhibition;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.IVSImage;

@Entity
public class DefaultExhibition extends VSpaceElement implements IDefaultExhibition {

	@Id
	@GeneratedValue(generator = "id-generator")
	@GenericGenerator(name = "id-generator", 	
					parameters = @Parameter(name = "prefix", value = "SPA"), 
					strategy = "edu.asu.diging.vspace.core.data.IdGenerator"
			)
	private String id;

	@OneToMany(mappedBy="sourceSpace", targetEntity=SpaceLink.class)
	private List<ISpaceLink> spaceLinks;

	@OneToMany(mappedBy = "space", targetEntity=ModuleLink.class)
	private List<IModuleLink> moduleLinks;
	
	@OneToOne(targetEntity=VSImage.class)
	private IVSImage image;

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
	public ISpace getSpace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSpace(ISpace space) {
		// TODO Auto-generated method stub
		
	}

}
