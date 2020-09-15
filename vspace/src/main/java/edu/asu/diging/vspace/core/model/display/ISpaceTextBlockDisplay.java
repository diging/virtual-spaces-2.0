package edu.asu.diging.vspace.core.model.display;

import edu.asu.diging.vspace.core.model.ISpaceTextBlock;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.impl.SpaceTextBlock;

public interface ISpaceTextBlockDisplay extends IVSpaceElement{
    
    String getId();

    void setId(String id);

    void setSpaceTextBlock(ISpaceTextBlock textBlock);

    ISpaceTextBlock getSpaceTextBlock();
    
    float getPositionX();

    void setPositionX(float positionX);

    float getPositionY();

    void setPositionY(float positionY);
    
    float getHeight();

    void setHeight(float height);

    float getWidth();

    void setWidth(float width);
}
