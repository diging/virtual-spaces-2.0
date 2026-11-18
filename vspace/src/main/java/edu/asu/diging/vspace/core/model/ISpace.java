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
    
    List<ILocalizedText> getSpaceNames();

    /**
     * Returns the name of this space in the language with the given code, or the
     * default name if there is no name in that language.
     */
    String getNameForLanguage(String languageCode);
    
    void setSpaceNames(List<ILocalizedText> spaceNames);
    
    void setSpaceDescriptions(List<ILocalizedText> spaceDescriptions);
    
    List<ILocalizedText> getSpaceDescriptions() ;

}