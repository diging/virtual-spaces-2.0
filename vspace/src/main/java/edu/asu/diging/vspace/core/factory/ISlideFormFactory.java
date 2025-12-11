package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

public interface ISlideFormFactory {
    
    SlideForm createNewSlideForm(ISlide slide, IExhibition startExhibition);

}
