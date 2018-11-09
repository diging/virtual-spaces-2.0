package edu.asu.diging.vspace.core.factory.impl;

import edu.asu.diging.vspace.core.factory.IExternalLinkDisplayFactory;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;

public class ExternalLinkDisplayFactory implements IExternalLinkDisplayFactory {

    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.factory.impl.IExternalLinkDisplayFactory#createExternalLinkDisplay(edu.asu.diging.vspace.core.model.IExternalLink)
     */
    @Override
    public IExternalLinkDisplay createExternalLinkDisplay(IExternalLink link) {
        IExternalLinkDisplay display = new ExternalLinkDisplay();
        display.setExternalLink(link);
        return display;
    }
}
