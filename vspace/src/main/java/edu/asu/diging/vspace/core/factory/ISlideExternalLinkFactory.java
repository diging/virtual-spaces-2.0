package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.ISlide;

public interface ISlideExternalLinkFactory {
    
    IExternalLinkSlide createExternalLink(String title, ISlide slide);

}
