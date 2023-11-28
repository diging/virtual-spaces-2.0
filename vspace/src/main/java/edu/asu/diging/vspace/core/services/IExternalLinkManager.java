package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ExternalLinkDisplayMode;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;

public interface IExternalLinkManager extends ILinkManager<IExternalLink, ExternalLinkValue, IExternalLinkDisplay>{

    IExternalLinkDisplay createLink(String title, String id, float positionX, float positionY, int rotation, String linkedId,
            String linkLabel, String desc, DisplayType displayType, byte[] linkImage, String imageFilename,ExternalLinkDisplayMode howToOpen, String imageId)
            throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, SpaceDoesNotExistException, ImageDoesNotExistException;

    IExternalLinkDisplay updateLink(String title, String id, float positionX, float positionY, int rotation, String linkedId,
            String linkLabel, String desc, String linkId, String linkDisplayId, DisplayType displayType, byte[] linkImage,
            String imageFilename, String existingImageId, ExternalLinkDisplayMode howToOpen)
            throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException;
}
