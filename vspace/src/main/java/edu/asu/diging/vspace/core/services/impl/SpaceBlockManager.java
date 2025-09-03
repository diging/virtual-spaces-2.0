package edu.asu.diging.vspace.core.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SpaceContentBlockRepository;
import edu.asu.diging.vspace.core.factory.ISpaceBlockFactory;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceBlock;
import edu.asu.diging.vspace.core.model.impl.SpaceBlock;
import edu.asu.diging.vspace.core.services.ISpaceBlockManager;

@Service
public class SpaceBlockManager extends GenericContentBlockManager<ISpaceBlock, SpaceContentBlockRepository> 
        implements ISpaceBlockManager {

    @Autowired
    private ISpaceBlockFactory spaceBlockFactory;
    
    @Autowired
    private SpaceContentBlockRepository spaceBlockRepo;

    @Override
    protected SpaceContentBlockRepository getRepository() {
        return spaceBlockRepo;
    }

    @Override
    public ISpaceBlock createContentBlock(String slideId) {
        return createSpaceBlock(slideId, "Default Space", null);
    }

    @Override
    public ISpaceBlock createSpaceBlock(String slideId, String title, ISpace space) {
        ISlide slide = slideManager.getSlide(slideId);
        Integer contentOrder = getNextContentOrder(slideId);
        
        ISpaceBlock spaceBlock = spaceBlockFactory.createSpaceBlock(slide, title, space);
        spaceBlock.setContentOrder(contentOrder);
        return spaceBlockRepo.save((SpaceBlock) spaceBlock);
    }

    @Override
    public void updateContentBlock(ISpaceBlock spaceBlock) {
        saveSpaceBlock(spaceBlock);
    }

    @Override
    public void saveSpaceBlock(ISpaceBlock spaceBlock) {
        spaceBlockRepo.save((SpaceBlock) spaceBlock);
    }

    @Override
    public void saveContentBlock(ISpaceBlock spaceBlock) {
        saveSpaceBlock(spaceBlock);
    }
}
