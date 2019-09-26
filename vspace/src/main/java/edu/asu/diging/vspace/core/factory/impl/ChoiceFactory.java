package edu.asu.diging.vspace.core.factory.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ChoiceRepository;
import edu.asu.diging.vspace.core.factory.IChoiceFactory;
import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.impl.Choice;
import edu.asu.diging.vspace.core.services.ISequenceManager;

@Service
public class ChoiceFactory implements IChoiceFactory{
    
    @Autowired
    private ISequenceManager sequenceManager;
   
    @Autowired
    private ChoiceRepository choiceRepo;
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.ISlideFactory#createChoices(java.util.List<java.lang.String>, edu.asu.diging.vspace.core.model.IBranchingPoint)
     */
    @Override
    public List<IChoice> createChoices(List<String> choices, IBranchingPoint branchingPoint) {
        List<IChoice> choiceList = new ArrayList<IChoice>();
        for(String choiceId: choices) {
            IChoice choice = new Choice();
            choice.setSequence(sequenceManager.getSequence(choiceId));  
            choiceList.add(choice);
        }
            return choiceList;        
    }
}
