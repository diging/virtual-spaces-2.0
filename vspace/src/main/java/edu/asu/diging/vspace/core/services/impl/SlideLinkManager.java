package edu.asu.diging.vspace.core.services.impl;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.ILinkSlide;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.ISlideLinkManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Transactional
public abstract class SlideLinkManager<L extends ILinkSlide<T>, T extends IVSpaceElement, U extends ILinkDisplay>
implements ISlideLinkManager<L, T, U> {
    
    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private IImageFactory imageFactory;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private IStorageEngine storage;
    
    @Override
    public U createLink(String title, String id, String linkedId,
            String linkLabel, DisplayType displayType, byte[] linkImage, String imageFilename)
            throws SlideDoesNotExistException, ImageCouldNotBeStoredException, SlideDoesNotExistException {

        L link = createLinkObject(title, id);
        System.out.println("LINK ID 3: " + id);
        System.out.println("LINK ID 4: " + link.getId());
        T target = getTarget(linkedId);
        link.setName(linkLabel);
        link.setTarget(target);
        System.out.println("LINK TARGET: " + link.getTarget());
        U displayLink = createDisplayLink(link);
        setDisplayProperties(displayLink, id, displayType, linkImage, imageFilename);
        return updateLinkAndDisplay(link, displayLink);

    }
    
    @Override
    public U updateLink(String title, String id, String linkedId,
            String linkLabel, String linkId, String linkDisplayId, DisplayType displayType, byte[] linkImage,
            String imageFilename)
            throws SlideDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException {

        validateSlide(id);

        L link = getLink(linkId);
        T target = getTarget(linkedId);
        link.setName(title);
        link.setTarget(target);
        U displayLink = getDisplayLink(linkDisplayId);
        setDisplayProperties(displayLink, id, displayType, linkImage, imageFilename);
        return updateLinkAndDisplay(link, displayLink);
    }
    
    @Override
    public void deleteLink(String linkId) {
        L link = getLink(linkId);
        removeFromLinkList(link.getSlide(), link);
        deleteLinkDisplayRepo(link);
        deleteLinkRepo(link);
    }

    protected abstract void deleteLinkRepo(L link);

    protected abstract void deleteLinkDisplayRepo(L link);

    protected abstract void removeFromLinkList(ISlide slide, L link);

    protected abstract U updateLinkAndDisplay(L link, U displayLink);

    protected abstract U getDisplayLink(String linkDisplayId) throws LinkDoesNotExistsException;

    protected abstract L getLink(String linkId);

    protected abstract L createLinkObject(String title, String id);
   
    protected abstract T getTarget(String linkedId);

    protected abstract U createDisplayLink(L link);

    protected void validateSlide(String id) throws SlideDoesNotExistException {
        ISlide source = slideManager.getSlide(id);
        if (source == null) {
            throw new SlideDoesNotExistException();
        }
    }
    
    protected void setDisplayProperties(ILinkDisplay linkDisplay, String id, DisplayType displayType, byte[] linkImage, String imageFilename)
            throws ImageCouldNotBeStoredException {
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
