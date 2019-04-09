package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IContentBlockManager {

    IContentBlock createTextBlock(String slideId, String content);

    CreationReturnValue createImageBlock(String slideId, byte[] image, String filename);

    List<IContentBlock> getAllContentBlocks(String slideId);

}
