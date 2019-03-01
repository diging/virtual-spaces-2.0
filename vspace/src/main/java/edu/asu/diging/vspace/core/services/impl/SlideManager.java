package edu.asu.diging.vspace.core.services.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Transactional
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
    public ISlide storeModule(ISlide slide) {
        return slideRepo.save((Slide) slide);
    }

}
