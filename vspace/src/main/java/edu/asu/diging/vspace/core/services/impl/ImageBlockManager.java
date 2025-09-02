package edu.asu.diging.vspace.core.services.impl;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ImageContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.factory.IImageBlockFactory;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IImageBlockManager;

@Service
public class ImageBlockManager extends GenericContentBlockManager<IImageBlock, ImageContentBlockRepository> 
        implements IImageBlockManager {

    @Autowired
    private IImageFactory imageFactory;

    @Autowired
    private IImageBlockFactory imageBlockFactory;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private ImageContentBlockRepository imageBlockRepo;

    @Autowired
    private IStorageEngine storage;

    @Override
    protected ImageContentBlockRepository getRepository() {
        return imageBlockRepo;
    }

    @Override
    public IImageBlock createContentBlock(String slideId) {
        // Default implementation - creates an image block without image data
        try {
            return createImageBlock(slideId, null, "default.jpg").getElement();
        } catch (ImageCouldNotBeStoredException e) {
            throw new RuntimeException("Failed to create default image block", e);
        }
    }

    @Override
    public CreationReturnValue createImageBlock(String slideId, byte[] image, String filename) 
            throws ImageCouldNotBeStoredException {
        ISlide slide = slideManager.getSlide(slideId);
        Integer contentOrder = getNextContentOrder(slideId);
        
        IVSImage slideContentImage = saveImage(image, filename);
        CreationReturnValue returnValue = new CreationReturnValue();
        returnValue.setErrorMsgs(new java.util.ArrayList<>());
        
        storeImageFile(image, slideContentImage, filename);
        IImageBlock imgBlock = imageBlockFactory.createImageBlock(slide, slideContentImage);
        imgBlock.setContentOrder(contentOrder);
        ImageBlock imageBlock = imageBlockRepo.save((ImageBlock) imgBlock);

        returnValue.setElement(imageBlock);
        return returnValue;
    }

    /**
     * Creates a new image block with an existing image.
     * 
     * @param slideId The ID of the slide
     * @param image The existing image
     * @return The creation result with the created image block
     */
    public CreationReturnValue createImageBlock(String slideId, IVSImage image) {
        ISlide slide = slideManager.getSlide(slideId);
        Integer contentOrder = getNextContentOrder(slideId);
        
        CreationReturnValue returnValue = new CreationReturnValue();
        returnValue.setErrorMsgs(new java.util.ArrayList<>());
        
        IImageBlock imgBlock = imageBlockFactory.createImageBlock(slide, image);
        imgBlock.setContentOrder(contentOrder);
        ImageBlock imageBlock = imageBlockRepo.save((ImageBlock) imgBlock);
        returnValue.setElement(imageBlock);
        return returnValue;
    }

    /**
     * Updates an image block with new image data.
     * 
     * @param imageBlock The image block to update
     * @param image The new image bytes
     * @param filename The filename
     * @throws ImageCouldNotBeStoredException if image storage fails
     */
    public void updateImageBlock(IImageBlock imageBlock, byte[] image, String filename) 
            throws ImageCouldNotBeStoredException {
        IVSImage slideContentImage = saveImage(image, filename);
        storeImageFile(image, slideContentImage, filename);
        imageBlock.setImage(slideContentImage);
        imageBlockRepo.save((ImageBlock) imageBlock);
    }

    @Override
    public void updateImageBlock(IImageBlock imageBlock, IVSImage image) {
        imageBlock.setImage(image);
        imageBlockRepo.save((ImageBlock) imageBlock);
    }

    @Override
    public void updateContentBlock(IImageBlock imageBlock) {
        imageBlockRepo.save((ImageBlock) imageBlock);
    }

    @Override
    public void saveContentBlock(IImageBlock imageBlock) {
        imageBlockRepo.save((ImageBlock) imageBlock);
    }

    private IVSImage saveImage(byte[] image, String filename) {
        if (image != null && image.length > 0) {
            Tika tika = new Tika();
            String contentType = tika.detect(image);
            IVSImage slideContentImage = imageFactory.createImage(filename, contentType);
            return imageRepo.save((VSImage) slideContentImage);
        }
        return null;
    }

    private void storeImageFile(byte[] image, IVSImage slideContentImage, String filename) 
            throws ImageCouldNotBeStoredException {
        if (slideContentImage != null) {
            String relativePath = null;
            try {
                relativePath = storage.storeFile(image, filename, slideContentImage.getId());
            } catch (FileStorageException e) {
                throw new ImageCouldNotBeStoredException(e);
            }
            slideContentImage.setParentPath(relativePath);
            imageRepo.save((VSImage) slideContentImage);
        }
    }
}
