package edu.asu.diging.vspace.core.model.display;

import edu.asu.diging.vspace.core.model.ISpace;

public interface ISpaceDisplay {

    String getId();

    void setId(String id);

    ISpace getSpace();

    void setSpace(ISpace space);

    int getWidth();

    void setWidth(int width);

    int getHeight();

    void setHeight(int height);

}