package edu.asu.diging.vspace.core.model.display;

import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.IVSpaceElement;

public interface ILinkDisplay extends IVSpaceElement {

    float getPositionX();

    void setPositionX(float positionX);

    float getPositionY();

    void setPositionY(float positionY);

    void setImage(IVSImage image);

    IVSImage getImage();
}
