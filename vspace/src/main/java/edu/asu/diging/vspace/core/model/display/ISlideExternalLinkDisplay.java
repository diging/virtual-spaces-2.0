package edu.asu.diging.vspace.core.model.display;

import edu.asu.diging.vspace.core.model.IExternalLinkSlide;

public interface ISlideExternalLinkDisplay extends ILinkDisplay {
    
    String getId();

    void setId(String id);

    void setExternalLink(IExternalLinkSlide link);

    IExternalLinkSlide getExternalLink();

}
