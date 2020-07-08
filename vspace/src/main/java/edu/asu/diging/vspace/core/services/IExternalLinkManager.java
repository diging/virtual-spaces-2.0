package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;

public interface IExternalLinkManager {
    public IExternalLinkDisplay createLink(String title, ISpace source, float positionX, float positionY, int rotation,
            String externalLink, String externalLabel, DisplayType displayType, byte[] linkImage, String imageFilename)
                    throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, SpaceDoesNotExistException;

    public List<ExternalLinkDisplay> getLinkDisplays(String spaceId);

    public void deleteLink(String linkId);

    public IExternalLinkDisplay editLink(String title, String id, float positionX, float positionY, int rotation,
            String externalLink, String externalLinkLabel, String externalLinkIdValueEdit, String externalLinkDisplayId, DisplayType displayType,
            byte[] linkImage, String imageFilename)
                    throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException;
}
