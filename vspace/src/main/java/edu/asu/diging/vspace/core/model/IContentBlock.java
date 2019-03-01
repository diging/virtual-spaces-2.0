package edu.asu.diging.vspace.core.model;

public interface IContentBlock extends ISlide{

    void setTextblock(String textblock);
    
    String getTextblock();
    
    void setImageblock(IVSImage imageblock);
    
    IVSImage getImageblock();
}
