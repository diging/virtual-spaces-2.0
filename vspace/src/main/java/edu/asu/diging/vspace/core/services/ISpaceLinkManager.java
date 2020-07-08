package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;

public interface ISpaceLinkManager {
    public ISpaceLinkDisplay createLink(String title, ISpace source, float positionX, float positionY, int rotation,
            String linkedSpaceId, String spaceLinkLabel, DisplayType displayType, byte[] linkImage, String imageFilename)
                    throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, SpaceDoesNotExistException;
    public List<SpaceLinkDisplay> getLinkDisplays(String spaceId);
    public ISpaceLinkDisplay editLink(String title, String id, float positionX, float positionY, int rotation,
            String linkedSpaceId, String spaceLinkLabel, String spaceLinkIdValueEdit, String spaceLinkDisplayId, DisplayType displayType,
            byte[] linkImage, String imageFilename)
                    throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException;
    public void deleteLink(String linkId);
}
