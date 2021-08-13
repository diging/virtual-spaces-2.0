package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ChoiceContentBlockRepository;
import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.factory.IChoiceBlockFactory;
import edu.asu.diging.vspace.core.factory.IImageBlockFactory;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.ChoiceBlock;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Transactional
@Service
public class ContentBlockManager implements IContentBlockManager {

    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private IImageFactory imageFactory;

    @Autowired
    private ITextBlockFactory textBlockFactory;

    @Autowired
    private IImageBlockFactory imageBlockFactory;

    @Autowired
    private IChoiceBlockFactory choiceBlockFactory;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private TextContentBlockRepository textBlockRepo;

    @Autowired
    private ImageContentBlockRepository imageBlockRepo;

    @Autowired
    private ChoiceContentBlockRepository choiceBlockRepo;

    @Autowired
    private IStorageEngine storage;

    @Autowired
    private ContentBlockRepository contentBlockRepository;

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
     * edu.asu.diging.vspace.core.services.impl.ITextBlock#createTextBlock(java.
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

    private IVSImage saveImage(byte[] image, String filename) {
        if (image != null && image.length > 0) {
            Tika tika = new Tika();
            String contentType = tika.detect(image);
            IVSImage slideContentImage = imageFactory.createImage(filename, contentType);
            slideContentImage = imageRepo.save((VSImage) slideContentImage);
            return slideContentImage;
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

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.services.impl.IContentBlockManager#
     * createImageBlock(java.lang.String, java.util.Arrays, java.lang.String)
     */
    @Override
    public CreationReturnValue createImageBlock(String slideId, byte[] image, String filename, Integer contentOrder)
            throws ImageCouldNotBeStoredException {

        ISlide slide = slideManager.getSlide(slideId);
        IVSImage slideContentImage = saveImage(image, filename);
        CreationReturnValue returnValue = new CreationReturnValue();
        returnValue.setErrorMsgs(new ArrayList<>());
        storeImageFile(image, slideContentImage, filename);
        IImageBlock imgBlock = imageBlockFactory.createImageBlock(slide, slideContentImage);
        imgBlock.setContentOrder(contentOrder);
        ImageBlock imageBlock = imageBlockRepo.save((ImageBlock) imgBlock);

        returnValue.setElement(imageBlock);
        return returnValue;
    }

    /**
     * Delete a text block using an id and also decrease content order by 1 of all
     * the slide's block which are after this block
     * 
     * @param blockId - id of resource to be deleted. If the id is null then the
     *                functions returns nothing.
     * @param slideId - id of the slide in which the text block with blockId is
     *                present.
     * 
     */

    @Override
    public void deleteTextBlockById(String blockId, String slideId) throws BlockDoesNotExistException {
        if (blockId == null) {
            return;
        }
        Integer contentOrder = null;
        Optional<ContentBlock> contentBlock = contentBlockRepository.findById(blockId);
        if (contentBlock.isPresent()) {
            contentOrder = contentBlock.get().getContentOrder();
        } else {
            throw new BlockDoesNotExistException("Block Id not present");
        }
        try {
            textBlockRepo.deleteById(blockId);
            updateContentOrder(slideId, contentOrder);
        } catch (EmptyResultDataAccessException e) {
            throw new BlockDoesNotExistException(e);
        }

    }

    /**
     * Delete an image block using an id and also decrease content order by 1 of all
     * the slide's block which are after this block
     * 
     * @param blockId - id of resource to be deleted. If the id is null then the
     *                functions returns nothing.
     * @param slideId - id of the slide in which the Image block with blockId is
     *                present.
     */

    @Override
    public void deleteImageBlockById(String blockId, String slideId) throws BlockDoesNotExistException {
        if (blockId == null) {
            return;
        }
        Integer contentOrder = null;
        Optional<ContentBlock> contentBlock = contentBlockRepository.findById(blockId);
        if (contentBlock.isPresent()) {
            contentOrder = contentBlock.get().getContentOrder();
        } else {
            throw new BlockDoesNotExistException("Block Id not present");
        }
        try {
            imageBlockRepo.deleteById(blockId);
            updateContentOrder(slideId, contentOrder);
        } catch (EmptyResultDataAccessException e) {
            throw new BlockDoesNotExistException(e);
        }

    }

    /**
     * Delete a choices block using an id and also decrease content order by 1 of
     * all the slide's block which are after this block
     * 
     * @param blockId - id of resource to be deleted. If the id is null then the
     *                functions returns nothing.
     * @param slideId - id of the slide in which the choice block with blockId is
     *                present.
     */

    @Override
    public void deleteChoiceBlockById(String blockId, String slideId) throws BlockDoesNotExistException {
        if (blockId == null) {
            return;
        }
        Integer contentOrder = null;
        Optional<ContentBlock> contentBlock = contentBlockRepository.findById(blockId);
        if (contentBlock.isPresent()) {
            contentOrder = contentBlock.get().getContentOrder();
        } else {
            throw new BlockDoesNotExistException("Block Id not present");
        }
        try {
            choiceBlockRepo.deleteById(blockId);
            updateContentOrder(slideId, contentOrder);
        } catch (EmptyResultDataAccessException e) {
            throw new BlockDoesNotExistException(e);
        }

    }

    public void updateTextBlock(TextBlock textBlock) {
        textBlockRepo.save((TextBlock) textBlock);
    }

    @Override
    public void updateImageBlock(IImageBlock imageBlock, byte[] image, String filename)
            throws ImageCouldNotBeStoredException {
        IVSImage slideContentImage = saveImage(image, filename);
        storeImageFile(image, slideContentImage, filename);
        imageBlock.setImage(slideContentImage);
        imageBlockRepo.save((ImageBlock) imageBlock);
    }

    @Override
    public IImageBlock getImageBlock(String imgBlockId) {
        Optional<ImageBlock> imgBlock = imageBlockRepo.findById(imgBlockId);
        if (imgBlock.isPresent()) {
            return imgBlock.get();
        }
        return null;
    }

    @Override
    public ITextBlock getTextBlock(String textBlockId) {
        Optional<TextBlock> textBlock = textBlockRepo.findById(textBlockId);
        if (textBlock.isPresent()) {
            return textBlock.get();
        }
        return null;
    }

    @Override
    public IChoiceBlock getChoiceBlock(String choiceBlockId) {
        Optional<ChoiceBlock> choiceBlock = choiceBlockRepo.findById(choiceBlockId);
        if (choiceBlock.isPresent()) {
            return choiceBlock.get();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.IChoiceBlock#createTextBlock(java.
     * lang.String, java.lang.String, java.lang.Integer)
     */
    @Override
    public IChoiceBlock createChoiceBlock(String slideId, List<String> selectedChoices, Integer contentOrder,
            boolean showsAll) {
        List<IChoice> choices = new ArrayList<IChoice>();
        if (!showsAll) {
            choices = selectedChoices.stream().map(choice -> slideManager.getChoice(choice))
                    .collect(Collectors.toList());
        }
        IChoiceBlock choiceBlock = choiceBlockFactory.createChoiceBlock(slideManager.getSlide(slideId), contentOrder,
                choices, showsAll);
        return choiceBlockRepo.save((ChoiceBlock) choiceBlock);
    }

    /**
     * Retrieving the maximum content order for a slide
     */
    @Override
    public Integer findMaxContentOrder(String slideId) {
        return contentBlockRepository.findMaxContentOrder(slideId);
    }

    /**
     * Adjusting the content order of the blocks of slide once it is dragged and changed position
     * 
     *  @param blockId - id of block whose content order needs to be adjusted.
     *  @param contentOrder - value with which contentOrder of block needs to be updated.
     *  
     */

    @Override
    public void adjustContentOrder(String blockId, Integer contentOrder) throws BlockDoesNotExistException {
        if (blockId == null) {
            return;
        }
        Optional<ContentBlock> contentBlock = contentBlockRepository.findById(blockId);
        if (contentBlock.isPresent()) {
            ContentBlock contentBlockObj = contentBlock.get();
            contentBlockObj.setContentOrder(contentOrder);
            contentBlockRepository.save(contentBlockObj);
        } else {
            throw new BlockDoesNotExistException("Block Id not present");
        }
    }
    
    /**
     * Decreasing content order by 1 of the slide's block which are after the
     * specified contentOrder
     * 
     * @param slideId      The Id of the slide for which content orders will be
     *                     updated
     * @param contentOrder The content orders of the slides which are greater than
     *                     contentOrder will be updated
     */
    private void updateContentOrder(String slideId, Integer contentOrder) {

        List<ContentBlock> contentBlockList = contentBlockRepository.findBySlide_IdAndContentOrderGreaterThan(slideId,
                contentOrder);
        if (contentBlockList != null) {
            for (ContentBlock eachContentBlock : contentBlockList) {
                eachContentBlock.setContentOrder(eachContentBlock.getContentOrder() - 1);
            }
            contentBlockRepository.saveAll(contentBlockList);
        }
    }
}
