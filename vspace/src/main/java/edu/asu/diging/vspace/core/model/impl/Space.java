package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.IVSImage;

@Entity
public class Space extends VSpaceElement implements ISpace {

	@Id
	@GeneratedValue(generator = "id-generator")
	@GenericGenerator(name = "id-generator", 
	  parameters = @Parameter(name = "prefix", value = "SPA"), 
	  strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
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
	@Override
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#getSpaceLinks()
	 */
	@Override
	public List<ISpaceLink> getSpaceLinks() {
		return spaceLinks;
	}

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#setSpaceLinks(java.util.List)
	 */
	@Override
	public void setSpaceLinks(List<ISpaceLink> spaceLinks) {
		this.spaceLinks = spaceLinks;
	}

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#getModuleLinks()
	 */
	@Override
	public List<IModuleLink> getModuleLinks() {
		return moduleLinks;
	}

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#setModuleLinks(java.util.List)
	 */
	@Override
	public void setModuleLinks(List<IModuleLink> moduleLinks) {
		this.moduleLinks = moduleLinks;
	}

	public IVSImage getImage() {
		return image;
	}

	public void setImage(IVSImage image) {
		this.image = image;
	}
}
