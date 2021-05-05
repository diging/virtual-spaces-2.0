package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IBiblioBlockFactory;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.ISlide;

@Service
public class BiblioBlockFactory implements IBiblioBlockFactory {

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.IBiblioBlockFactory#createBiblioBlock(
     * java.lang.String, )
     */
    @Override
    public IBiblioBlock createBiblioBlock(ISlide slide, IBiblioBlock biblioBlockData) {
        return biblioBlockData;
    }

}
