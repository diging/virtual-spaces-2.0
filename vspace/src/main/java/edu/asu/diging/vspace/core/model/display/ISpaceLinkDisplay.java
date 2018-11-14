package edu.asu.diging.vspace.core.model.display;

import edu.asu.diging.vspace.core.model.ISpaceLink;

public interface ISpaceLinkDisplay extends IDisplay {

    String getId();

    void setId(String id);

    void setLink(ISpaceLink link);

    ISpaceLink getLink();

    void setRotation(int rotation);

    int getRotation();

    void setType(DisplayType type);

    DisplayType getType();

}