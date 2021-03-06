package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;

@Entity
public class Sequence extends VSpaceElement implements ISequence {

    @Id
    @GeneratedValue(generator = "sequence_id_generator")
    @GenericGenerator(name = "sequence_id_generator", 
        parameters = @Parameter(name = "prefix", value = "SEQ"), 
        strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    @ManyToOne(targetEntity = Module.class)
    private IModule module;

    @ManyToMany(targetEntity = Slide.class)
    @JoinTable(name="Sequence_Slides",
    joinColumns = @JoinColumn(name = "Sequence_Id", referencedColumnName="id"),
    inverseJoinColumns = @JoinColumn(name = "Slide_Id", referencedColumnName="id"))  
    @OrderColumn(name="slide_order")
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
    
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.impl.ISequence#getModule()
     */
    public IModule getModule() {
        return module;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.impl.ISequence#setModule(edu.asu.diging.vspace.
     * core.model.IModule)
     */
    public void setModule(IModule module) {
        this.module = module;
    }
}
