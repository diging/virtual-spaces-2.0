package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;

/**
 * The IModuleLinkDisplayFactory interface defines a factory for creating
 * instances of the edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay
 * interface. Implementations of this interface should provide a method for
 * creating an edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay
 * instance with the specified edu.asu.diging.vspace.core.model.IModuleLink set
 * as its link property.
 */
public interface IModuleLinkDisplayFactory {

    /**
     * Creates an instance of the IModuleLinkDisplay interface with the specified
     * edu.asu.diging.vspace.core.model.IModuleLink set as its link property.
     *
     * @param link the edu.asu.diging.vspace.core.model.IModuleLink to set as the
     *             link property of the created IModuleLinkDisplay instance
     * @return an instance of the
     *         edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay interface
     *         with the specified IModuleLink set as its link property
     */
    IModuleLinkDisplay createModuleLinkDisplay(IModuleLink link);

}
