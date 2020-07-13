package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;

public interface IModuleLinkManager extends ILinkManager{

    List<ModuleLinkDisplay> getLinkDisplays(String spaceId);

    void deleteLink(String linkId);

}
