package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.IModule;

public interface IModuleManager {

    IModule storeModule(IModule module);

    IModule getModule(String id);
    
    List<IModule> getAllModules();

}