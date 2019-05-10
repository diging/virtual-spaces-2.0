package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;

public interface IModuleLinkDisplayFactory {

    IModuleLinkDisplay createModuleLinkDisplay(IModuleLink link);

}
