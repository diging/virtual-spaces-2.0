package edu.asu.diging.vspace.core.model;

public interface IVideoBlock extends IContentBlock {

    void setVideo(IVSVideo video);

    IVSVideo getVideo();

    void setId(String id);

    String getId();
    
}
