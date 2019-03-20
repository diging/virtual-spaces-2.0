package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ExternalLinkDisplayRepository;
import edu.asu.diging.vspace.core.data.ExternalLinkRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.SpaceLinkRepository;
import edu.asu.diging.vspace.core.data.display.SpaceLinkDisplayRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IExternalLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IExternalLinkFactory;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ISpaceLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISpaceLinkFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.ILinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Transactional
@Service
public class LinkManager implements ILinkManager {
    
    @Autowired
    private ISpaceManager spaceManager;
    
    @Autowired
    private ISpaceLinkFactory spaceLinkFactory;

    @Autowired
    private ISpaceLinkDisplayFactory spaceLinkDisplayFactory;

    @Autowired
    private SpaceLinkRepository spaceLinkRepo;

    @Autowired
    private SpaceLinkDisplayRepository spaceLinkDisplayRepo;

    @Autowired
    private ExternalLinkRepository externalLinkRepo;

    @Autowired
    private ExternalLinkDisplayRepository externalLinkDisplayRepo;

    @Autowired
    private IExternalLinkFactory externalLinkFactory;

    @Autowired
    private IExternalLinkDisplayFactory externalLinkDisplayFactory;
    
    @Autowired
    private IImageFactory imageFactory;
    
    @Autowired
    private ImageRepository imageRepo;
    
    @Autowired
    private IStorageEngine storage;
    
    /*
     * ==== Space links ====
     */
    
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.services.impl.ILinkManager#createSpaceLink(java.lang.String, edu.asu.diging.vspace.core.model.ISpace, float, float, int, java.lang.String, java.lang.String, edu.asu.diging.vspace.core.model.display.DisplayType)
     */
    @Override
    public ISpaceLinkDisplay createSpaceLink(String title, ISpace source, float positionX, float positionY,
            int rotation, String linkedSpaceId, String spaceLinkLabel, DisplayType displayType, byte[] linkImage, String imageFilename) throws ImageCouldNotBeStoredException, SpaceDoesNotExistException {
        // we need this to fully load the space
        source = spaceManager.getSpace(source.getId());
        if (source == null) {
            throw new SpaceDoesNotExistException();
        }
        
        ISpace target = spaceManager.getSpace(linkedSpaceId);
        ISpaceLink link = spaceLinkFactory.createSpaceLink(title, source);
        link.setTargetSpace(target);
        link.setName(spaceLinkLabel);
        spaceLinkRepo.save((SpaceLink) link);

        ISpaceLinkDisplay display = spaceLinkDisplayFactory.createSpaceLinkDisplay(link);
        display.setPositionX(positionX);
        display.setPositionY(positionY);
        display.setRotation(rotation);
        display.setType(displayType != null ? displayType : DisplayType.ARROW);
        
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
            imageRepo.save((VSImage)image);
            
            display.setImage(image);
        }
        
        spaceLinkDisplayRepo.save((SpaceLinkDisplay) display);
        return display;
    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.services.impl.ILinkManager#getSpaceLinkDisplays(java.lang.String)
     */
    @Override
    public List<ISpaceLinkDisplay> getSpaceLinkDisplays(String spaceId) {
        return new ArrayList<>(spaceLinkDisplayRepo.findSpaceLinkDisplaysForSpace(spaceId));
    }
    
    /*
     *  ===== External links ===== 
     */
    
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.services.impl.ILinkManager#getExternalLinkDisplays(java.lang.String)
     */
    @Override
    public List<IExternalLinkDisplay> getExternalLinkDisplays(String spaceId) {
        return new ArrayList<>(externalLinkDisplayRepo.findExternalLinkDisplaysForSpace(spaceId));
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.services.impl.ILinkManager#createExternalLink(java.lang.String, edu.asu.diging.vspace.core.model.ISpace, float, float, java.lang.String)
     */
    @Override
    public IExternalLinkDisplay createExternalLink(String title, ISpace source, float positionX, float positionY, String externalLink) throws SpaceDoesNotExistException {
        // we need this to fully load the space
        source = spaceManager.getSpace(source.getId());
        if(source == null) {
            throw new SpaceDoesNotExistException();
        }
        IExternalLink link = externalLinkFactory.createExternalLink(title, source, externalLink);
        externalLinkRepo.save((ExternalLink) link);

        IExternalLinkDisplay display = externalLinkDisplayFactory.createExternalLinkDisplay(link);
        display.setPositionX(positionX);
        display.setPositionY(positionY);
        display.setName(title);
        externalLinkDisplayRepo.save((ExternalLinkDisplay) display);
        return display;
    }
}
