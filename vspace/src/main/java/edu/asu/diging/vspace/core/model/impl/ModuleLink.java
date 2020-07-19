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
import edu.asu.diging.vspace.core.model.IVSpaceElement;

@Entity
public class ModuleLink extends VSpaceElement implements IModuleLink {

    @Id 
    @GeneratedValue(generator = "modlink_id_generator")
    @GenericGenerator(name = "modlink_id_generator", 
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

    @Override
    public IModule getTarget() {
        return this.module;
    }

    //    @Override
    //    public void setTarget(IModule target) {
    //        this.module = target;
    //    }
    @Override
    public void setTarget(IVSpaceElement target) {
        // TODO Auto-generated method stub
        this.module = (IModule) target;
    }
}
