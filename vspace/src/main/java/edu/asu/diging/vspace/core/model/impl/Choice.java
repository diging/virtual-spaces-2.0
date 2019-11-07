package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.ISequence;

@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
public class Choice extends VSpaceElement implements IChoice {

    @Id
    @GeneratedValue(generator = "choice_id_generator")
    @GenericGenerator(name = "choice_id_generator", 
        parameters = @Parameter(name = "prefix", value = "CHO"), 
        strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

//    @OneToMany(targetEntity = ChoiceBlock.class)
//    @JoinTable(name = "ChoiceBlck_Choice",
//    joinColumns = {@JoinColumn(name = "ChoiceBlock_id")},
//    inverseJoinColumns = {@JoinColumn(name = "choices_id")}
//    )
//    private List<IChoiceBlock> choiceBlock;

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
    
//    public IChoiceBlock getChoiceBlock() {
//        return choiceBlock;
//    }
//
//    public void setChoiceBlock(IChoiceBlock choiceBlock) {
//        this.choiceBlock = choiceBlock;
//    }
}
