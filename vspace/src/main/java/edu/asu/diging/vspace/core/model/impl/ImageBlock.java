package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.IVSImage;

@Entity
public class ImageBlock extends ContentBlock implements IImageBlock {
    
    @OneToOne(targetEntity=VSImage.class)
    private IVSImage image;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ITextBlock#getImage()
     */
    @Override
    public IVSImage getImage() {
        return image;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ITextBlock#setImage(edu.asu.diging.vspace.
     * core.model.IVSImage)
     */
    @Override
    public void setImage(IVSImage image) {
        this.image = image;
    }
}
