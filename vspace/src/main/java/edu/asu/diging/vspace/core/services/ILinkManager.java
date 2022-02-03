package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ILink;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;

public interface ILinkManager<L extends ILink<T>,T extends IVSpaceElement, U extends ILinkDisplay> {

    U createLink(String title, String id, float positionX, float positionY,
            int rotation, String linkedId, String linkLabel, String linkDesc, DisplayType displayType, byte[] linkImage,
            String imageFilename, String existingImageId) throws SpaceDoesNotExistException,ImageCouldNotBeStoredException, SpaceDoesNotExistException, ImageDoesNotExistException;

    U updateLink(String title, String id, float positionX, float positionY,
            int rotation, String linkedId, String linkLabel, String linkDesc, String linkId, String linkDisplayId,
            DisplayType displayType, byte[] linkImage, String imageFilename, String existingImageId) throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException, ImageDoesNotExistException;

    List<U> getLinkDisplays(String spaceId);


    void deleteLink(String linkId);
}
