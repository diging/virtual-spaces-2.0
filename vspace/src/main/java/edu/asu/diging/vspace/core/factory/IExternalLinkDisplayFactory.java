package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;

public interface IExternalLinkDisplayFactory {

    IExternalLinkDisplay createExternalLinkDisplay(IExternalLink link);
}
