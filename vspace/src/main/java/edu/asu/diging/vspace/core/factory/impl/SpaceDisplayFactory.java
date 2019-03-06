package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.factory.ISpaceDisplayFactory;
import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceDisplay;

@Component
public class SpaceDisplayFactory implements ISpaceDisplayFactory {

    public ISpaceDisplay createSpaceDisplay() {
        return new SpaceDisplay();
    }
}
