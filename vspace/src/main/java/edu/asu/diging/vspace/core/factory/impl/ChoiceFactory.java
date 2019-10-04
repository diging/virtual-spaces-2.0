package edu.asu.diging.vspace.core.factory.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IChoiceFactory;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.impl.Choice;
import edu.asu.diging.vspace.core.services.ISequenceManager;

@Service
public class ChoiceFactory implements IChoiceFactory{
    
    @Autowired
    private ISequenceManager sequenceManager;

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.ISlideFactory#createChoices(java.util.List<java.lang.String>)
     */
    @Override
    public List<IChoice> createChoices(List<String> sequenceIds) {
        List<IChoice> choices = new ArrayList<IChoice>();
        for(String sequenceId: sequenceIds) {
            IChoice choice = new Choice();
            choice.setSequence(sequenceManager.getSequence(sequenceId));
            choices.add(choice);
           }
        return choices;        
    }
}
