package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;

public interface IModuleManager {

    IModule storeModule(IModule module);

    IModule getModule(String id);
    
    List<IModule> getAllModules();

    List<ISlide> getModuleSlides(String moduleId);
    
    List<ISequence> getModuleSequences(String moduleId);

    ISequence checkIfSequenceExists(String moduleId, String sequenceId);

}