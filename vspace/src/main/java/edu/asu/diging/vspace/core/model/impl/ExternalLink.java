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
    @GeneratedValue(generator = "extlink_id_generator")
    @GenericGenerator(name = "extlink_id_generator", 
        parameters = @Parameter(name = "prefix", value = "EXL"), 
        strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @ManyToOne(targetEntity=Space.class)
    @JoinColumn(name="space_id", nullable=false)
    private ISpace space;
  
    private String externalLink;

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

    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.impl.IExternalLink#getExternalLink()
     */
    @Override
    public String getExternalLink() {
        return externalLink;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.impl.IExternalLink#setExternalLink(java.lang.String)
     */
    @Override
    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    @Override
    public ExternalLinkValue getTarget() {
        return new ExternalLinkValue(this.externalLink);
    }

    @Override
    public void setTarget(ExternalLinkValue target) {
        this.externalLink = target.getValue();
    }
}
