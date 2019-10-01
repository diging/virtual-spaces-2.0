package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.factory.ISequenceFactory;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

@Transactional
@Service
public class SequenceManager implements ISequenceManager {

    @Autowired
    private ModuleManager moduleManager;

    @Autowired
    private SlideManager slideManager;

    @Autowired
    private ISequenceFactory sequenceFactory;

    @Autowired
    private SequenceRepository sequenceRepo;

    @Override
    public ISequence storeSequence(String moduleId, SequenceForm sequenceForm) {
        List<ISlide> slides = new ArrayList<>();
        for (String slideId : sequenceForm.getOrderedSlides()) {
            slides.add(slideManager.getSlide(slideId));
        }
        ISequence sequence = sequenceFactory.createSequence(moduleManager.getModule(moduleId), sequenceForm, slides);
        return sequenceRepo.save((Sequence) sequence);

    }

    @Override
    public ISequence getSequence(String sequenceId) {
        Optional<Sequence> mayBeSequence = sequenceRepo.findById(sequenceId);
        if (mayBeSequence.isPresent()) {
            return mayBeSequence.get();
        }
        return null;
    }

    @Override
    public void updateSequence(ISequence sequence) {
        sequenceRepo.save((Sequence) sequence);
    }
}