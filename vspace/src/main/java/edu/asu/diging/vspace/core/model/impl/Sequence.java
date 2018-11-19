package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;

@Entity
public class Sequence extends VSpaceElement implements ISequence {

    @Id
    @GeneratedValue(generator = "sequence-id-generator")
    @GenericGenerator(name = "sequence-id-generator", parameters = @Parameter(name = "prefix", value = "SEQ"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @OneToMany(targetEntity = Slide.class)
    private List<ISlide> slides;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISequence#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISequence#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISequence#getSlides()
     */
    @Override
    public List<ISlide> getSlides() {
        return slides;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.ISequence#setSlides(java.util.List)
     */
    @Override
    public void setSlides(List<ISlide> slides) {
        this.slides = slides;
    }
}
