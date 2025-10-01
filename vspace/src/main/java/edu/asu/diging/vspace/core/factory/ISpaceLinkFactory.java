package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;


/**
 * (non-javadoc)
 * The ISpaceLinkFactory interface defines a factory for creating instances of the edu.asu.diging.vspace.core.model.ISpaceLink
 * interface. 
 * Implementations of this interface should provide an implementation for the
 * method to create an space link, which takes a title and source
 * as input parameters and returns an instance of the IVSImage interface.
 * 
 */
public interface ISpaceLinkFactory {
    
    /**
     * Creates a new space link with the given title and source space.
     *
     * @param title The title of the space link.
     * @param source The source space for the link.
     * @return The created space link instance of the edu.asu.diging.vspace.core.model.ISpaceLink
     */
    ISpaceLink createSpaceLink(String title, ISpace source);

}