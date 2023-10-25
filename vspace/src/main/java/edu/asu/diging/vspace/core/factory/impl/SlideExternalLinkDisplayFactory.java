package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ISlideExternalLinkDisplayFactory;
import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.display.ISlideExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SlideExternalLinkDisplay;

@Service
public class SlideExternalLinkDisplayFactory implements ISlideExternalLinkDisplayFactory {
    
    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.factory.impl.ISlideExternalLinkDisplayFactory#
     * createExternalLinkDisplay(edu.asu.diging.vspace.core.model.IExternalLinkSlide)
     */
    @Override
    public ISlideExternalLinkDisplay createExternalLinkDisplay(IExternalLinkSlide link) {
        ISlideExternalLinkDisplay display = new SlideExternalLinkDisplay();
        display.setExternalLink(link);
        return display;
    }

}
