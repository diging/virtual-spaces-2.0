package edu.asu.diging.vspace.core.model.display.impl;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.model.impl.VSpaceElement;

@MappedSuperclass
public abstract class LinkDisplay extends VSpaceElement implements ILinkDisplay {

    private float positionX;
    private float positionY;
    
    @OneToOne(targetEntity=VSImage.class)
    private IVSImage image;

    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.display.impl.IDisplay#getPositionX()
     */
    @Override
    public float getPositionX() {
        return positionX;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.display.impl.IDisplay#setPositionX(float)
     */
    @Override
    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.display.impl.IDisplay#getPositionY()
     */
    @Override
    public float getPositionY() {
        return positionY;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.display.impl.IDisplay#setPositionY(float)
     */
    @Override
    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    @Override
    public IVSImage getImage() {
        return image;
    }

    @Override
    public void setImage(IVSImage image) {
        this.image = image;
    }
}
