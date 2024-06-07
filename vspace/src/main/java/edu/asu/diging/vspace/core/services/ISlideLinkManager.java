package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.model.ILink;
import edu.asu.diging.vspace.core.model.ILinkSlide;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;

public interface ISlideLinkManager<L extends ILinkSlide<T>, T extends IVSpaceElement, U extends ILinkDisplay> {
    
    U createLink(String title, String id, String linkedId,
            String linkLabel, DisplayType displayType, byte[] linkImage, String imageFilename)
            throws SlideDoesNotExistException, ImageCouldNotBeStoredException, SlideDoesNotExistException;

    U updateLink(String title, String id, String linkedId,
            String linkLabel, String linkId, String linkDisplayId, DisplayType displayType, byte[] linkImage,
            String imageFilename)
            throws SlideDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException;

    List<U> getLinkDisplays(String slideId);

    void deleteLink(String linkId);

}
