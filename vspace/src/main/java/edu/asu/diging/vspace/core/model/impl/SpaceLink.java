package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;

@Entity
public class SpaceLink extends VSpaceElement implements ISpaceLink {

    @Id 
    @GeneratedValue(generator = "spalink_id_generator")
    @GenericGenerator(name = "spalink_id_generator", 
        parameters = @Parameter(name = "prefix", value = "SPL"), 
        strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @JsonIgnore
    @ManyToOne(targetEntity=Space.class)
    @JoinColumn(name = "source_space_id")
    @NotFound(action=NotFoundAction.IGNORE)
    private ISpace sourceSpace;

    @ManyToOne(targetEntity=Space.class)
    @JoinColumn(name = "target_space_id")
    @NotFound(action=NotFoundAction.IGNORE)
    private ISpace targetSpace;
    
    /* 
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISpaceLink#getId()
     */
    @Override
    public String getId() {
        return id;
    }
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.impl.ISpaceLink#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.impl.ISpaceLink#getSourceSpace()
     */
    @Override
    public ISpace getSourceSpace() {
        return sourceSpace;
    }
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.impl.ISpaceLink#setSourceSpace(edu.asu.diging.vspace.core.model.ISpace)
     */
    @Override
    public void setSourceSpace(ISpace sourceSpace) {
        this.sourceSpace = sourceSpace;
    }
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.impl.ISpaceLink#getTargetSpace()
     */
    @Override
    public ISpace getTargetSpace() {
        return targetSpace;
    }
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.impl.ISpaceLink#setTargetSpace(edu.asu.diging.vspace.core.model.ISpace)
     */
    @Override
    public void setTargetSpace(ISpace targetSpace) {
        this.targetSpace = targetSpace;
    }
    @Override
    public ISpace getSpace() {
        return sourceSpace;
    }
    @Override
    public void setSpace(ISpace space) {
        this.sourceSpace = space;

    }
    @Override
    public ISpace getTarget() {
        return targetSpace;
    }

    @Override
    public void setTarget(ISpace target) {
        this.targetSpace = target;

    }
}
