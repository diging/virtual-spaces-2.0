package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface ISlide extends IVSpaceElement {

    void setModule(IModule module);

    IModule getModule();

    void setContents(List<IContentBlock> contents);

    List<IContentBlock> getContents();
    
    IImageBlock getFirstImageBlock();
    
    ITextBlock getFirstMatchedTextBlock(String searchTerm);
    
    List<IExternalLinkSlide> getExternalLinks();

    void setExternalLinks(List<IExternalLinkSlide> externalLinks);
        
    IVSImage getImage();

    void setImage(IVSImage image);
   
    
}
