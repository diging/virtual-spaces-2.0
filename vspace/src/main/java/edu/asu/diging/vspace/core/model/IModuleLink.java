package edu.asu.diging.vspace.core.model;

public interface IModuleLink extends ILLink<IModule> {

    IModule getModule();

    void setModule(IModule module);
}