package edu.asu.diging.vspace.core.model;

public interface IExternalLinkSlide{
    
    String getId();
    
    void setId(String id);
    
    ISlide getSlide();

    void setSlide(ISlide slide);

    String getExternalLink();

    void setExternalLink(String externalLink);
    
    String getLabel();
    
    void setLabel(String label);
}
