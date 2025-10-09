package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;

/**
 * The ISpaceDisplayFactory interface defines a factory for creating
 * instances of the edu.asu.diging.vspace.core.model.display.ISpaceDisplay
 * interface. Implementations of this interface should provide a method for
 * creating an edu.asu.diging.vspace.core.model.display.ISpaceDisplay
 * instance.
 */
public interface ISpaceDisplayFactory {

    /**
     * Creates an instance of the ISpaceDisplay interface
     *
     * @return an instance of the
     *         edu.asu.diging.vspace.core.model.display.ISpaceDisplay interface
     */
    ISpaceDisplay createSpaceDisplay();

}