package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ExternalLinkDisplayMode;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISlideExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;

public interface ISlideExternalLinkManager extends ISlideLinkManager<IExternalLinkSlide, ExternalLinkValue, ISlideExternalLinkDisplay>{
    
    ISlideExternalLinkDisplay createLink(String title, String id, float positionX, float positionY, int rotation, String linkedId,
            String linkLabel, DisplayType displayType, byte[] linkImage, String imageFilename,ExternalLinkDisplayMode howToOpen)
            throws SlideDoesNotExistException, ImageCouldNotBeStoredException, SlideDoesNotExistException;

    ISlideExternalLinkDisplay updateLink(String title, String id, float positionX, float positionY, int rotation, String linkedId,
            String linkLabel, String linkId, String linkDisplayId, DisplayType displayType, byte[] linkImage,
            String imageFilename,ExternalLinkDisplayMode howToOpen)
            throws SlideDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException;
    
    

}
