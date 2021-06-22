package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IContentBlockManager {

    List<IContentBlock> getAllContentBlocks(String slideId);

    ITextBlock createTextBlock(String slideId, String content, Integer contentOrder);

    CreationReturnValue createImageBlock(String slideId, byte[] image, String filename, Integer contentOrder)
            throws ImageCouldNotBeStoredException;

    CreationReturnValue createImageBlock(String slideId, IVSImage slideContentImage, Integer contentOrder);

    void deleteTextBlockById(String blockid) throws BlockDoesNotExistException;

    void deleteImageBlockById(String blockid) throws BlockDoesNotExistException;

    void deleteChoiceBlockById(String blockid) throws BlockDoesNotExistException;

    void updateTextBlock(TextBlock textBlock);

    void updateImageBlock(IImageBlock imageBlock, byte[] image, String filename, Integer contentOrder)
            throws ImageCouldNotBeStoredException;

    void updateImageBlock(IImageBlock imageBlock, IVSImage slideContentImage, Integer contentOrder);

    IImageBlock getImageBlock(String imgBlockId);

    ITextBlock getTextBlock(String textBlockId);

    IChoiceBlock getChoiceBlock(String choiceBlockId);

    IChoiceBlock createChoiceBlock(String slideId, List<String> selectedChoices, Integer contentOrder,
            boolean showsAll);

}
