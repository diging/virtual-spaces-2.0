package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ISpace;

@Entity
public class Space extends VSpaceElement implements ISpace {

	@Id
	@GeneratedValue(generator = "id-generator")
	@GenericGenerator(name = "id-generator", 	
					parameters = @Parameter(name = "prefix", value = "SPA"), 
					strategy = "edu.asu.diging.vspace.core.data.IdGenerator"
			)
	private String id;

	@OneToMany
	@JoinTable
	private List<SpaceLink> spaceLinks;

	@OneToMany(mappedBy = "space")
	private List<ModuleLink> moduleLinks;

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
	public List<SpaceLink> getSpaceLinks() {
		return spaceLinks;
	}

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#setSpaceLinks(java.util.List)
	 */
	@Override
	public void setSpaceLinks(List<SpaceLink> spaceLinks) {
		this.spaceLinks = spaceLinks;
	}

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#getModuleLinks()
	 */
	@Override
	public List<ModuleLink> getModuleLinks() {
		return moduleLinks;
	}

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#setModuleLinks(java.util.List)
	 */
	@Override
	public void setModuleLinks(List<ModuleLink> moduleLinks) {
		this.moduleLinks = moduleLinks;
	}

}
