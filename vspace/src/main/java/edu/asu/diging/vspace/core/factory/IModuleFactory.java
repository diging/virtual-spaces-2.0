package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.web.staff.forms.ModuleForm;

/**
 * (non-javadoc) The IModuleFactory interface defines a method for creating
 * instances of the edu.asu.diging.vspace.core.model.IModule interface.
 */
public interface IModuleFactory {
    /**
     * (non-javadoc) 
     * Creates an instance of the IModule interface based on the given
     * ModuleForm object.
     *
     * @param form the edu.asu.diging.vspace.web.staff.forms.ModuleForm object that
     *             contains the module's name and description
     * @return an instance of the edu.asu.diging.vspace.core.model.IModule interface
     *         with the specified name and description
     */

    IModule createModule(ModuleForm form);

}