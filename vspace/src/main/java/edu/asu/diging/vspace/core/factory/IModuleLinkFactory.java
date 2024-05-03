package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;

/**
 * The edu.asu.diging.vspace.core.factory.IModuleLinkFactory interface defines a
 * factory for creating instances of the
 * edu.asu.diging.vspace.core.model.IModuleLink interface.
 * 
 * Implementations of this interface must provide a single method for creating
 * new module link objects.
 * 
 */
public interface IModuleLinkFactory {

    /**
     * Creates a new instance of the edu.asu.diging.vspace.core.model.IModuleLink
     * interface with the given title and space, and returns the new object.
     * 
     * @param Lang.String.title the name of the new module link object
     * @param space the edu.asu.diging.vspace.core.model.ISpace object to associate with the new module link object
     * @return the new edu.asu.diging.vspace.core.model.IModuleLink object
     */
    IModuleLink createModuleLink(String title, ISpace space);

}
