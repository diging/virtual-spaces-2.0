package edu.asu.diging.vspace.core.model;

import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;

public interface IExternalLink extends ILink<ExternalLinkValue> {

    ISpace getSpace();

    void setSpace(ISpace space);

    String getExternalLink();

    void setExternalLink(String externalLink);

}
