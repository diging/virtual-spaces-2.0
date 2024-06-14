package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.ISlide;
@Entity
public class ExternalLinkSlide extends VSpaceElement implements IExternalLinkSlide {
    
    @Id 
    @GeneratedValue(generator = "extlinkslide_id_generator")
    @GenericGenerator(name = "extlinkslide_id_generator", 
        parameters = @Parameter(name = "prefix", value = "EXLS"), 
        strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @ManyToOne(targetEntity=Slide.class)
    @JoinColumn(name="slide_id", nullable=false)
    private ISlide slide;
  
    private String externalLink;
    
    private String label;
        
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public ISlide getSlide() {
        return slide;
    }

    @Override
    public void setSlide(ISlide slide) {
        this.slide = slide;
    }

    @Override
    public String getExternalLink() {
        return externalLink;
    }

    @Override
    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }
    
    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }
}
