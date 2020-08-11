package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.display.ISpaceTextBlockDisplay;

public interface ISpaceTextBlockManager {
    ISpaceTextBlockDisplay createTextBlock(String id, float positionX, float positionY,String text,
            float height, float width) throws SpaceDoesNotExistException,ImageCouldNotBeStoredException, SpaceDoesNotExistException;

}
