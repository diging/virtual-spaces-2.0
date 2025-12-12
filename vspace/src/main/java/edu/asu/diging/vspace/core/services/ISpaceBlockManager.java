package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceBlock;

public interface ISpaceBlockManager extends IGenericContentBlockManager<ISpaceBlock> {

    ISpaceBlock createSpaceBlock(String slideId, String title, ISpace space);

    void saveSpaceBlock(ISpaceBlock spaceBlock);
}
