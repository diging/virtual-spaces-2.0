package edu.asu.diging.vspace.core.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Service
public class SlideManager implements ISlideManager {
    
    @Autowired
    private SlideRepository slideRepo;
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.ISlideManager#storeSlide(edu.asu.
     * diging.vspace.core.model.ISlide)
     */
    @Override
    public ISlide storeSlide(ISlide slide) {
        return slideRepo.save((Slide) slide);
    }
    
    @Override
    public ISlide getSlide(String slideId) {
        Optional<Slide> slide = slideRepo.findById(slideId);
        if (slide.isPresent()) {
            return slide.get();
        }
        return null;
    }

}
