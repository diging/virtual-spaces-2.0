package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;

/*
*(non-javadoc)
*This interface represents a factory for creating external link displays.
*/
public interface IExternalLinkDisplayFactory {

    /*
     *(non-javadoc)
     *Creates an external link display based on the provided external link.
     *@param link the external link to create a display for
     *@return  edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay that 
     *represents the provided external link
     *
     */
    IExternalLinkDisplay createExternalLinkDisplay(IExternalLink link);
}
