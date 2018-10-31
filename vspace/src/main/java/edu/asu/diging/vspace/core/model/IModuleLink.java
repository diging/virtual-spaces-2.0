package edu.asu.diging.vspace.core.model;

public interface IModuleLink extends IVSpaceElement {

    ISpace getSpace();

    void setSpace(ISpace space);

    IModule getModule();

    void setModule(IModule module);

}