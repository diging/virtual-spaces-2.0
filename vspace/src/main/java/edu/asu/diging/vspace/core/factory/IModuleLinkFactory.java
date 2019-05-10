package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;

public interface IModuleLinkFactory {

    IModuleLink createModuleLink(String title, ISpace space);

}
