package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface ISpace extends ITVSpaceElement {

    List<ISpaceLink> getSpaceLinks();

    void setSpaceLinks(List<ISpaceLink> spaceLinks);

    List<IModuleLink> getModuleLinks();

    void setModuleLinks(List<IModuleLink> moduleLinks);

    List<IExternalLink> getExternalLinks();

    void setExternalLinks(List<IExternalLink> externalLinks);

    IVSImage getImage();

    void setImage(IVSImage image);

}