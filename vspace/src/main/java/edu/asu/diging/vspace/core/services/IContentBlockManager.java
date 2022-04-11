package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceBlock;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.model.impl.SpaceBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IContentBlockManager {

    ITextBlock createTextBlock(String slideId, String content, Integer contentOrder);

    CreationReturnValue createImageBlock(String slideId, byte[] image, String filename, Integer contentOrder)
            throws ImageCouldNotBeStoredException;
    
    CreationReturnValue createImageBlock(String slideId, IVSImage image, Integer contentOrder);

    List<IContentBlock> getAllContentBlocks(String slideId);

    void deleteTextBlockById(String blockid, String slideId) throws BlockDoesNotExistException;

    void deleteImageBlockById(String blockid, String slideId) throws BlockDoesNotExistException;

    void deleteChoiceBlockById(String blockid, String slideId) throws BlockDoesNotExistException;

    void updateTextBlock(TextBlock textBlock);

    IImageBlock getImageBlock(String imgBlockId);

    ITextBlock getTextBlock(String textBlockId);

    IChoiceBlock getChoiceBlock(String choiceBlockId);

    void updateImageBlock(IImageBlock imageBlock, byte[] image, String filename) throws ImageCouldNotBeStoredException;

    void updateImageBlock(IImageBlock imageBlock, IVSImage image);

    IChoiceBlock createChoiceBlock(String slideId, List<String> selectedChoices, Integer contentOrder,
            boolean showsAll);

    Integer findMaxContentOrder(String slideId);
    
    ISpaceBlock createSpaceBlock(String slideId, String title, Integer contentOrder, ISpace space);

    ISpaceBlock getSpaceBlock(String spaceBlockId);

    void updateSpaceBlock(SpaceBlock textBlock);

    /**
     * Delete a space block using an id and also decrease content order by 1 of all
     * the slide's block which are after this block
     * 
     * @param blockId - id of resource to be deleted. If the id is null then the
     *                functions returns nothing.
     * @param slideId - id of the slide in which the text block with blockId is
     *                present.
     * 
     */
    void deleteSpaceBlockById(String blockId, String slideId) throws BlockDoesNotExistException;
    
    void updateContentOrder(List<ContentBlock> contentBlockList) throws BlockDoesNotExistException;

    ISpace getCurrentSpaceForSpaceBlock(String blockId, String slideId) throws BlockDoesNotExistException;
}
