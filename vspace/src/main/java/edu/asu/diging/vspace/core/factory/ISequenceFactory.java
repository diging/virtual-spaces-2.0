package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

public interface ISequenceFactory {
    
    ISequence createSequence(IModule module, SequenceForm sequenceForm);
}
