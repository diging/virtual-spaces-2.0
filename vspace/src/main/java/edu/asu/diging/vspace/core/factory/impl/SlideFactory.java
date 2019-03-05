package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ISlideFactory;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

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
    public ISlide createSlide(SlideForm form) {
        ISlide slide = new Slide();
        slide.setName(form.getName());
        slide.setDescription(form.getDescription());
        return slide;
    }  
}


//@Override
//public ISlide createSlide(IModule module, String title, String description) {
//    ISlide slide = new Slide();
//    slide.setName(title);
//    slide.setDescription(description);
//    slide.setModule(module);
//    return slide;
//}