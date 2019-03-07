package edu.asu.diging.vspace.web.staff;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.VSImage;

@Entity
public class ContentBlock extends Slide implements IContentBlock {
    
    @Id
    @GeneratedValue(generator = "content_id_generator")
    @GenericGenerator(name = "content_id_generator", parameters = @Parameter(name = "prefix", value = "CON"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    private String textblock;
    
    @OneToOne(targetEntity = VSImage.class)
    private IVSImage imageblock;
    
//    @OneToOne(targetEntity = Slide.class)
//    private ISlide slide;
    

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
//    public ISlide getSlide() {
//        return slide;
//    }
//    public void setSlide(ISlide slide) {
//        this.slide = slide;
//    }
    public String getTextblock() {
        return textblock;
    }
    public void setTextblock(String textblock) {
        this.textblock = textblock;
    }
    public IVSImage getImageblock() {
        return imageblock;
    }
    public void setImageblock(IVSImage imageblock) {
        this.imageblock = imageblock;
    }
    

}
