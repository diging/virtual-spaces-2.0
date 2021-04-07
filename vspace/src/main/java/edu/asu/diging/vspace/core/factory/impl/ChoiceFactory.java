package edu.asu.diging.vspace.core.factory.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IChoiceFactory;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.impl.Choice;
import edu.asu.diging.vspace.core.services.ISequenceManager;

@Service
public class ChoiceFactory implements IChoiceFactory {

    @Autowired
    private ISequenceManager sequenceManager;

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.IChoiceFactory#createChoices(java.util.List<java.lang.String>)
     */
    @Override
    public List<IChoice> createChoices(List<String> sequenceIds) {
        List<ISequence> sequences=sequenceIds.stream().map(sequenceId->sequenceManager.getSequence(sequenceId)).collect(Collectors.toList());
        return sequences.stream().map(sequence -> new Choice(sequence)).collect(Collectors.toList());        
    }
}
