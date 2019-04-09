package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IModuleLinkDisplayFactory;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;

@Service
public class ModuleLinkDisplayFactory implements IModuleLinkDisplayFactory {

	@Override
	public IModuleLinkDisplay createModuleLinkDisplay(IModuleLink link) {
		IModuleLinkDisplay display = new ModuleLinkDisplay();
        display.setLink(link);
        return display;
	}
  
}
