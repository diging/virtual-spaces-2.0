package edu.asu.diging.vspace.core.services;

import java.util.Set;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;

public interface IModuleLinkManager extends ILinkManager <IModuleLink, IModule, IModuleLinkDisplay> {

    Set<ISpace> findSpaceListFromModuleId(String moduleId);

}
