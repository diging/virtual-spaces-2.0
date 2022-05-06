package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ExternalLinkDisplayMode;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;

public interface IExternalLinkManager extends ILinkManager<IExternalLink, ExternalLinkValue, IExternalLinkDisplay>{

    IExternalLinkDisplay createLink(String title, String id, float positionX, float positionY, int rotation, String linkedId,
            String linkLabel, DisplayType displayType, byte[] linkImage, String imageFilename,ExternalLinkDisplayMode howToOpen)
            throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, SpaceDoesNotExistException;

    IExternalLinkDisplay updateLink(String title, String id, float positionX, float positionY, int rotation, String linkedId,
            String linkLabel, String linkId, String linkDisplayId, DisplayType displayType, byte[] linkImage,
            String imageFilename,ExternalLinkDisplayMode howToOpen)
            throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException;
}
