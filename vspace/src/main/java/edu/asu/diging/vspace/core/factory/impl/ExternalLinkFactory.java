package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IExternalLinkFactory;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;

@Service
public class ExternalLinkFactory implements IExternalLinkFactory {

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.factory.impl.IExternalLinkFactory#
     * createExternalLink(java.lang.String, edu.asu.diging.vspace.core.model.ISpace)
     */
    @Override
    public IExternalLink createExternalLink(String title, ISpace space, String externalLink) {
        IExternalLink link = new ExternalLink();
        link.setName(title);
        link.setSpace(space);
        link.setExternalLink(externalLink);
        return link;
    }
}
