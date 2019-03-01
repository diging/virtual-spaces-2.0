package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IExhibitionFactory;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;

@Service
public class ExhibitionFactory implements IExhibitionFactory {

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.Exhibition
     */
    @Override
    public IExhibition createExhibition() {
        return new Exhibition();
    }

}
