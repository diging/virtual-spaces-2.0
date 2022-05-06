package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IModuleFactory;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.web.staff.forms.ModuleForm;

@Service
public class ModuleFactory implements IModuleFactory {

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.factory.impl.IModuleFactory#createModule(edu.asu.diging.vspace.web.staff.forms.ModuleForm)
	 */
	@Override
	public IModule createModule(ModuleForm form) {
		IModule module = new Module();
		module.setName(form.getName());
		module.setDescription(form.getDescription());
		return module;
	}
}
