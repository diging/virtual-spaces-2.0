package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface ISpaceManager {

    CreationReturnValue storeSpace(ISpace space, byte[] image, String filename);

    ISpace getSpace(String id);

    ISpaceLinkDisplay createSpaceLink(String title, ISpace source, float positionX, float positionY, int rotation,
            String linkedSpaceId, String spaceLinkLabel, DisplayType displayType) throws SpaceDoesNotExistException;

    IExternalLinkDisplay createExternalLink(String title, ISpace space, float positionX, float positionY, String externalLink)
            throws SpaceDoesNotExistException;

    ISpace getFullyLoadedSpace(String id);

    List<ISpaceLinkDisplay> getSpaceLinkDisplays(String spaceId);

    List<IExternalLinkDisplay> getExternalLinkDisplays(String spaceId);

    List<ISpace> getAllSpaces();

}