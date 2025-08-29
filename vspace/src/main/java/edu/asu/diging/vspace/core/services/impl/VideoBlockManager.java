package edu.asu.diging.vspace.core.services.impl;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.VideoContentBlockRepository;
import edu.asu.diging.vspace.core.data.VideoRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.VideoCouldNotBeStoredException;
import edu.asu.diging.vspace.core.factory.IVideoBlockFactory;
import edu.asu.diging.vspace.core.factory.IVideoFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSVideo;
import edu.asu.diging.vspace.core.model.IVideoBlock;
import edu.asu.diging.vspace.core.model.impl.VSVideo;
import edu.asu.diging.vspace.core.model.impl.VideoBlock;

@Service
public class VideoBlockManager extends AbstractContentBlockManager<IVideoBlock, VideoContentBlockRepository> {

    @Autowired
    private IVideoFactory videoFactory;

    @Autowired
    private IVideoBlockFactory videoBlockFactory;

    @Autowired
    private VideoRepository videoRepo;

    @Autowired
    private VideoContentBlockRepository videoBlockRepo;

    @Autowired
    private IStorageEngine storage;

    @Override
    protected VideoContentBlockRepository getRepository() {
        return videoBlockRepo;
    }

    /**
     * Creates a new video block.
     * 
     * @param slideId The ID of the slide
     * @param video The video bytes (null if using URL)
     * @param size The file size
     * @param fileName The filename
     * @param url The video URL (null if using file upload)
     * @param title The video title
     * @return The creation result with the created video block
     * @throws VideoCouldNotBeStoredException if video storage fails
     */
    public CreationReturnValue createVideoBlock(String slideId, byte[] video, Long size, String fileName, 
            String url, String title) throws VideoCouldNotBeStoredException {
        ISlide slide = slideManager.getSlide(slideId);
        Integer contentOrder = getNextContentOrder(slideId);
        
        CreationReturnValue returnValue = new CreationReturnValue();
        returnValue.setErrorMsgs(new java.util.ArrayList<>());
        
        IVSVideo slideContentVideo = storeVideo(video, size, fileName, url, title);
        IVideoBlock vidBlock = videoBlockFactory.createVideoBlock(slide, slideContentVideo);
        vidBlock.setContentOrder(contentOrder);
        VideoBlock videoBlock = videoBlockRepo.save((VideoBlock) vidBlock);
        returnValue.setElement(videoBlock);
        return returnValue;
    }

    /**
     * Updates an existing video block.
     * 
     * @param videoBlock The video block to update
     * @param video The video bytes
     * @param fileSize The file size
     * @param url The video URL
     * @param filename The filename
     * @param title The video title
     * @throws VideoCouldNotBeStoredException if video storage fails
     */
    public void updateVideoBlock(IVideoBlock videoBlock, byte[] video, Long fileSize, String url, 
            String filename, String title) throws VideoCouldNotBeStoredException {
        IVSVideo slideContentVideo = storeVideo(video, fileSize, filename, url, title);
        videoBlock.setVideo(slideContentVideo);
        videoBlockRepo.save((VideoBlock) videoBlock);
    }

    /**
     * Saves a video block (used for updating video properties).
     * 
     * @param videoBlock The video block to save
     */
    public void saveVideoBlock(IVideoBlock videoBlock) {
        videoRepo.save((VSVideo) videoBlock.getVideo());
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
            return videoRepo.save((VSVideo) slideContentVideo);
        }
        return null;
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
}
