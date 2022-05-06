package edu.asu.diging.vspace.core.model;

public interface IModuleLink extends ILink<IModule> {

    IModule getModule();

    void setModule(IModule module);
}