package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface IExhibition extends IVSpaceElement {

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISpacee#getSpace()
     */
    ISpace getStartSpace();

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.ISpacee#setSpace(edu.asu.diging.vspace.
     * core.model.impl.Space)
     */
    void setStartSpace(ISpace space);

    void setTitle(String title);

    String getTitle();

    boolean isAboutPageConfigured();

    void setAboutPageConfigured(boolean aboutPageConfigured);

    IVSImage getSpaceLinkDefaultImage();

    void setSpaceLinkDefaultImage(IVSImage spaceLinkDefaultImage);
    
    void deleteSpaceLinkDefaultImage();

    IVSImage getModuleLinkDefaultImage();

    void setModuleLinkDefaultImage(IVSImage moduleLinkDefaultImage);
    
    void deleteModuleLinkDefaultImage();

    IVSImage getExternalLinkDefaultImage();

    void setExternalLinkDefaultImage(IVSImage externalLinkDefaultImage);
    
    void deleteExternalLinkDefaultImage();

    List<IExhibitionLanguage> getLanguages();
    
    ExhibitionModes getMode();
    
    void setMode(ExhibitionModes mode);
    
    String getPreviewId();
    
    void setPreviewId(String previewId);

    void setCustomMessage(String customMessage);

}
