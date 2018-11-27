package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;

@Entity
public class ModuleLink extends VSpaceElement implements IModuleLink {

	@Id 
	@GeneratedValue(generator = "modlink-id-generator")
	@GenericGenerator(name = "modlink-id-generator", 
      parameters = @Parameter(name = "prefix", value = "MOL"), 
      strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
	private String id;
	
	@ManyToOne(targetEntity=Space.class)
    @JoinColumn(name="space_id", nullable=false)
	private ISpace space;
	
	@ManyToOne(targetEntity=Module.class)
    @JoinColumn(name="module_id", nullable=false)
	private IModule module;
	
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IModuleLink#getId()
	 */
	@Override
	public String getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IModuleLink#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IModuleLink#getSpace()
	 */
	@Override
	public ISpace getSpace() {
		return space;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IModuleLink#setSpace(edu.asu.diging.vspace.core.model.ISpace)
	 */
	@Override
	public void setSpace(ISpace space) {
		this.space = space;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IModuleLink#getModule()
	 */
	@Override
	public IModule getModule() {
		return module;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IModuleLink#setModule(edu.asu.diging.vspace.core.model.IModule)
	 */
	@Override
	public void setModule(IModule module) {
		this.module = module;
	}
}
