package edu.asu.diging.vspace.core.services.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.ILink;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.ISpaceManager;

public abstract class LinkManager{

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private IImageFactory imageFactory;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private IStorageEngine storage;

    public ILinkDisplay createLinkTemplate(String title, String id, float positionX, float positionY,
            int rotation, String linkedId, String linkinkLabel, DisplayType displayType, byte[] linkImage,
            String imageFilename) throws SpaceDoesNotExistException,ImageCouldNotBeStoredException, SpaceDoesNotExistException{
        
        IVSpaceElement target = getTarget(linkedId);
        ILink link = (ILink) createLinkObject(title, id, target); 
        ILinkDisplay display = setProperties(link, positionX, positionY, rotation, displayType);
        return display;
        
    }
    
    public void deleteLinkTemplate(String linkId) {
        deleteLink(linkId);
    }
    
    public List getLinkDisplaysTemplate(String spaceId) {
        return getLinkDisplays(spaceId);
    }
    
    public ILinkDisplay updateLinkTemplate(String title, String id, float positionX, float positionY,
            int rotation, String linkedId, String linkLabel, String linkId, String linkDisplayId,
            DisplayType displayType, byte[] linkImage, String imageFilename) throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException{
        return updateLink(title, id, positionX, positionY, rotation, linkedId, linkLabel, linkId, linkDisplayId, displayType, linkImage, imageFilename);
    }
    
    protected abstract ILinkDisplay setProperties(ILink link, float positionX, float positionY,
            int rotation, DisplayType displayType);

    protected abstract ILink createLinkObject(String title, String id, IVSpaceElement target);

    protected abstract IVSpaceElement getTarget(String linkedId);
    
    
    

    public abstract ILinkDisplay createLink(String title, String id, float positionX, float positionY,
            int rotation, String linkedId, String linkinkLabel, DisplayType displayType, byte[] linkImage,
            String imageFilename)
                    throws SpaceDoesNotExistException,ImageCouldNotBeStoredException, SpaceDoesNotExistException;

    public abstract List getLinkDisplays(String spaceId);

    public abstract ILinkDisplay updateLink(String title, String id, float positionX, float positionY,
            int rotation, String linkedId, String linkLabel, String linkId, String linkDisplayId,
            DisplayType displayType, byte[] linkImage, String imageFilename) throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException;

    public abstract void deleteLink(String linkId);

    public void spaceValidation(String id) throws SpaceDoesNotExistException{
        ISpace source = spaceManager.getSpace(id);
        if (source == null) {
            throw new SpaceDoesNotExistException();
        }
    }

    public void linksValidation(Optional linkOptional,
            Optional dislpayLinkOptional) throws LinkDoesNotExistsException {
        if(!linkOptional.isPresent()) {
            throw new LinkDoesNotExistsException("Link Does Not Exists");
        }

        if(!dislpayLinkOptional.isPresent()) {
            throw new LinkDoesNotExistsException("Link Does Not Exists");
        }
    }

    public void populateDisplay(ILinkDisplay linkDisplay,float positionX,float positionY,int rotation, DisplayType displayType, byte[] linkImage, String imageFilename) throws ImageCouldNotBeStoredException {
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
