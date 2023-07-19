package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.display.SlideType;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

/*
* (non-Javadoc)
* The {@code ISlideFactory} interface defines a factory for creating a list of slides.
* It provides a single method for creating a object of {@link ISlide} from given specified parameters.
* 
*/
public interface ISlideFactory {

    /*
    * Creates a slide based on the specified module, form, and type.
    * @param module The module associated with the slide.
    * @param form The form of the slide.
    * @param type The type of the slide.
    * @return edu.asu.diging.vspace.core.model.ISlide object with the specified parameters
    */
    ISlide createSlide(IModule module, SlideForm form, SlideType type);

}
