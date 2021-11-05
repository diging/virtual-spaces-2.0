package edu.asu.diging.vspace.core.factory.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ISpaceBlockFactory;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceBlock;
import edu.asu.diging.vspace.core.model.impl.SpaceBlock;

@Service
public class SpaceBlockFactory implements ISpaceBlockFactory {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public ISpaceBlock createSpaceBlock(ISlide slide, String title, ISpace space) {
        ISpaceBlock spaceBlock = new SpaceBlock();
        spaceBlock.setTitle(title);
        spaceBlock.setSlide(slide);
        spaceBlock.setSpace(space);
        logger.info("space block is created with values {} {} {}", spaceBlock.getSlide().getContents(), spaceBlock.getSlide().getId(), spaceBlock.getSpace().getId());;
        return spaceBlock;
    }
}
