package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.ISequence;

@Entity
public class Choice extends VSpaceElement implements IChoice {

    @Id
    @GeneratedValue(generator = "choice_id_generator")
    @GenericGenerator(name = "choice_id_generator", 
        parameters = @Parameter(name = "prefix", value = "CHO"), 
        strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @OneToOne(targetEntity = Slide.class)
    private ISequence sequence;
    
    @ManyToMany(mappedBy = "choices", targetEntity = BranchingPoint.class)
    private List<IBranchingPoint> bpoint;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.IChoice#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.IChoice#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.IChoice#getSequence()
     */
    @Override
    public ISequence getSequence() {
        return sequence;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.IChoice#setSequence(edu.asu.diging.
     * vspace.core.model.ISequence)
     */
    @Override
    public void setSequence(ISequence sequence) {
        this.sequence = sequence;
    }
}
