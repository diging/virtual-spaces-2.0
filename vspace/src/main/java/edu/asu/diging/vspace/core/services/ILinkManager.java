package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.exception.SpaceLinkDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;

public interface ILinkManager {

    ISpaceLinkDisplay createSpaceLink(String title, ISpace source, float positionX, float positionY, int rotation,
            String linkedSpaceId, String spaceLinkLabel, DisplayType displayType, byte[] linkImage, String imageFilename) throws ImageCouldNotBeStoredException, SpaceDoesNotExistException;

    List<ISpaceLinkDisplay> getSpaceLinkDisplays(String spaceId);

    List<IExternalLinkDisplay> getExternalLinkDisplays(String spaceId);

    IExternalLinkDisplay createExternalLink(String title, ISpace source, float positionX, float positionY,
            String externalLink) throws SpaceDoesNotExistException;

    void deleteSpaceLink(String linkId);
    
    void deleteSpaceLinkBySource(String sourceId) throws SpaceLinkDoesNotExistException;

}