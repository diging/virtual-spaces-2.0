package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.display.SlideType;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

/*
* 
* The {@code ISlideFactory} interface defines a factory for creating a list of slides.
* It provides a single method for creating a instance of edu.asu.diging.vspace.core.model.ISlide interface.
* 
*/
public interface ISlideFactory {

    /**
     * 
     * Creates an instance of the ISlide interface based on the given
     * SlideForm object.
     *
     * @param form the edu.asu.diging.vspace.web.staff.forms.SlideForm object that
     *             contains the Slides's name and description
     * @param module the edu.asu.diging.vspace.core.model.IModule to set as the
     *             module property of the created ISlide instance
     * @param type the edu.asu.diging.vspace.core.model.display.SlideType 
     *             object to set the slide type property of the created ISlide instance
     * @return an instance of the edu.asu.diging.vspace.core.model.ISlide interface
     *         with the specified name and description
     */
    ISlide createSlide(IModule module, SlideForm form, SlideType type);

}
