package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.model.IContentBlock;

public interface IGenericContentBlockManager<T extends IContentBlock> {

    T createContentBlock(String slideId);

    void updateContentBlock(T contentBlock);

    T getContentBlock(String blockId);

    void deleteContentBlock(String blockId, String slideId) throws BlockDoesNotExistException;

    void saveContentBlock(T contentBlock);
}
