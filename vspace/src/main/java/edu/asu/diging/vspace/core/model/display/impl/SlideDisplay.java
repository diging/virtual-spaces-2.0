package edu.asu.diging.vspace.core.model.display.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.ISlideDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;

@Entity
public class SlideDisplay implements ISlideDisplay{
    
    @Id
    @GeneratedValue(generator = "slide_display_id_generator")
    @GenericGenerator(name = "slide_display_id_generator", parameters = @Parameter(name = "prefix", value = "SLD"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @OneToOne(targetEntity = Slide.class)
    private ISlide slide;

    private int width;
    private int height;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.ISlideeDisplay#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.display.impl.ISlideDisplay#setId(java.lang.
     * String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.ISlideDisplay#getSlide()
     */
    @Override
    public ISlide getSlide() {
        return slide;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.display.impl.ISlideDisplay#setSlide(edu.asu.
     * diging.vspace.core.model.ISlide)
     */
    @Override
    public void setSlide(ISlide slide) {
        this.slide = slide;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.ISlideDisplay#getWidth()
     */
    @Override
    public int getWidth() {
        return width;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.display.impl.ISlideDisplay#setWidth(int)
     */
    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.ISlideDisplay#getHeight()
     */
    @Override
    public int getHeight() {
        return height;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.display.impl.ISlideDisplay#setHeight(int)
     */
    @Override
    public void setHeight(int height) {
        this.height = height;
    }

}
