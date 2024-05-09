package edu.asu.diging.vspace.core.services;

import java.util.List;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.exception.VideoCouldNotBeStoredException;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ITextBlock;

import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.IVideoBlock;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IContentBlockManager {
    
    IBiblioBlock createBiblioBlock(String slideId, String title, String description, Integer contentOrder);

    List<IContentBlock> getAllContentBlocks(String slideId);

    ITextBlock createTextBlock(String slideId, String content, Integer contentOrder);

    CreationReturnValue createImageBlock(String slideId, byte[] image, String filename, Integer contentOrder) throws ImageCouldNotBeStoredException;
    
    CreationReturnValue createImageBlock(String slideId, IVSImage image, Integer contentOrder);
    
    public CreationReturnValue createVideoBlock(String slideId, byte[] video, Long size, String fileName, String url, Integer contentOrder, String title) throws VideoCouldNotBeStoredException;

    void deleteTextBlockById(String blockid, String slideId) throws BlockDoesNotExistException;
    
    void deleteBiblioBlockById(String blockid) throws BlockDoesNotExistException;

    void deleteImageBlockById(String blockid, String slideId) throws BlockDoesNotExistException;

    void deleteVideoBlockById(String blockId,String slideId) throws BlockDoesNotExistException;

    void deleteChoiceBlockById(String blockid, String slideId) throws BlockDoesNotExistException;

    void updateTextBlock(TextBlock textBlock);
    
    void updateBiblioBlock(BiblioBlock biblioBlock);

    IImageBlock getImageBlock(String imgBlockId);

    IVideoBlock getVideoBlock(String videoBlockId);

    ITextBlock getTextBlock(String textBlockId);
    
    IBiblioBlock getBiblioBlock(String biblioBlockId);

    IChoiceBlock getChoiceBlock(String choiceBlockId);

    void updateImageBlock(IImageBlock imageBlock, byte[] image, String filename) throws ImageCouldNotBeStoredException;

    void updateImageBlock(IImageBlock imageBlock, IVSImage image);

    void updateVideoBlock(IVideoBlock videoBlock, byte[] video, Long fileSize, String url, String filename, String title) throws VideoCouldNotBeStoredException;

    IChoiceBlock createChoiceBlock(String slideId, List<String> selectedChoices, Integer contentOrder, boolean showsAll);

    Integer findMaxContentOrder(String slideId);

    void updateContentOrder(List<ContentBlock> contentBlockList) throws BlockDoesNotExistException;

    void saveVideoBlock(IVideoBlock videoBlock);
}
