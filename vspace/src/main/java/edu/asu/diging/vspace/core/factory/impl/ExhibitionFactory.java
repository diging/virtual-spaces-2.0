package edu.asu.diging.vspace.core.factory.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IExhibitionFactory;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;

@Service
public class ExhibitionFactory implements IExhibitionFactory {

    public static final String EXH_PREVIEW = "EXH_PREVIEW_";

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.Exhibition
     */
    @Override
    public IExhibition createExhibition() {
        Exhibition exhibitionObj = new Exhibition();
        UUID randomUUID = UUID.randomUUID();
        String randomString = randomUUID.toString().replaceAll("-", "");
        exhibitionObj.setPreviewId(EXH_PREVIEW + randomString.substring(0, 8));
        return exhibitionObj;
    }

}
