package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface ISequence extends IVSpaceElement {

    void setModule(IModule module);

    IModule getModule();
    
    List<ISlide> getSlides();

    void setSlides(List<ISlide> slides);
}
