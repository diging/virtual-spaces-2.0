package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceTextBlock;

@Entity
public class SpaceTextBlock extends VSpaceElement implements ISpaceTextBlock {

    @Id 
    @GeneratedValue(generator = "spacetextblock_id_generator")
    @GenericGenerator(name = "spacetextblock_id_generator", 
        parameters = @Parameter(name = "prefix", value = "STB"), 
        strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @ManyToOne(targetEntity=Space.class)
    @JoinColumn(name="space_id", nullable=false)
    private ISpace space;

    
    private String text;

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
    public String getText() {
        return text;
    }
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.impl.IModuleLink#setModule(edu.asu.diging.vspace.core.model.IModule)
     */
    @Override
    public void setText(String text) {
        this.text = text;
    }
}
