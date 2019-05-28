package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

public interface ISequenceManager {

    ISequence storeSequence(String moduleId, SequenceForm sequenceForm);

    ISequence getSequence(String sequenceId);

}