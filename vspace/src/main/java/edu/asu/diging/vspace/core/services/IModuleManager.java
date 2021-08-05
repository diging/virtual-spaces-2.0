package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.impl.Module;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IModuleManager {

    IModule storeModule(IModule module);

    IModule getModule(String id);
    
    List<IModule> getAllModules();

    List<ISlide> getModuleSlides(String moduleId);
    
    List<ISequence> getModuleSequences(String moduleId);

    ISequence checkIfSequenceExists(String moduleId, String sequenceId);
    
    Page<Module> findByNameOrDescription(Pageable requestedPage,String searchText);

    int findByNameOrDescription(String searchTerm);
}