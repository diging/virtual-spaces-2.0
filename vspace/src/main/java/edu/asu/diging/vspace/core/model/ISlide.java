package edu.asu.diging.vspace.core.model;

import java.util.List;
import java.util.Set;

public interface ISlide extends IVSpaceElement {
    
    void setModule(IModule module);

    IModule getModule();

    void setContents(Set<IContentBlock> contents);

    Set<IContentBlock> getContents();


}
