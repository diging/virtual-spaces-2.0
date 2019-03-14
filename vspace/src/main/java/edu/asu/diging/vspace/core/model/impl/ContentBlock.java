package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.ISlide;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ContentBlock extends VSpaceElement implements IContentBlock {
    
    @Id
    @GeneratedValue(generator = "content_id_generator")
    @GenericGenerator(name = "content_id_generator", parameters = @Parameter(name = "prefix", value = "CON"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    @OneToOne(targetEntity = Slide.class)
    private ISlide slide;
    
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
}
