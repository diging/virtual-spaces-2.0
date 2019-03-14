package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.IContentBlock;

public interface IContentBlockManager {

    IContentBlock createTextBlock(String slideId, String text);

}
