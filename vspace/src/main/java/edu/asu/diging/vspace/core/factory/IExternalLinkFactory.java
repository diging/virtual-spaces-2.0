package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;

public interface IExternalLinkFactory {

    IExternalLink createExternalLink(String title, ISpace space);
    
}
