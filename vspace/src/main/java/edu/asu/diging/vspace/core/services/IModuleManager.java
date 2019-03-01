package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IModuleManager {

    IModule storeModule(IModule module);

    IModule getModule(String id);

    CreationReturnValue storeSlide(ISlide slide, byte[] image, String filename);

    List<ISlide> getModuleSlides(String moduleId);

    ISlide getSlide(String slideId);

}