package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.IModule;

public interface IModuleManager {

	IModule storeModule(IModule module);
	
	IModule getModule(String id);

}