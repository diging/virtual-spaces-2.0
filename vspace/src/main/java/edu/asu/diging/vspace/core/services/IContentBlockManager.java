package edu.asu.diging.vspace.core.services;

import java.util.Map;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IContentBlockManager {

    IContentBlock createTextBlock(String slideId, String content);

    IImageBlock storeImageBlock(IContentBlock imageBlock);

    CreationReturnValue createImageBlock(String slideId, byte[] image, String filename);

    Map<IContentBlock, String> getAllContentBlocks(String slideId);

}
