package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

public interface ISequenceManager {

    void createSequence(String moduleId,
            SequenceForm sequenceForm);

}
