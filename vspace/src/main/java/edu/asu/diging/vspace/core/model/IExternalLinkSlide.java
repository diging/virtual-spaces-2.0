package edu.asu.diging.vspace.core.model;

import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;

public interface IExternalLinkSlide extends ILinkSlide<ExternalLinkValue>{
    
    ISlide getSlide();

    void setSlide(ISlide slide);

    String getExternalLink();

    void setExternalLink(String externalLink);
}
