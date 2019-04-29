package edu.asu.diging.vspace.core.services.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.factory.ISequenceFactory;
import edu.asu.diging.vspace.core.model.IModule;
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
    private ISequenceFactory sequenceFactory;
    
    @Autowired
    private SequenceRepository sequenceRepo;
    
    @Autowired
    private SlideRepository slideRepo;
    
    @Override
    public ISequence storeSequence(String moduleId, SequenceForm sequenceForm) {
        IModule module = moduleManager.getModule(moduleId);
        List<String> slideIds = new LinkedList<String>(Arrays.asList(sequenceForm.getOrderedSlideIds()));
        List<ISlide> slides = new LinkedList<>();
        slideRepo.findAllById(slideIds).forEach(slides::add);
        slides.forEach(s -> System.out.println("Slide" + s.getId()));
        ISequence sequence = sequenceFactory.createSequence(module, 
                sequenceForm, slides);
        sequenceRepo.save((Sequence) sequence);
        return sequence;
    }

    @Override
    public ISequence getSequence(String sequenceId) {
        Optional<Sequence> mayBeSequence = sequenceRepo.findById(sequenceId);
        if (mayBeSequence.isPresent()) {
            return mayBeSequence.get();
        }
        return null;
    }
}