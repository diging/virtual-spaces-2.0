package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISpace;

import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

/**
 * (non-javadoc) The ISpaceFactory interface defines a method for creating
 * instances of the edu.asu.diging.vspace.core.model.ISpace interface.
 */
public interface ISpaceFactory {

    /**
     * (non-javadoc) 
     * Creates an instance of the ISpace interface based on the given
     * SpaceForm object.
     *
     * @param form the edu.asu.diging.vspace.web.staff.forms.SpaceForm object that
     *             contains the space's name and description
     * @return an instance of the edu.asu.diging.vspace.core.model.ISpace interface
     *         with the specified name and description
     */
    ISpace createSpace(SpaceForm form);

}