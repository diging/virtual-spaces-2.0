package edu.asu.diging.vspace.core.model.display.impl;

import edu.asu.diging.vspace.core.model.display.IDisplay;
import edu.asu.diging.vspace.core.model.impl.VSpaceElement;

public abstract class Display extends VSpaceElement implements IDisplay {

    private float positionX;
    private float positionY;

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
}
