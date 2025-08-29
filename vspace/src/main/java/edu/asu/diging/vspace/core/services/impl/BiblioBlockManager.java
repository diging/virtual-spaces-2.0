package edu.asu.diging.vspace.core.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.BiblioBlockRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;

@Service
public class BiblioBlockManager extends AbstractContentBlockManager<IBiblioBlock, BiblioBlockRepository> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BiblioBlockRepository biblioBlockRepo;

    @Override
    protected BiblioBlockRepository getRepository() {
        return biblioBlockRepo;
    }

    /**
     * Creates a new bibliography block for the specified slide.
     * 
     * @param slideId The ID of the slide
     * @param title The bibliography title
     * @param description The bibliography description
     * @return The created bibliography block
     */
    public IBiblioBlock createBiblioBlock(String slideId, String title, String description) {
        ISlide slide = slideManager.getSlide(slideId);
        Integer contentOrder = getNextContentOrder(slideId);
        
        IBiblioBlock biblioBlock = new BiblioBlock();
        biblioBlock.setDescription(description);
        biblioBlock.setBiblioTitle(title);
        biblioBlock.setSlide(slide);
        biblioBlock.setContentOrder(contentOrder);
        return biblioBlockRepo.save((BiblioBlock) biblioBlock);
    }

    /**
     * Deletes a bibliography block by ID.
     * Note: This method doesn't update content order like the other delete methods
     * because it appears to be used differently in the original implementation.
     * 
     * @param id The ID of the bibliography block to delete
     * @throws BlockDoesNotExistException if the block doesn't exist
     */
    public void deleteBiblioBlockById(String id) throws BlockDoesNotExistException {
        if (id == null) {
            logger.warn("Attempted to delete biblio block with null id.");
            return;
        }

        try {
            biblioBlockRepo.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new BlockDoesNotExistException("Biblio block with id " + id + " does not exist.", e);
        }
    }

    /**
     * Updates an existing bibliography block.
     * 
     * @param biblioBlock The bibliography block to update
     */
    public void updateBiblioBlock(IBiblioBlock biblioBlock) {
        biblioBlockRepo.save((BiblioBlock) biblioBlock);
    }
}
