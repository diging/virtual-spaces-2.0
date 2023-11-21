package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.factory.ISlideDisplayFactory;
import edu.asu.diging.vspace.core.model.display.ISlideDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SlideDisplay;

@Component
public class SlideDisplayFactory implements ISlideDisplayFactory{
    
    public ISlideDisplay createSlideDisplay() {
        return new SlideDisplay();
    }

}
