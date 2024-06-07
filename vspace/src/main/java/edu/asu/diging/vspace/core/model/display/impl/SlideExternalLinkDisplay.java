package edu.asu.diging.vspace.core.model.display.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ISlideExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkSlide;

@Entity
public class SlideExternalLinkDisplay extends LinkDisplay implements ISlideExternalLinkDisplay {
    
    @Id
    @GeneratedValue(generator = "externallink_display_id_generator")
    @GenericGenerator(name = "externallink_display_id_generator", 
      	parameters = @Parameter(name = "prefix", value = "EXLDS"),
      	strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @OneToOne(targetEntity = ExternalLinkSlide.class)
    private IExternalLinkSlide externalLink;
    
    private DisplayType type;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.ISpaceLinkDisplay#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.display.impl.ISpaceLinkDisplay#setId(java.
     * lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.IExternalLinkDisplay#
     * getExternalLinkSlide()
     */
    @Override
    public IExternalLinkSlide getExternalLink() {
        return externalLink;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.IExternalLinkDisplay#
     * setExternalLink(IExternalLinkSlide)
     */
    @Override
    public void setExternalLink(IExternalLinkSlide externalLink) {
        this.externalLink = externalLink;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.IExternalLinkDisplay#
     * getType()
     */
    @Override
    public DisplayType getType() {
        return type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.IExternalLinkDisplay#
     * setType(DisplayType)
     */
    @Override
    public void setType(DisplayType type) {
        this.type = type;
    }
    

}
