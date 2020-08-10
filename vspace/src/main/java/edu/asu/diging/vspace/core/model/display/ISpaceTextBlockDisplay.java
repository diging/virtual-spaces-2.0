package edu.asu.diging.vspace.core.model.display;

import edu.asu.diging.vspace.core.model.ISpaceTextBlock;

public interface ISpaceTextBlockDisplay extends ILinkDisplay{
    
    String getId();

    void setId(String id);

    void setSpaceTextBlock(ISpaceTextBlock link);

    ISpaceTextBlock getSpaceTextBlock();
}
