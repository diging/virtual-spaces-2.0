package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import edu.asu.diging.vspace.core.model.IVSVideo;
import edu.asu.diging.vspace.core.model.IVideoBlock;

@Entity
public class VideoBlock extends ContentBlock implements IVideoBlock {
    
    private String url;
    
    @OneToOne(targetEntity = VSVideo.class)
    private IVSVideo video;

    
    public VideoBlock(String url) {
        this.url = url;
    }
    
    // Default constructor.
    public VideoBlock() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public IVSVideo getVideo() {
        return video;
    }

    public void setVideo(IVSVideo video) {
        this.video = video;
    }

}
