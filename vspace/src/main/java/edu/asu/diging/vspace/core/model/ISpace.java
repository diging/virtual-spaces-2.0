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

    void setShowUnpublishedLinks(Boolean showUnpublishedLinks);
    
    // VSPC-89 Ashmi changes
    boolean isShowLinksToUnpublishedSpaces();
    
    void setShowLinksToUnpublishedSpaces(Boolean showLinksToUnpublishedSpaces);

}