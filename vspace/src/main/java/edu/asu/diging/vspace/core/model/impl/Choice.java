package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.ISequence;

@Entity
public class Choice extends VSpaceElement implements IChoice {

    @Id
    @GeneratedValue(generator = "choice-id-generator")
    @GenericGenerator(name = "choice-id-generator", 
      parameters = @Parameter(name = "prefix", value = "CHO"), 
      strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @OneToOne(targetEntity = Sequence.class)
    private ISequence sequence;

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
