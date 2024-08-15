package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

public interface ISequenceManager {

    ISequence storeSequence(String moduleId, SequenceForm sequenceForm);

    ISequence getSequence(String sequenceId);
    
    void updateSequence(ISequence sequence);

    List<ISequence> getAllSequencesForModules();
    
}
