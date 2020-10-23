package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.display.ISpaceTextBlockDisplay;

public interface ISpaceTextBlockManager {
    ISpaceTextBlockDisplay createTextBlock(String id, float positionX, float positionY,String text,
            float height, float width) throws SpaceDoesNotExistException;

    List<ISpaceTextBlockDisplay> getSpaceTextBlockDisplays(String spaceId);
    
    void deleteTextBlock(String blockId);

    ISpaceTextBlockDisplay updateTextBlock(String id, Float float1, Float float2, String textBlockIdValueEdit,
            String textBlockDisplayId, String text, Float height, Float width);

}
