package edu.asu.diging.vspace.core.model.display;

import edu.asu.diging.vspace.core.model.IVSpaceElement;

public interface IDisplay extends IVSpaceElement {

    float getPositionX();

    void setPositionX(float positionX);

    float getPositionY();

    void setPositionY(float positionY);
}
