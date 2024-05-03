package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IVSVideo;

public interface IVideoFactory {

    IVSVideo createVideo(String filename, Long size, String fileType);
    
    IVSVideo createVideo(String url);

}
