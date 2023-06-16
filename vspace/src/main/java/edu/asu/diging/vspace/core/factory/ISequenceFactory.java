package edu.asu.diging.vspace.core.factory;

import java.util.List;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

/**
 * The edu.asu.diging.vspace.core.factory.ISequenceFactory interface defines a factory for creating instances of
 * the edu.asu.diging.vspace.core.model.ISequence interface.
 * 
 * Implementations of this interface must provide a method for creating new
 * sequence objects.
 * 
 */
public interface ISequenceFactory {
    
    /**
     * Creates a new instance of the ISequence interface with the given module,
     * sequence form, and list of slides, and returns the new object.
     * 
     * @param module the  edu.asu.diging.vspace.core.model.IModule object to associate with the new sequence object
     * @param edu.asu.diging.vspace.web.staff.forms.sequenceForm the SequenceForm object to use for the new sequence object
     * @param slides the list of  edu.asu.diging.vspace.core.model.ISlide objects to include in the new sequence object
     * @return the new  edu.asu.diging.vspace.core.model.ISequence object
     */
    ISequence createSequence(IModule module, SequenceForm sequenceForm, List<ISlide> slides);
}
