package edu.asu.diging.vspace.core.model;

public interface IDefaultImage {
    
    IVSImage getSpacelinkDefaultImage();
    void setSpacelinkDefaultImage(IVSImage spacelinkDefaultImage);
    IVSImage getModulelinkDefaultImage();
    void setModulelinkDefaultImage(IVSImage modulelinkDefaultImage);
    IVSImage getExternallinkDefaultImage();
    void setExternallinkDefaultImage(IVSImage externallinkDefaultImage);

}
