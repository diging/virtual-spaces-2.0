package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.display.ISpaceTextBlockDisplay;

public interface ISpaceTextBlockManager {
    ISpaceTextBlockDisplay createTextBlock(String id, float positionX, float positionY,String text,
            float height, float width, String textColor) throws SpaceDoesNotExistException;

    List<ISpaceTextBlockDisplay> getSpaceTextBlockDisplays(String spaceId);
    
    void deleteTextBlock(String blockId);

    ISpaceTextBlockDisplay updateTextBlock(String id, float positionX, float positionY, String textBlockIdValueEdit,
            String textBlockDisplayId, String text, float height, float width);

}
