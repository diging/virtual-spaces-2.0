package edu.asu.diging.vspace.core.model;

public interface IExternalLink extends IVSpaceElement {

    ISpace getSpace();

    void setSpace(ISpace space);

    String getExternalLink();

    void setExternalLink(String externallink);

}
