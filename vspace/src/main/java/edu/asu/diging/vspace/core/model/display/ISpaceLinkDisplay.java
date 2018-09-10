package edu.asu.diging.vspace.core.model.display;

import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.IVSpaceElement;

public interface ISpaceLinkDisplay extends IVSpaceElement {

    String getId();

    void setId(String id);

    float getPositionX();

    void setPositionX(float positionX);

    float getPositionY();

    void setPositionY(float positionY);

    void setLink(ISpaceLink link);

    ISpaceLink getLink();

    void setRotation(int rotation);

    int getRotation();

    void setType(DisplayType type);

    DisplayType getType();

}