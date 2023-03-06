package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IVideoBlockFactory;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSVideo;
import edu.asu.diging.vspace.core.model.IVideoBlock;
import edu.asu.diging.vspace.core.model.impl.VideoBlock;

@Service
public class VideoBlockFactory implements IVideoBlockFactory {

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.IVideoBlockFactory#createVideoBlock(
     * edu.asu.diging.vspace.core.model.ISlide,
     * edu.asu.diging.vspace.core.model.IVSVideo)
     */
    @Override
    public IVideoBlock createVideoBlock(ISlide slide, IVSVideo video) {
        IVideoBlock videoBlock = new VideoBlock();
        videoBlock.setVideo(video);
        videoBlock.setSlide(slide);

        return videoBlock;
    }
}
