package edu.asu.diging.vspace.core.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.BranchingPointRepository;
import edu.asu.diging.vspace.core.data.ChoiceRepository;
import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.factory.impl.SlideFactory;
import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.Choice;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

@Service
public class SlideManager implements ISlideManager {

    @Autowired
    private SlideFactory slideFactory;
 
    @Autowired
    private SlideRepository slideRepo;
    
    @Autowired
    private BranchingPointRepository bpointRepo;
    
    @Autowired
    private ChoiceRepository choiceRepo;

    @Override
    public ISlide createSlide(IModule module, SlideForm slideForm) {
        ISlide slide = slideFactory.createSlide("Slide", module, slideForm);             
        slideRepo.save((Slide) slide);        
        return slide;
    }

    @Override
    public IBranchingPoint createBranchingPoint(IModule module, SlideForm slideForm) {
        ISlide branchingPoint = slideFactory.createSlide("BranchingPoint", module, slideForm);            
        bpointRepo.save((BranchingPoint) branchingPoint);        
        return (IBranchingPoint) branchingPoint;
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
    public IChoice getChoice(String choiceId) {
        Optional<Choice> choice = choiceRepo.findById(choiceId);
        if (choice.isPresent()) {
            return choice.get();
        }
        return null;
    }
    @Override
    public void updateSlide(Slide slide) {
        slideRepo.save((Slide) slide);
    }
}
