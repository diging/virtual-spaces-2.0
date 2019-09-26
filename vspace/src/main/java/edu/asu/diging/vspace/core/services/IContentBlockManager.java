package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IContentBlockManager {

    ITextBlock createTextBlock(String slideId, String content, Integer contentOrder);

    CreationReturnValue createImageBlock(String slideId, byte[] image, String filename, Integer contentOrder)
            throws ImageCouldNotBeStoredException;

    List<IContentBlock> getAllContentBlocks(String slideId);

    void updateTextBlock(TextBlock textBlock);

    IImageBlock getImageBlock(String imgBlockId);

    ITextBlock getTextBlock(String textBlockId);

    void updateImageBlock(IImageBlock imageBlock, byte[] image, String filename, Integer contentOrder)
            throws ImageCouldNotBeStoredException;

    IChoiceBlock createChoiceBlock(String slideId, String choice, Integer contentOrder);

}
