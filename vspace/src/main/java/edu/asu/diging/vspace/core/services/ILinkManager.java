package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;

public interface ILinkManager {

    ILinkDisplay createLinkTemplate(String title, String id, float positionX, float positionY,
            int rotation, String linkedId, String linkLabel, DisplayType displayType, byte[] linkImage,
            String imageFilename) throws SpaceDoesNotExistException,ImageCouldNotBeStoredException, SpaceDoesNotExistException;

    ILinkDisplay updateLinkTemplate(String title, String id, float positionX, float positionY,
            int rotation, String linkedId, String linkLabel, String linkId, String linkDisplayId,
            DisplayType displayType, byte[] linkImage, String imageFilename) throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException;
    
    List<ILinkDisplay> getLinkDisplays(String spaceId);

    void deleteLink(String linkId);

}
