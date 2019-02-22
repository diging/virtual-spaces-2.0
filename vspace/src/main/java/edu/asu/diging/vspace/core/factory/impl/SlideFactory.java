package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ISlideFactory;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Slide;

@Service
public class SlideFactory implements ISlideFactory {

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.ISlideFactory#createSlide(java.lang.
     * String, java.lang.String)
     */
    @Override
    public ISlide createSlide(IModule module, String title, String description) {
        ISlide slide = new Slide();
        slide.setName(title);
        slide.setDescription(description);
        slide.setModule(module);
        return slide;
    }
}
