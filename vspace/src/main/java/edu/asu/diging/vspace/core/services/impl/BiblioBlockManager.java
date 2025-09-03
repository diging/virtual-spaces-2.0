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
import edu.asu.diging.vspace.core.services.IBiblioBlockManager;

@Service
public class BiblioBlockManager extends GenericContentBlockManager<IBiblioBlock, BiblioBlockRepository> 
        implements IBiblioBlockManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BiblioBlockRepository biblioBlockRepo;

    @Override
    protected BiblioBlockRepository getRepository() {
        return biblioBlockRepo;
    }

    @Override
    public IBiblioBlock createContentBlock(String slideId) {
        return createBiblioBlock(slideId, "Default Bibliography", "Default Description");
    }

    @Override
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

    @Override
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

    @Override
    public void updateContentBlock(IBiblioBlock biblioBlock) {
        updateBiblioBlock((BiblioBlock) biblioBlock);
    }

    @Override
    public void updateBiblioBlock(BiblioBlock biblioBlock) {
        biblioBlockRepo.save(biblioBlock);
    }

    @Override
    public void saveContentBlock(IBiblioBlock biblioBlock) {
        biblioBlockRepo.save((BiblioBlock) biblioBlock);
    }
}
