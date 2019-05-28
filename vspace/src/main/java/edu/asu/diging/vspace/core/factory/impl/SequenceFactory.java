package edu.asu.diging.vspace.core.factory.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ISequenceFactory;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

@Service
public class SequenceFactory implements ISequenceFactory{
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.IImageFactory#createImage(java.lang.
     * String, java.lang.String)
     */
    @Override
    public ISequence createSequence(IModule module, SequenceForm sequenceForm, List<ISlide> slides) {
        ISequence sequence = new Sequence();
        sequence.setName(sequenceForm.getName());;
        sequence.setDescription(sequenceForm.getDescription());
        sequence.setSlides(slides);
        sequence.setModule(module);
        return sequence;
    }
}
