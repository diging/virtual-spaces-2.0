package edu.asu.diging.vspace.core.services.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SpaceTextBlockRepository;
import edu.asu.diging.vspace.core.data.display.SpaceTextBlockDisplayRepository;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.ISpaceTextBlockDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISpaceTextBlockFactory;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceTextBlock;
import edu.asu.diging.vspace.core.model.display.ISpaceTextBlockDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceTextBlockDisplay;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.SpaceTextBlock;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpaceTextBlockManager;

@Transactional
@Service
public class SpaceTextBlockManager implements ISpaceTextBlockManager{
    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private ISpaceTextBlockFactory spaceTextBlockFactory;

    @Autowired
    private ISpaceTextBlockDisplayFactory spaceTextBlockDisplayFactory;

    @Autowired
    private SpaceTextBlockRepository spaceTextBlockRepo;

    @Autowired
    private SpaceTextBlockDisplayRepository spaceTextBlockDisplayRepo;

    @Override
    public ISpaceTextBlockDisplay createTextBlock(String id, float positionX, float positionY, String text,
            float height, float width)
                    throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, SpaceDoesNotExistException {
        ISpace source = spaceManager.getSpace(id);
        ISpaceTextBlock textBlock=spaceTextBlockFactory.createSpaceTextBlock(text, source);
        ISpaceTextBlockDisplay spaceTextBlockDisplay = spaceTextBlockDisplayFactory.createSpaceTextBlockDisplay(textBlock,positionX,positionY,height, width);
        textBlock=spaceTextBlockRepo.save((SpaceTextBlock) textBlock);
        return spaceTextBlockDisplayRepo.save((SpaceTextBlockDisplay)spaceTextBlockDisplay);
    }

    @Override
    public List<ISpaceTextBlockDisplay> getSpaceTextBlockDisplays(String spaceId) {
        return spaceTextBlockDisplayRepo.findSpaceTextBlockDisplaysForSpace(spaceId);
    }
    
    @Override
    public void deleteTextBlock(String blockId) {
        Optional<SpaceTextBlock> spaceTextBlock = spaceTextBlockRepo.findById(blockId);
        if(spaceTextBlock.isPresent()) {
            spaceTextBlockDisplayRepo.deleteBySpaceTextBlock(spaceTextBlock.get());
            spaceTextBlockRepo.delete(spaceTextBlock.get());
        }
    }
    @Override
    public ISpaceTextBlockDisplay updateTextBlock(String id, Float positionX, Float positionY, String textBlockIdValueEdit,
            String textBlockDisplayId, String text, Float height, Float width) {
        
        Optional<SpaceTextBlock> spaceTextBlock = spaceTextBlockRepo.findById(textBlockIdValueEdit);
        if(spaceTextBlock.isPresent()) {
            ISpaceTextBlock spaceTextBlockEdit = spaceTextBlock.get();
            spaceTextBlockEdit.setText(text);
            Optional<SpaceTextBlockDisplay> spaceTextBlockDisplay = spaceTextBlockDisplayRepo.findById(textBlockDisplayId);
            if(spaceTextBlockDisplay.isPresent()) {
                ISpaceTextBlockDisplay spaceTextBlockDisplayEdit = spaceTextBlockDisplay.get();
                spaceTextBlockDisplayEdit.setHeight(height);
                spaceTextBlockDisplayEdit.setWidth(width);
                spaceTextBlockDisplayEdit.setPositionX(positionX);
                spaceTextBlockDisplayEdit.setPositionY(positionY);
                spaceTextBlockRepo.save((SpaceTextBlock)spaceTextBlockEdit);
                return spaceTextBlockDisplayRepo.save((SpaceTextBlockDisplay)spaceTextBlockDisplayEdit);
            }
        }
        return null;
    }
}
