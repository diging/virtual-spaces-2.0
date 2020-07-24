package edu.asu.diging.vspace.core.services.impl;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.ILink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.ILinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Transactional
public abstract class LinkManager<L extends ILink,T extends IVSpaceElement, U extends ILinkDisplay> implements ILinkManager{

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private IImageFactory imageFactory;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private IStorageEngine storage;

    public U createLink(String title, String id, float positionX, float positionY,
            int rotation, String linkedId, String linkLabel, DisplayType displayType, byte[] linkImage,
            String imageFilename) throws SpaceDoesNotExistException,ImageCouldNotBeStoredException, SpaceDoesNotExistException{

        L link = createLinkObject(title, id);
        T target = getTarget(linkedId);
        link.setName(linkLabel);
        link.setTarget(target);
        U displayLink = createDisplayLink(link);
        setDisplayProperties((ILinkDisplay) displayLink, positionX, positionY, rotation, displayType, linkImage, imageFilename);
        return updateLinkAndDisplay(link,displayLink);

    }

    public U updateLink(String title, String id, float positionX, float positionY,
            int rotation, String linkedId, String linkLabel, String linkId, String linkDisplayId,
            DisplayType displayType, byte[] linkImage, String imageFilename) throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException{

        validateSpace(id);

        L link =  getLink(linkId);
        T target = getTarget(linkedId);
        link.setName(title);
        link.setTarget((IVSpaceElement) target);
        U displayLink = getDisplayLink(linkDisplayId);
        setDisplayProperties((ILinkDisplay) displayLink,positionX,positionY,rotation, displayType, linkImage, imageFilename);
        return updateLinkAndDisplay(link,displayLink);
    }

    @Override
    public void deleteLink(String linkId){
        L link = getLink(linkId);
        removeFromLinkList(((ILink) link).getSpace(),link);
        deleteLinkDisplayRepo(link);
        deleteLinkRepo(link);
    }

    protected abstract void deleteLinkRepo(L link);

    protected abstract void deleteLinkDisplayRepo(L link);

    protected abstract void removeFromLinkList(ISpace space, L link);

    protected abstract U updateLinkAndDisplay(L link, U displayLink);

    protected abstract U getDisplayLink(String linkDisplayId) throws LinkDoesNotExistsException;

    protected abstract L getLink(String linkId);

    protected abstract L createLinkObject(String title, String id);

    protected abstract T getTarget(String linkedId);

    protected abstract U createDisplayLink(L link);

    protected void validateSpace(String id) throws SpaceDoesNotExistException{
        ISpace source = spaceManager.getSpace(id);
        if (source == null) {
            throw new SpaceDoesNotExistException();
        }
    }

    protected void setDisplayProperties(ILinkDisplay linkDisplay,float positionX,float positionY,int rotation, DisplayType displayType, byte[] linkImage, String imageFilename) throws ImageCouldNotBeStoredException {
        linkDisplay.setPositionX(positionX);
        linkDisplay.setPositionY(positionY);
        linkDisplay.setRotation(rotation);
        linkDisplay.setType(displayType != null ? displayType : DisplayType.ARROW);
        if (linkImage != null && linkImage.length > 0) {
            Tika tika = new Tika();
            String contentType = tika.detect(linkImage);
            IVSImage image = imageFactory.createImage(imageFilename, contentType);
            image = imageRepo.save((VSImage) image);
            String relativePath = null;
            try {
                relativePath = storage.storeFile(linkImage, imageFilename, image.getId());
            } catch (FileStorageException e) {
                throw new ImageCouldNotBeStoredException(e);
            }
            image.setParentPath(relativePath);
            imageRepo.save((VSImage) image);
            linkDisplay.setImage(image);
        }
    }
}
