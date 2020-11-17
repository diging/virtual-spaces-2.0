package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IVideoFactory;
import edu.asu.diging.vspace.core.model.IVSVideo;
import edu.asu.diging.vspace.core.model.impl.VSVideo;

@Service
public class VideoFactory implements IVideoFactory {

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.IImageFactory#createImage(java.lang.
     * String, java.lang.String)
     */
    @Override
    public IVSVideo createVideo(String filename, String fileType) {
        IVSVideo video = new VSVideo();
        video.setFilename(filename);
        video.setFileType(fileType);
        return video;
    }
    
}
