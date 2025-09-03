package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;

public interface IBiblioBlockManager extends IGenericContentBlockManager<IBiblioBlock> {

    IBiblioBlock createBiblioBlock(String slideId, String title, String description);

    void deleteBiblioBlockById(String id) throws BlockDoesNotExistException;

    void updateBiblioBlock(BiblioBlock biblioBlock);
}
