package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import edu.asu.diging.vspace.core.model.IVSVideo;
import edu.asu.diging.vspace.core.model.IVideoBlock;

@Entity
public class VideoBlock extends ContentBlock implements IVideoBlock {
    
    @OneToOne(targetEntity = VSVideo.class)
    private IVSVideo video;
    
    public VideoBlock() {
    }
    
    public IVSVideo getVideo() {
        return video;
    }

    public void setVideo(IVSVideo video) {
        this.video = video;
    }

}
