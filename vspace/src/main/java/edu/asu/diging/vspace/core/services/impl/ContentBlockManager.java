package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.BiblioBlockRepository;
import org.springframework.transaction.annotation.Transactional;
import edu.asu.diging.vspace.core.data.ChoiceContentBlockRepository;
import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.data.VideoContentBlockRepository;
import edu.asu.diging.vspace.core.data.VideoRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.VideoCouldNotBeStoredException;
import edu.asu.diging.vspace.core.factory.IChoiceBlockFactory;
import edu.asu.diging.vspace.core.factory.IImageBlockFactory;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.factory.IVideoBlockFactory;
import edu.asu.diging.vspace.core.factory.IVideoFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.model.IVSVideo;
import edu.asu.diging.vspace.core.model.IVideoBlock;
import edu.asu.diging.vspace.core.model.impl.ChoiceBlock;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.model.impl.VSVideo;
import edu.asu.diging.vspace.core.model.impl.VideoBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Transactional(rollbackFor = { Exception.class })
@Service
public class ContentBlockManager implements IContentBlockManager {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISlideManager slideManager;
    
    @Autowired
    private IImageFactory imageFactory;

    @Autowired
    private IVideoFactory videoFactory;

    @Autowired
    private ITextBlockFactory textBlockFactory;
    
    @Autowired
    private IImageBlockFactory imageBlockFactory;

    @Autowired
    private IVideoBlockFactory videoBlockFactory;

    @Autowired
    private IChoiceBlockFactory choiceBlockFactory;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private VideoRepository videoRepo;

    @Autowired
    private TextContentBlockRepository textBlockRepo;
    
    @Autowired
    private BiblioBlockRepository biblioBlockRepo;

    @Autowired
    private ImageContentBlockRepository imageBlockRepo;

    @Autowired
    private VideoContentBlockRepository videoBlockRepo;

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

    private IVSVideo saveVideoWithUrl(String url, String title) {
        IVSVideo vidContent = videoFactory.createVideo(url);
        vidContent.setTitle(title);
        return videoRepo.save((VSVideo) vidContent);
    }

    private IVSVideo saveVideo(byte[] video, Long size, String filename, String title) {
        if (video != null && video.length > 0) {
            Tika tika = new Tika();
            String contentType = tika.detect(video);
            IVSVideo slideContentVideo = videoFactory.createVideo(filename, size, contentType);
            slideContentVideo.setTitle(title);
            slideContentVideo = videoRepo.save((VSVideo) slideContentVideo);
            return slideContentVideo;
        }
        return null;
    }

