package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ISpaceBlockFactory;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceBlock;
import edu.asu.diging.vspace.core.model.impl.SpaceBlock;

@Service
public class SpaceBlockFactory implements ISpaceBlockFactory {
    
    @Override
    public ISpaceBlock createSpaceBlock(ISlide slide, String title, ISpace space) {
        ISpaceBlock spaceBlock = new SpaceBlock();
        spaceBlock.setTitle(title);
        spaceBlock.setSlide(slide);
        spaceBlock.setSpace(space);
        return spaceBlock;
    }
}
