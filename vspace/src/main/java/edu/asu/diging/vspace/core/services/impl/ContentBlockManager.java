package edu.asu.diging.vspace.core.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.VideoCouldNotBeStoredException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceBlock;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.IVideoBlock;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.services.IBiblioBlockManager;
import edu.asu.diging.vspace.core.services.IChoiceBlockManager;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.IImageBlockManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceBlockManager;
import edu.asu.diging.vspace.core.services.ITextBlockManager;
import edu.asu.diging.vspace.core.services.IVideoBlockManager;

@Transactional(rollbackFor = { Exception.class })
@Service
public class ContentBlockManager implements IContentBlockManager {

    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private ContentBlockRepository contentBlockRepository;

    @Autowired
    private ITextBlockManager textBlockManager;

    @Autowired
    private IImageBlockManager imageBlockManager;

    @Autowired
    private IVideoBlockManager videoBlockManager;

    @Autowired
    private ISpaceBlockManager spaceBlockManager;

    @Autowired
    private IChoiceBlockManager choiceBlockManager;

    @Autowired
    private IBiblioBlockManager biblioBlockManager;

    @Override
    public List<IContentBlock> getAllContentBlocks(String slideId) {
        ISlide slide = slideManager.getSlide(slideId);
        return slide.getContents();
    }

    @Override
    public ITextBlock createTextBlock(String slideId, String text) {
        return textBlockManager.createTextBlock(slideId, text);
    }

    @Override
    public ISpaceBlock createSpaceBlock(String slideId, String title, ISpace space) {
        return spaceBlockManager.createSpaceBlock(slideId, title, space);
    }

    @Override
    public CreationReturnValue createImageBlock(String slideId, byte[] image, String filename) 
            throws ImageCouldNotBeStoredException {
        return imageBlockManager.createImageBlock(slideId, image, filename);
    }

    @Override
    public CreationReturnValue createImageBlock(String slideId, IVSImage image) {
        return imageBlockManager.createImageBlock(slideId, image);
    }

    @Override
    public CreationReturnValue createVideoBlock(String slideId, byte[] video, Long size, String fileName, 
            String url, String title) throws VideoCouldNotBeStoredException {
        return videoBlockManager.createVideoBlock(slideId, video, size, fileName, url, title);
    }

    @Override
    public IChoiceBlock createChoiceBlock(String slideId, List<String> selectedChoices, boolean showsAll) {
        return choiceBlockManager.createChoiceBlock(slideId, selectedChoices, showsAll);
    }

    @Override
    public IBiblioBlock createBiblioBlock(String slideId, String title, String description) {
        return biblioBlockManager.createBiblioBlock(slideId, title, description);
    }

    @Override
    public void deleteTextBlockById(String blockId, String slideId) throws BlockDoesNotExistException {
        textBlockManager.deleteContentBlock(blockId, slideId);
    }

    @Override
    public void deleteSpaceBlockById(String blockId, String slideId) throws BlockDoesNotExistException {
        spaceBlockManager.deleteContentBlock(blockId, slideId);
    }

    @Override
    public void deleteImageBlockById(String blockId, String slideId) throws BlockDoesNotExistException {
        imageBlockManager.deleteContentBlock(blockId, slideId);
    }

    @Override
    public void deleteVideoBlockById(String blockId, String slideId) throws BlockDoesNotExistException {
        videoBlockManager.deleteContentBlock(blockId, slideId);
    }

    @Override
    public void deleteChoiceBlockById(String blockId, String slideId) throws BlockDoesNotExistException {
        choiceBlockManager.deleteContentBlock(blockId, slideId);
    }

    @Override
    public void deleteBiblioBlockById(String blockId, String slideId) throws BlockDoesNotExistException {
        biblioBlockManager.deleteBiblioBlockById(blockId, slideId);
    }

    @Override
    public void updateTextBlock(TextBlock textBlock) {
        textBlockManager.updateTextBlock(textBlock);
    }

    @Override
    public void saveSpaceBlock(ISpaceBlock spaceBlock) {
        spaceBlockManager.saveSpaceBlock(spaceBlock);
    }

    @Override
    public void updateImageBlock(IImageBlock imageBlock, byte[] image, String filename) throws ImageCouldNotBeStoredException {
        imageBlockManager.updateImageBlock(imageBlock, image, filename);
    }

    @Override
    public void updateImageBlock(IImageBlock imageBlock, IVSImage image) {
        imageBlockManager.updateImageBlock(imageBlock, image);
    }

    @Override
    public void updateVideoBlock(IVideoBlock videoBlock, byte[] video, Long fileSize, String url, String filename,
            String title) throws VideoCouldNotBeStoredException {
        videoBlockManager.updateVideoBlock(videoBlock, video, fileSize, url, filename, title);
    }

    @Override
    public IImageBlock getImageBlock(String imgBlockId) {
        return imageBlockManager.getContentBlock(imgBlockId);
    }

    @Override
    public IVideoBlock getVideoBlock(String videoBlockId) {
        return videoBlockManager.getContentBlock(videoBlockId);
    }

    @Override
    public ITextBlock getTextBlock(String textBlockId) {
        return textBlockManager.getContentBlock(textBlockId);
    }

    @Override
    public ISpaceBlock getSpaceBlock(String spaceBlockId) {
        return spaceBlockManager.getContentBlock(spaceBlockId);
    }

    @Override
    public IChoiceBlock getChoiceBlock(String choiceBlockId) {
        return choiceBlockManager.getContentBlock(choiceBlockId);
    }

    @Override
    public BiblioBlock getBiblioBlock(String biblioBlockId) {
        return (BiblioBlock) biblioBlockManager.getContentBlock(biblioBlockId);
    }

    @Override
    public void updateBiblioBlock(BiblioBlock biblioBlock) {
        biblioBlockManager.updateBiblioBlock(biblioBlock);
    }

    @Override
    public void saveVideoBlock(IVideoBlock videoBlock) {
        videoBlockManager.saveVideoBlock(videoBlock);
    }

    @Override
    public Integer findMaxContentOrder(String slideId) {
        return contentBlockRepository.findMaxContentOrder(slideId);
    }

    @Override
    public void updateContentOrder(List<ContentBlock> contentBlockList) throws BlockDoesNotExistException {
        if (contentBlockList == null) {
            return;
        }
        List<ContentBlock> contentBlocks = new java.util.ArrayList<>();
        for (ContentBlock eachBlock : contentBlockList) {
            String blockId = eachBlock.getId();
            int contentOrder = eachBlock.getContentOrder();
            java.util.Optional<ContentBlock> contentBlock = contentBlockRepository.findById(blockId);
            if (contentBlock.isPresent()) {
                ContentBlock contentBlockObj = contentBlock.get();
                contentBlockObj.setContentOrder(contentOrder);
                contentBlocks.add(contentBlockObj);
            } else {
                throw new BlockDoesNotExistException("Block Id not present");
            }
        }
        contentBlockRepository.saveAll(contentBlocks);
    }
}
