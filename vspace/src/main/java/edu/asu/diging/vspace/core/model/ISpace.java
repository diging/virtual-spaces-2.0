package edu.asu.diging.vspace.core.model;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.SpaceStatus;

public interface ISpace extends IVSpaceElement {

    List<ISpaceLink> getSpaceLinks();

    void setSpaceLinks(List<ISpaceLink> spaceLinks);

    List<IModuleLink> getModuleLinks();

    void setModuleLinks(List<IModuleLink> moduleLinks);

    List<IExternalLink> getExternalLinks();

    void setExternalLinks(List<IExternalLink> externalLinks);

    IVSImage getImage();

    void setImage(IVSImage image);

    SpaceStatus getSpaceStatus();

    void setSpaceStatus(SpaceStatus status);

    boolean isShowUnpublishedLinks();
    
    boolean isHideIncomingLinks();

    void setShowUnpublishedLinks(Boolean showUnpublishedLinks);
    
    void setHideIncomingLinks(boolean hideIncomingLinks);
    
    List<ILocalizedtext> getSpaceNames();
    
    void setSpaceNames(List<ILocalizedtext> spaceNames);
    
    void setSpaceDescriptions(List<ILocalizedtext> spaceDescriptions);
    
    List<ILocalizedtext> getSpaceDescriptions() ;

}