package edu.asu.diging.vspace.core.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IContentBlockManager {

    IContentBlock createTextBlock(String slideId, String content);

    ITextBlock storeTextBlock(IContentBlock textBlock);

    IImageBlock storeImageBlock(IContentBlock imageBlock);

    CreationReturnValue createImageBlock(String slideId, byte[] image, String filename);
//
//    List<TextBlock> getTextBlocks(String slideId);
//
//    List<ImageBlock> getImageBlocks(String slideId);

    Set<IContentBlock> getAllContentBlocks(String slideId);

    IContentBlock getTextBlock(IContentBlock contentBlock);
    
    IContentBlock getImageBlock(IContentBlock contentBlock);

}
