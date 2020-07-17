package edu.asu.diging.vspace.core.model;

import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;

public interface ILLink<T extends ILink> extends ILink{

    ExternalLinkValue setExternalLink(ExternalLinkValue target);

    void setTargetSpace(IVSpaceElement target);

    void setTargetModule(IVSpaceElement target);
   
}
