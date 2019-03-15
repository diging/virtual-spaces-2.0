package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.ITextBlock;

public interface IContentBlockManager {

    IContentBlock createTextBlock(String slideId, String content);

    void createImageBlock(String slideId, String value);

    ITextBlock storeTextBlock(IContentBlock textBlock);

}