    private void storeImageFile(byte[] image, IVSImage slideContentImage, String filename) throws ImageCouldNotBeStoredException {
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

    private void storeVideoFile(byte[] video, IVSVideo slideContentVideo, String filename) throws VideoCouldNotBeStoredException {
        if (slideContentVideo != null) {
            String relativePath = null;
            try {
                relativePath = storage.storeFile(video, filename, slideContentVideo.getId());
            } catch (FileStorageException e) {
                throw new VideoCouldNotBeStoredException(e);
            }
            slideContentVideo.setParentPath(relativePath);
            videoRepo.save((VSVideo) slideContentVideo);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.services.impl.IContentBlockManager#
     * createImageBlock(java.lang.String, java.util.Arrays, java.lang.String)
     */
    @Override
    public CreationReturnValue createImageBlock(String slideId, byte[] image, String filename, Integer contentOrder) throws ImageCouldNotBeStoredException {
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
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.services.impl.IContentBlockManager#
     *      createImageBlock(java.lang.String,
     *      edu.asu.diging.vspace.core.model.IVSImage, java.lang.Integer)
     */
    @Override
    public CreationReturnValue createImageBlock(String slideId, IVSImage image, Integer contentOrder) {
        CreationReturnValue returnValue = new CreationReturnValue();
        returnValue.setErrorMsgs(new ArrayList<>());
        ISlide slide = slideManager.getSlide(slideId);
        IImageBlock imgBlock = imageBlockFactory.createImageBlock(slide, image);
        imgBlock.setContentOrder(contentOrder);
        ImageBlock imageBlock = imageBlockRepo.save((ImageBlock) imgBlock);
        returnValue.setElement(imageBlock);
        return returnValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.services.impl.IContentBlockManager#
     * createVideoBlock(java.lang.String, java.util.Arrays, java.lang.String)
     */
    @Override
    public CreationReturnValue createVideoBlock(String slideId, byte[] video, Long size, String fileName, String url,
            Integer contentOrder, String title) throws VideoCouldNotBeStoredException {
        ISlide slide = slideManager.getSlide(slideId);
        CreationReturnValue returnValue = new CreationReturnValue();
        returnValue.setErrorMsgs(new ArrayList<>());
        IVSVideo slideContentVideo = storeVideo(video, size, fileName, url, title);
        IVideoBlock vidBlock = videoBlockFactory.createVideoBlock(slide, slideContentVideo);
        vidBlock.setContentOrder(contentOrder);
        VideoBlock videoBlock = videoBlockRepo.save((VideoBlock) vidBlock);
        returnValue.setElement(videoBlock);
        return returnValue;
    }

    private IVSVideo storeVideo(byte[] video, Long size, String fileName, String url, String title)
            throws VideoCouldNotBeStoredException {
        IVSVideo slideContentVideo = null;
        if (video != null) {
            slideContentVideo = saveVideo(video, size, fileName, title);
            storeVideoFile(video, slideContentVideo, fileName);
            slideContentVideo.setUrl(null);
        } else if (url != null && !url.isEmpty()) {
            slideContentVideo = saveVideoWithUrl(url, title);
        }
        return slideContentVideo;
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
     * Delete an video block using an id and also decrease content order by 1 of all
     * the slide's block which are after this block
     * 
     * @param blockId - id of resource to be deleted. If the id is null then the
     *                functions returns nothing.
     * @param slideId - id of the slide in which the Image block with blockId is
     *                present.
     */

    @Override
    public void deleteVideoBlockById(String blockId, String slideId) throws BlockDoesNotExistException {
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
            videoBlockRepo.deleteById(blockId);
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
    public void updateImageBlock(IImageBlock imageBlock, byte[] image, String filename) throws ImageCouldNotBeStoredException {
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
    public void updateVideoBlock(IVideoBlock videoBlock, byte[] video, Long fileSize, String url, String filename,
            String title) throws VideoCouldNotBeStoredException {
        IVSVideo slideContentVideo = storeVideo(video, fileSize, filename, url, title);

        videoBlock.setVideo(slideContentVideo);
        videoBlockRepo.save((VideoBlock) videoBlock);
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
    public IVideoBlock getVideoBlock(String videoBlockId) {
        Optional<VideoBlock> videoBlock = videoBlockRepo.findById(videoBlockId);
        if (videoBlock.isPresent()) {
            return videoBlock.get();
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

    @Override
    public IBiblioBlock createBiblioBlock(String slideId, String title, String description, Integer contentOrder) {
        ISlide slide = slideManager.getSlide(slideId);
        IBiblioBlock bilioBlock = new BiblioBlock();
        bilioBlock.setDescription(description);
        bilioBlock.setBiblioTitle(title);
        bilioBlock.setSlide(slide);
        bilioBlock.setContentOrder(contentOrder);
        return biblioBlockRepo.save((BiblioBlock) bilioBlock);
    }

    @Override
    public void deleteBiblioBlockById(String id) throws BlockDoesNotExistException {
        if (id == null) {
            logger.warn("Attempted to delete biblio block with null id.");
            return;
        }

        try {
            biblioBlockRepo.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new BlockDoesNotExistException("Biblio block with id " + id + " does not exist.", e);
        }
    }

    @Override
    public void updateBiblioBlock(BiblioBlock biblioBlock) {
        biblioBlockRepo.save((BiblioBlock) biblioBlock);
    }

    @Override
    public IBiblioBlock getBiblioBlock(String biblioBlockId) {
        Optional<BiblioBlock> biblioBlock = biblioBlockRepo.findById(biblioBlockId);
        if (biblioBlock.isPresent()) {
            return biblioBlock.get();
        }
        return null;
    }
    
    /**
     * Retrieving the maximum content order for a slide
     */
    @Override
    public Integer findMaxContentOrder(String slideId) {
        return contentBlockRepository.findMaxContentOrder(slideId);
    }

    /**
     * Adjusting the content order of the blocks of slide once it is dragged and
     * changed position.
     * 
     * @param contentBlockList - The list contains the blocks and the updated
     *                         content order corresponding to each blocks.
     */
    @Override
    public void updateContentOrder(List<ContentBlock> contentBlockList) throws BlockDoesNotExistException {
        if (contentBlockList == null) {
            return;
        }
        List<ContentBlock> contentBlocks = new ArrayList<>();
        for (ContentBlock eachBlock : contentBlockList) {
            String blockId = eachBlock.getId();
            int contentOrder = eachBlock.getContentOrder();
            Optional<ContentBlock> contentBlock = contentBlockRepository.findById(blockId);
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
    
    @Override
    public void saveVideoBlock(IVideoBlock videoBlock){
        videoRepo.save((VSVideo) videoBlock.getVideo());
    }
}
