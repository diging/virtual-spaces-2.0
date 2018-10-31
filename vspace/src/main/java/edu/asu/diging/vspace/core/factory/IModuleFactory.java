package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.web.staff.forms.ModuleForm;

public interface IModuleFactory {

    IModule createModule(ModuleForm form);

}