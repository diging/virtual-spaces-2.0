package edu.asu.diging.vspace.core.services;

import java.util.Set;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;


public interface IModuleLinkManager extends ILinkManager<IModuleLink, IModule, IModuleLinkDisplay> {
    
    /**
     * Finds a list of spaces based on the given module ID.
     *
     * @param moduleId the ID of the module to search for spaces
     * @return a set of ISpace objects associated with the given module ID
     */
    Set<ISpace> findSpaceListFromModuleId(String moduleId);

}
