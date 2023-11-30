package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ISlideExternalLinkFactory;
import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkSlide;

@Service
public class SlideExternalLinkFactory implements ISlideExternalLinkFactory{
    
    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.factory.impl.ISlideExternalLinkFactory#
     * createExternalLink(java.lang.String, edu.asu.diging.vspace.core.model.ISlide)
     */
    @Override
    public IExternalLinkSlide createExternalLink(String title, ISlide slide) {
        IExternalLinkSlide link = new ExternalLinkSlide();
        link.setName(title);
        link.setSlide(slide);
        return link;
    }

}