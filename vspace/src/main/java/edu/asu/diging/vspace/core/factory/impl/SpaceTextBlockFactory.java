package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ISpaceTextBlockFactory;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceTextBlock;
import edu.asu.diging.vspace.core.model.impl.SpaceTextBlock;

@Service
public class SpaceTextBlockFactory implements ISpaceTextBlockFactory {

    @Override
    public ISpaceTextBlock createSpaceTextBlock(String text, ISpace space) {
        ISpaceTextBlock textBlock = new SpaceTextBlock();
        textBlock.setText(text);
        textBlock.setSpace(space);
        return textBlock;
    }

}