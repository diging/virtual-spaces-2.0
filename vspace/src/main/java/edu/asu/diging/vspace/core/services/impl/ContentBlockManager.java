package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ImageContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.factory.IImageBlockFactory;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
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
    private SlideRepository slideRepo;

    @Autowired
    private IStorageEngine storage;

    private int BlockInOrder = 0;

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map<edu.asu.diging.vspace.core.services.impl.IContentBlock,
     * java.lang.String>#getAllContentBlocks(java.lang.String)
     */
    @Override
    public Map<IContentBlock, String> getAllContentBlocks(String slideId) {
        ISlide slide = slideManager.getSlide(slideId);
        Map<IContentBlock, String> slideContentBlocks = new LinkedHashMap<IContentBlock, String>();

        for (IContentBlock block : slide.getContents()) {
            if (block.getDescription().equals("text")) {
                slideContentBlocks.put(block, ((TextBlock) block).getText());
            } else {
                slideContentBlocks.put(block, ((ImageBlock) block).getImage().getId());
            }

        }
        return slideContentBlocks;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.IContentBlock#createTextBlock(java.
     * lang.String, java.lang.String)
     */
    @Override
    public IContentBlock createTextBlock(String slideId, String text) {
        ISlide slide = slideManager.getSlide(slideId);
        IContentBlock textBlock = textBlockFactory.createTextBlock(slide, text);
        textBlock.setBlockInOrder(BlockInOrder);
        if (slide.getContents() == null) {
            slide.setContents(new ArrayList<>());
        }
        slide.getContents().add(textBlock);
        textBlock = textBlockRepo.save((TextBlock) textBlock);
        slideRepo.save((Slide) slide);
        BlockInOrder++;

        return textBlock;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.IImageBlock#storeImageBlock(edu.
     * asu.diging.vspace.core.model.IContentBlock)
     */
    @Override
    public IImageBlock storeImageBlock(IContentBlock imageBlock) {
        return imageBlockRepo.save((IImageBlock) imageBlock);

    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.services.impl.IContentBlockManager#
     * createImageBlock(java.lang.String, java.util.Arrays, java.lang.String)
     */
    @Override
    public CreationReturnValue createImageBlock(String slideId, byte[] image, String filename) {
        // TODO Auto-generated method stub
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
                returnValue.getErrorMsgs().add("Image could not be stored: " + e.getMessage());
            }
            slideContentImage.setParentPath(relativePath);
            imageRepo.save((VSImage) slideContentImage);
        }

        IContentBlock imgBlock = imageBlockFactory.createImageBlock(slide, slideContentImage);
        imgBlock.setBlockInOrder(BlockInOrder);
        ImageBlock imageBlock = (ImageBlock) storeImageBlock(imgBlock);
        BlockInOrder++;

        returnValue.setElement(imageBlock);
        return returnValue;
    }

}
