package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        String[] orderedSlideIds = sequenceForm.getOrderedSlideIds().split(",");        
        List<String> slideIds = Arrays.asList(orderedSlideIds);
        Map<String, Integer> idOrderMap = new HashMap<>();
        for(int i = 0; i<orderedSlideIds.length; ++i) {
            idOrderMap.put(orderedSlideIds[i], i);
        }
        List<ISlide> slides = new ArrayList<>();
        slideRepo.findAllById(slideIds).forEach(slides::add);
        Collections.sort(slides, (ISlide s1, ISlide s2) 
                -> idOrderMap.get(s1.getId()).compareTo(idOrderMap.get(s2.getId())));
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