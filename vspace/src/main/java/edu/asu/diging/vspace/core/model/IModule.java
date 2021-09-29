package edu.asu.diging.vspace.core.model;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.ModuleStatus;

public interface IModule extends IVSpaceElement {

    List<ISlide> getSlides();

    void setSlides(List<ISlide> slides);

    List<ISequence> getSequences();

    void setSequences(List<ISequence> sequence);

    ISequence getStartSequence();

    void setStartSequence(ISequence startSequence);
    
    void setModuleStatus(ModuleStatus status);
    ModuleStatus getModuleStatus();
    
}