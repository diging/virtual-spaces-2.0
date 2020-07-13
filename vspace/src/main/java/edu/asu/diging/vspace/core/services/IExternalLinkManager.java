package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;

public interface IExternalLinkManager extends ILinkManager{

    public List<ExternalLinkDisplay> getLinkDisplays(String spaceId);

    public void deleteLink(String linkId);

}
