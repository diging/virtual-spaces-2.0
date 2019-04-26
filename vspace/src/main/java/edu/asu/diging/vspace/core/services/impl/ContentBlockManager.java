package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ImageContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.factory.IImageBlockFactory;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Transactional
@Service
public class ContentBlockManager implements IContentBlockManager {

    @Autowired
    private ITextBlockFactory textBlockFactory;

    @Autowired
    private IImageBlockFactory imageBlockFactory;

    @Autowired
    private SlideManager slideManager;

    @Autowired
    private TextContentBlockRepository textBlockRepo;

    @Autowired
    private ImageContentBlockRepository imageBlockRepo;

    @Autowired
    private IImageFactory imageFactory;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private IStorageEngine storage;

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List<edu.asu.diging.vspace.core.services.impl.IContentBlock>#
     * getAllContentBlocks(java.lang.String)
     */
    @Override
    public List<IContentBlock> getAllContentBlocks(String slideId) {
        ISlide slide = slideManager.getSlide(slideId);
        return slide.getContents();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.IContentBlock#createTextBlock(java.
     * lang.String, java.lang.String)
     */
    @Override
    public ITextBlock createTextBlock(String slideId, String text, Integer contentOrder) {
        ISlide slide = slideManager.getSlide(slideId);
        ITextBlock textBlock = textBlockFactory.createTextBlock(slide, text);
        textBlock.setContentOrder(contentOrder);
        textBlock = textBlockRepo.save((TextBlock) textBlock);
        
        return textBlock;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.services.impl.IContentBlockManager#
     * createImageBlock(java.lang.String, java.util.Arrays, java.lang.String)
     */
    @Override
    public CreationReturnValue createImageBlock(String slideId, byte[] image, String filename, Integer contentOrder) throws ImageCouldNotBeStoredException {
        IVSImage slideContentImage = null;
        ISlide slide = slideManager.getSlide(slideId);
        if (image != null && image.length > 0) {
            Tika tika = new Tika();
            String contentType = tika.detect(image);

            slideContentImage = imageFactory.createImage(filename, contentType);
            slideContentImage = imageRepo.save((VSImage) slideContentImage);
        }

        CreationReturnValue returnValue = new CreationReturnValue();
        returnValue.setErrorMsgs(new ArrayList<>());

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

        IImageBlock imgBlock = imageBlockFactory.createImageBlock(slide, slideContentImage);
        imgBlock.setContentOrder(contentOrder);
        ImageBlock imageBlock = imageBlockRepo.save((ImageBlock) imgBlock);

        returnValue.setElement(imageBlock);
        return returnValue;
    }

}
