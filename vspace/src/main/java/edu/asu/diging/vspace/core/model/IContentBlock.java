package edu.asu.diging.vspace.core.model;

public interface IContentBlock extends ISlide{
    
    String getId();
    
    void setId(String id);
    
//    ISlide getSlide();
//    
//    void setSlide(ISlide slide); 
    
    String getTextblock();
    
    void setTextblock(String textblock);
    
    IVSImage getImageblock();
    
    void setImageblock(IVSImage imageblock);
}
