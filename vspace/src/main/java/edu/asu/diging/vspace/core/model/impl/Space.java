package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.IVSImage;

@Entity
public class Space extends VSpaceElement implements ISpace {

	@Id
	@GeneratedValue(generator = "space_id_generator")
	@GenericGenerator(name = "space_id_generator", 
	    parameters = @Parameter(name = "prefix", value = "SPA"), 
	    strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
	private String id;

	@OneToMany(mappedBy="sourceSpace", targetEntity=SpaceLink.class)
	private List<ISpaceLink> spaceLinks;

	@OneToMany(mappedBy = "space", targetEntity=ModuleLink.class)
	private List<IModuleLink> moduleLinks;
	
	@OneToMany(mappedBy = "space", targetEntity=ExternalLink.class)
	private List<IExternalLink> externalLinks;

	@OneToOne(targetEntity=VSImage.class)
	@NotFound(action=NotFoundAction.IGNORE)
	private IVSImage image;

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpace#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpace#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpace#getSpaceLinks()
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
	 * @see edu.asu.diging.vspace.core.model.impl.ISpace#getModuleLinks()
	 */
	@Override
	public List<IModuleLink> getModuleLinks() {
		return moduleLinks;
	}

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpace#setModuleLinks(java.util.List)
	 */
	@Override
	public void setModuleLinks(List<IModuleLink> moduleLinks) {
		this.moduleLinks = moduleLinks;
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#getExternalLinks()
	 */
	@Override
	public List<IExternalLink> getExternalLinks() {
		return externalLinks;
	}

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpace#setExternalLinks(java.util.List)
	 */
	@Override
	public void setExternalLinks(List<IExternalLink> externalLinks) {
		this.externalLinks = externalLinks;
	}

	/* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.impl.ISpace#getImage()
     */
	@Override
	public IVSImage getImage() {
		return image;
	}

	
	/* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.impl.ISpace#setImage(edu.asu.diging.vspace.core.model.IVSImage)
     */
	@Override
	public void setImage(IVSImage image) {
		this.image = image;
	}
}
