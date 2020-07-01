package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;

public interface ILinkManager {

    ISpaceLinkDisplay createSpaceLink(String title, ISpace source, float positionX, float positionY, int rotation,
            String linkedSpaceId, String spaceLinkLabel, DisplayType displayType, byte[] linkImage, String imageFilename) throws ImageCouldNotBeStoredException, SpaceDoesNotExistException;

    List<ISpaceLinkDisplay> getSpaceLinkDisplays(String spaceId);

    List<IExternalLinkDisplay> getExternalLinkDisplays(String spaceId);

    IExternalLinkDisplay createExternalLink(String title, ISpace source, float positionX, float positionY,
            String externalLink, DisplayType displayType, byte[] linkImage, String imageFilename) throws ImageCouldNotBeStoredException, SpaceDoesNotExistException;

    void deleteSpaceLink(String linkId);

    void deleteModuleLink(String linkId);

    IModuleLinkDisplay createModuleLink(String title, ISpace source, float positionX, float positionY, int rotation,
            String linkedModuleId, String moduleLinkLabel, DisplayType displayType) throws SpaceDoesNotExistException;

    IModuleLinkDisplay editModuleLink(String title, String id, float positionX, float positionY, int rotation,
            String linkedModuleId, String moduleLinkLabel, String linkId, String moduleLinkDisplayId, DisplayType displayType) throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, LinkDoesNotExistsException;

    ISpaceLinkDisplay editSpaceLink(String title, String id, float positionX, float positionY,
            int rotation, String linkedSpaceId, String spaceLinkLabel, String spaceLinkIdValueEdit, String spaceLinkDisplayId, DisplayType displayType, byte[] linkImage, String imageFilename) throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, LinkDoesNotExistsException;

    IExternalLinkDisplay editExternalLink(String title, String id, float positionX, float positionY,
            String externalLink, String externalLinkIdValueEdit, String externalLinkDisplayId, DisplayType displayType, byte[] linkImage, String imageFilename) throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, LinkDoesNotExistsException;

    List<IModuleLinkDisplay> getModuleLinkDisplays(String spaceId);

    void deleteExternalLink(String linkId);

}