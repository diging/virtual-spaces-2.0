package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;

public interface IModuleLinkManager {

    public IModuleLinkDisplay createLink(String title, ISpace source, float positionX, float positionY, int rotation,
            String linkedModuleId, String moduleLinkLabel, DisplayType displayType, byte[] linkImage, String imageFilename)
                    throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, SpaceDoesNotExistException;

    public List<ModuleLinkDisplay> getLinkDisplays(String spaceId);

    public void deleteLink(String linkId);

    public IModuleLinkDisplay editLink(String title, String id, float positionX, float positionY, int rotation,
            String linkedModuleId, String moduleLinkLabel, String linkId, String moduleLinkDisplayId, DisplayType displayType,
            byte[] linkImage, String imageFilename)
                    throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException;
}
