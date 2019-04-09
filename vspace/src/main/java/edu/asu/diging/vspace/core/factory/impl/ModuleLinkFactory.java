package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IModuleLinkFactory;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;

@Service
public class ModuleLinkFactory implements IModuleLinkFactory{

	@Override
	public IModuleLink createModuleLink(String title, ISpace space) {
		IModuleLink link = new ModuleLink();
        link.setName(title);
        link.setSpace(space);
        return link;
	}

}
