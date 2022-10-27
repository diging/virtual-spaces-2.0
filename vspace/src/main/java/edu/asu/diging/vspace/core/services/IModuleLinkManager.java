package edu.asu.diging.vspace.core.services;

import java.util.HashSet;
import java.util.List;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;

public interface IModuleLinkManager extends ILinkManager <IModuleLink, IModule, IModuleLinkDisplay> {

    HashSet <ISpace> findModuleLinksFromModuleId(String moduleId);

}
