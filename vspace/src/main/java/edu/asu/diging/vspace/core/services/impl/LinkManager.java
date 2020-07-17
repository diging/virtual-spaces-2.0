package edu.asu.diging.vspace.core.services.impl;

import java.util.Optional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.ILLink;
import edu.asu.diging.vspace.core.model.ILink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ITVSpaceElement;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.ILinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

public abstract class LinkManager implements ILinkManager{

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private IImageFactory imageFactory;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private IStorageEngine storage;

    public ILinkDisplay createLink(String title, String id, float positionX, float positionY,
            int rotation, String linkedId, String linkLabel, DisplayType displayType, byte[] linkImage,
            String imageFilename) throws SpaceDoesNotExistException,ImageCouldNotBeStoredException, SpaceDoesNotExistException{

        IVSpaceElement target = getTarget(linkedId);
        ILink link = (ILink) createLinkObject(title, id, target, linkLabel); 
        return saveDisplayLinkRepo(link,positionX, positionY, rotation, displayType, linkImage, imageFilename);
    }

    protected abstract ILinkDisplay saveDisplayLinkRepo(ILink link, float positionX, float positionY, int rotation,
            DisplayType displayType, byte[] linkImage, String imageFilename)  throws ImageCouldNotBeStoredException;

    public ILinkDisplay updateLink(String title, String id, float positionX, float positionY,
            int rotation, String linkedId, String linkLabel, String linkId, String linkDisplayId,
            DisplayType displayType, byte[] linkImage, String imageFilename) throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException{

        spaceValidation(id);

        ILink link =  getLink(linkId);
        ILinkDisplay displayLink = getDisplayLink(linkDisplayId);


        IVSpaceElement target = (IVSpaceElement) getTarget(linkedId);
        link.setName(title);
        setTarget(link,target);

        setDisplayProperties(displayLink,positionX,positionY,rotation, displayType, linkImage, imageFilename);

        return updateLinkAndDisplay(link,displayLink);
    }
    
    @Override
    public void deleteLink(String linkId){
        
        ILink link = getLink(linkId);
        removeFromLinkList(link.getSpace(),link);
        deleteLinkDisplayRepo(link);
        deleteLinkRepo(link);
    }

    protected abstract void deleteLinkRepo(ILink link);

    protected abstract void deleteLinkDisplayRepo(ILink link);

    protected abstract void removeFromLinkList(ISpace space, ILink link);

    protected abstract ILinkDisplay updateLinkAndDisplay(ILink link, ILinkDisplay displayLink);

    protected abstract void setTarget(ILink link, IVSpaceElement target);

    protected abstract ILinkDisplay getDisplayLink(String linkDisplayId) throws LinkDoesNotExistsException;

    protected abstract ILink getLink(String linkId);

    protected abstract ILink createLinkObject(String title, String id, IVSpaceElement target, String linkLabel);

    protected abstract IVSpaceElement getTarget(String linkedId);

    protected void spaceValidation(String id) throws SpaceDoesNotExistException{
        ISpace source = spaceManager.getSpace(id);
        if (source == null) {
            throw new SpaceDoesNotExistException();
        }
    }

    protected boolean linksValidation(Optional link){
        return link.isPresent();
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
