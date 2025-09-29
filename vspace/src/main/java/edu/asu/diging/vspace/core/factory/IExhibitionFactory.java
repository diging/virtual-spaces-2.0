package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;

public interface IExhibitionFactory {

    IExhibition createExhibition();

    /**
     * Updates the exhibition object with preview id
     * 
     * @param exhibitionObj
     * @return
     */
    void updatePreviewId(IExhibition exhibitionObj);

}
