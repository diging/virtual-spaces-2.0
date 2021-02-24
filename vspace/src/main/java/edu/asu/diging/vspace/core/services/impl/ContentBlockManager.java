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
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.IVSVideo;
import edu.asu.diging.vspace.core.model.IVideoBlock;
import edu.asu.diging.vspace.core.model.impl.ChoiceBlock;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.model.impl.VSVideo;
import edu.asu.diging.vspace.core.model.impl.VideoBlock;
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
    private ImageContentBlockRepository imageBlockRepo;

    @Autowired
    private VideoContentBlockRepository videoBlockRepo;

    @Autowired
    private ChoiceContentBlockRepository choiceBlockRepo;

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
     * @see edu.asu.diging.vspace.core.services.impl.ITextBlock#createTextBlock(java.
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

    private IVSVideo saveVideoWithUrl(String url) {
        IVSVideo vidContent = videoFactory.createVideo(url);
        return videoRepo.save((VSVideo) vidContent);
    }
    
    private IVSVideo saveVideo(byte[] video, Long size, String filename) { 
        if (video != null && video.length > 0) { 
            Tika tika = new Tika();
            String contentType = tika.detect(video);
            IVSVideo slideContentVideo = videoFactory.createVideo(filename, size, contentType);
            slideContentVideo = videoRepo.save((VSVideo) slideContentVideo);
            return slideContentVideo;
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

    private void storeVideoFile(byte[] video, IVSVideo slideContentVideo, String filename)
            throws VideoCouldNotBeStoredException {
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

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.services.impl.IContentBlockManager#
     * createVideoBlock(java.lang.String, java.util.Arrays, java.lang.String)
     */
    @Override
    public CreationReturnValue createVideoBlock(String slideId, byte[] video, Long size, String fileName, String url, Integer contentOrder)
            throws VideoCouldNotBeStoredException {
        ISlide slide = slideManager.getSlide(slideId);
        CreationReturnValue returnValue = new CreationReturnValue();
        returnValue.setErrorMsgs(new ArrayList<>());
        IVideoBlock vidBlock = null;
        if (url == null || url.equals("")) {
            IVSVideo slideContentVideo = saveVideo(video, size, fileName);
            storeVideoFile(video, slideContentVideo, fileName);
            vidBlock = videoBlockFactory.createVideoBlock(slide, slideContentVideo);
        } else {
            IVSVideo vidContent = saveVideoWithUrl(url);
            vidBlock = videoBlockFactory.createVideoBlock(slide, vidContent);
        }
        vidBlock.setContentOrder(contentOrder);
        VideoBlock videoBlock = videoBlockRepo.save((VideoBlock) vidBlock);

        returnValue.setElement(videoBlock);

        return returnValue;
    }

    /**
     * Delete a text block using an id
     * 
     * @param id - id of resource to be deleted. If the id is null then the
     *           functions returns nothing.
     *
     */

    @Override
    public void deleteTextBlockById(String id) throws BlockDoesNotExistException {
        if (id == null) {
            return;
        }
        try {
            textBlockRepo.deleteById(id);         
        } catch (EmptyResultDataAccessException e) {
            throw new BlockDoesNotExistException(e);
        }

    }

    /**
     * Delete an image block using an id
     * 
     * @param id - id of resource to be deleted. If the id is null then the
     *           functions returns nothing.
     *
     */

    @Override
    public void deleteImageBlockById(String id) throws BlockDoesNotExistException {
        if (id == null) {
            return;
        }
        try {
            imageBlockRepo.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new BlockDoesNotExistException(e);
        }

    }
    /**
     * Delete an video block using an id
     * 
     * @param id - id of resource to be deleted. If the id is null then the
     *           functions returns nothing.
     *
     */

    @Override
    public void deleteVideoBlockById(String id) throws BlockDoesNotExistException {
        if (id == null) {
            return;
        }
        try {
            videoBlockRepo.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new BlockDoesNotExistException(e);
        }
    }
    /**
     * Delete a choices block using an id
     * 
     * @param id - id of resource to be deleted. If the id is null then the
     *           functions returns nothing.
     *
     */

    @Override
    public void deleteChoiceBlockById(String id) throws BlockDoesNotExistException {
        if (id == null) {
            return;
        }
        try {
            choiceBlockRepo.deleteById(id);         
        } catch (EmptyResultDataAccessException e) {
            throw new BlockDoesNotExistException(e);
        }

    }

    public void updateTextBlock(TextBlock textBlock) {
        textBlockRepo.save((TextBlock) textBlock);
    }

    @Override
    public void updateImageBlock(IImageBlock imageBlock, byte[] image, String filename, Integer contentOrder)
            throws ImageCouldNotBeStoredException {
        IVSImage slideContentImage = saveImage(image, filename);
        storeImageFile(image, slideContentImage, filename);
        imageBlock.setImage(slideContentImage);
        imageBlockRepo.save((ImageBlock) imageBlock);
    }

    @Override
    public void updateVideoBlock(IVideoBlock videoBlock, byte[] video, Long fileSize, String url, String filename, Integer contentOrder)
            throws VideoCouldNotBeStoredException {
        IVSVideo slideContentVideo = null;
        if(video != null ) {
            slideContentVideo = saveVideo(video, fileSize, filename);
            storeVideoFile(video, slideContentVideo, filename);
            slideContentVideo.setUrl(null);
        } else {
            slideContentVideo = saveVideoWithUrl(url);
        }
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
     * @see edu.asu.diging.vspace.core.services.impl.IChoiceBlock#createTextBlock(java.
     * lang.String, java.lang.String, java.lang.Integer)
     */
    @Override
    public IChoiceBlock createChoiceBlock(String slideId, List<String> selectedChoices, Integer contentOrder, boolean showsAll) {
        List<IChoice> choices = new ArrayList<IChoice>();
        if(!showsAll) {
            choices = selectedChoices.stream().map(choice -> slideManager.getChoice(choice)).collect(Collectors.toList());
        }
        IChoiceBlock choiceBlock = choiceBlockFactory.createChoiceBlock(slideManager.getSlide(slideId), contentOrder, choices, showsAll);
        return choiceBlockRepo.save((ChoiceBlock)choiceBlock);
    }

}
