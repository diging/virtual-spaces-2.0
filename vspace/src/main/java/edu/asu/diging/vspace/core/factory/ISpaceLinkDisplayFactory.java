package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;


/**
 * The ISpaceLinkDisplayFactory interface defines a factory for creating
 * instances of the edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay
 * interface. Implementations of this interface should provide a method for
 * creating an edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay
 * instance with the specified edu.asu.diging.vspace.core.model.ISpaceLink set
 * as its link property.
 */
public interface ISpaceLinkDisplayFactory {
    
    /**
     * Creates an instance of the ISpaceLinkDisplay interface with the specified
     * edu.asu.diging.vspace.core.model.ISpaceLink set as its link property.
     *
     * @param link the edu.asu.diging.vspace.core.model.ISpaceLink to set as the
     *             link property of the created ISpaceLinkDisplay instance
     * @return an instance of the
     *         edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay interface
     *         with the specified ISpaceLink set as its link property
     */
    ISpaceLinkDisplay createSpaceLinkDisplay(ISpaceLink link);

}