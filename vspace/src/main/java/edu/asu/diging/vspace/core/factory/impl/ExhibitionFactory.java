package edu.asu.diging.vspace.core.factory.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IExhibitionFactory;
import edu.asu.diging.vspace.core.model.ExhibitionModes;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;

@Service
public class ExhibitionFactory implements IExhibitionFactory {

    public static final String EXH_PREVIEW = "EXH_PREVIEW_";

    /**
     * This method create an Exhibition with a previewId and its status has been set
     * to active by default. This previewId is generated randomly every time an
     * exhibition got created.
     * 
     * @return an Exhibition object after creation.
     */
    @Override
    public IExhibition createExhibition() {
        Exhibition exhibitionObj = new Exhibition();
        updatePreviewId(exhibitionObj);
        return exhibitionObj;
    }
    /**
     * Updates the exhibition object with preview id
     * 
     * @param exhibitionObj
     * @return
     */
    @Override
    public void updatePreviewId(Exhibition exhibitionObj) {
        UUID randomUUID = UUID.randomUUID();
        String randomString = randomUUID.toString().replaceAll("-", "");
        exhibitionObj.setPreviewId(EXH_PREVIEW + randomString.substring(0, 8));
        exhibitionObj.setMode(ExhibitionModes.ACTIVE);
    }

}
