package edu.asu.diging.vspace.core.model;

import java.util.List;
import java.util.Set;

import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;

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
    
    IVSImage getSpacelinkDefaultImage();
    
    void setSpacelinkDefaultImage(IVSImage spacelinkDefaultImage);
    
    IVSImage getModulelinkDefaultImage();
    
    void setModulelinkDefaultImage(IVSImage modulelinkDefaultImage);
    
    IVSImage getExternallinkDefaultImage();
    
    void setExternallinkDefaultImage(IVSImage externallinkDefaultImage);
    
    List<IVSImage> getDefaultImage();
     
    void setDefaultImage(List<IVSImage> defaultImage);

    List<IExhibitionLanguage> getLanguages();

}
