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

    private Integer contentOrder;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.IContentBlock#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.IContentBlock#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.IContentBlock#getSlide()
     */
    @Override
    public ISlide getSlide() {
        return slide;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.IContentBlock#setSlide(edu.asu.diging.
     * vspace. core.model.ISlide)
     */
    @Override
    public void setSlide(ISlide slide) {
        this.slide = slide;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.IContentBlock#getBlockInOrder()
     */
    @Override
    public Integer getContentOrder() {
        return contentOrder;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.IContentBlock#setBlockInOrder(java.lang
     * .Integer)
     */
    @Override
    public void setContentOrder(Integer contentOrder) {
        this.contentOrder = contentOrder;
    }

}
