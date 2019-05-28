package edu.asu.diging.vspace.core.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.factory.impl.SlideFactory;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

@Service
public class SlideManager implements ISlideManager {
    
    @Autowired
    private SlideRepository slideRepo;
    
    @Autowired
    private SlideFactory slideFactory;
    
    @Autowired
    private ModuleManager moduleManager;
    
    @Autowired
    private SequenceManager sequenceManager;
    
    @Override
    public ISlide createSlide(String moduleId, SlideForm slideForm) {
        IModule module = moduleManager.getModule(moduleId);
        ISlide slide = slideFactory.createSlide(module, slideForm);
        slideRepo.save((Slide) slide);
        return slide;
    }
    
    @Override
    public ISlide getSlide(String slideId) {
        Optional<Slide> slide = slideRepo.findById(slideId);
        if (slide.isPresent()) {
            return slide.get();
        }
        return null;
    }

    @Override
    public List<ISlide> getSlidesInSequence(String sequenceId) {
        return sequenceManager.getSequence(sequenceId).getSlides();
    }
}
