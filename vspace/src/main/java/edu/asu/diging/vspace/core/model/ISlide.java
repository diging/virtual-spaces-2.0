package edu.asu.diging.vspace.core.model;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.TextBlock;

public interface ISlide extends IVSpaceElement {

    void setModule(IModule module);

    IModule getModule();

    void setContents(List<IContentBlock> contents);

    List<IContentBlock> getContents();
    
    IImageBlock getFirstImageBlock();
    
    TextBlock getFirstMatchedTextBlock(String searchTerm);
}
