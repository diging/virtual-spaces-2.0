package edu.asu.diging.vspace.core.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SpaceContentBlockRepository;
import edu.asu.diging.vspace.core.factory.ISpaceBlockFactory;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceBlock;
import edu.asu.diging.vspace.core.model.impl.SpaceBlock;

@Service
public class SpaceBlockManager extends AbstractContentBlockManager<ISpaceBlock, SpaceContentBlockRepository> {

    @Autowired
    private ISpaceBlockFactory spaceBlockFactory;
    
    @Autowired
    private SpaceContentBlockRepository spaceBlockRepo;

    @Override
    protected SpaceContentBlockRepository getRepository() {
        return spaceBlockRepo;
    }

    /**
     * Creates a new space block for the specified slide.
     * 
     * @param slideId The ID of the slide
     * @param title The space block title
     * @param space The space to be displayed in the block
     * @return The created space block
     */
    public ISpaceBlock createSpaceBlock(String slideId, String title, ISpace space) {
        ISlide slide = slideManager.getSlide(slideId);
        Integer contentOrder = getNextContentOrder(slideId);
        
        ISpaceBlock spaceBlock = spaceBlockFactory.createSpaceBlock(slide, title, space);
        spaceBlock.setContentOrder(contentOrder);
        return spaceBlockRepo.save((SpaceBlock) spaceBlock);
    }

    /**
     * Saves/updates an existing space block.
     * 
     * @param spaceBlock The space block to save
     */
    public void saveSpaceBlock(ISpaceBlock spaceBlock) {
        spaceBlockRepo.save((SpaceBlock) spaceBlock);
    }
}
