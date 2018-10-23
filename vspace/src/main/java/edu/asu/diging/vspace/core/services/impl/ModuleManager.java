package edu.asu.diging.vspace.core.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.services.IModuleManager;

@Service
public class ModuleManager implements IModuleManager {

	@Autowired
	private ModuleRepository moduleRepo;
	
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.services.impl.IModuleManager#storeModule(edu.asu.diging.vspace.core.model.IModule, java.lang.String)
	 */
	@Override
	public IModule storeModule(IModule module) {
		return moduleRepo.save((Module)module);
	}
	
	@Override
    public IModule getModule(String id) {
        Optional<Module> module = moduleRepo.findById(id);
        if (module != null && module.isPresent()) {
            return module.get();
        }
        return null;
    }
}
