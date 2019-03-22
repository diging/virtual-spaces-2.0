package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IContentBlockManager {

    IContentBlock createTextBlock(String slideId, String content);

    ITextBlock storeTextBlock(IContentBlock textBlock);

    IImageBlock storeImageBlock(IContentBlock imageBlock);

    CreationReturnValue createImageBlock(String slideId, byte[] image, String filename);

}
