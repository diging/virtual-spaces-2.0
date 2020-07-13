package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;

public interface ISpaceLinkManager extends ILinkManager{

    List<SpaceLinkDisplay> getLinkDisplays(String spaceId);

    void deleteLink(String linkId);
}
