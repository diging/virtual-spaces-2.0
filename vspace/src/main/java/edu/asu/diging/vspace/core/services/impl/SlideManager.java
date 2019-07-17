package edu.asu.diging.vspace.core.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.BranchingPointRepository;
import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.factory.impl.SlideFactory;
import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

@Service
public class SlideManager implements ISlideManager {

    @Autowired
    private BranchingPointRepository bpointRepo;
    
    @Autowired
    private SlideRepository slideRepo;

    @Autowired
    private SlideFactory slideFactory;

    @Override
    public ISlide createSlide(IModule module, SlideForm slideForm) {
        System.out.println("---------manager---------");
        ISlide slide = slideFactory.createSlide(module, slideForm);             
        slideRepo.save((Slide) slide);        
        return slide;
    }

    @Override
    public IBranchingPoint createBranchingPoint(IModule module, SlideForm slideForm) {
        IBranchingPoint branchingPoint = slideFactory.createBranchingPoint(module, slideForm);        
        bpointRepo.save((BranchingPoint) branchingPoint);        
        return branchingPoint;
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
    public void updateSlide(Slide slide) {
        slideRepo.save((Slide) slide);
    }

}
