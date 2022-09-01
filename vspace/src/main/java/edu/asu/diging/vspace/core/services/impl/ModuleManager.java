package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.ModuleLinkRepository;
import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.data.display.ModuleLinkDisplayRepository;
import edu.asu.diging.vspace.core.exception.ModuleNotFoundException;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.ModuleStatus;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.impl.model.ModuleWithSpace;

@Transactional
@Service
public class ModuleManager implements IModuleManager {
    
    
    @Autowired
    private ModuleLinkRepository moduleLinkRepo;

    @Autowired
    private ModuleRepository moduleRepo;

    @Autowired
    private SlideRepository slideRepo;

    @Autowired
    private SequenceRepository sequenceRepo;
    
    @Autowired
    private SlideManager slideManager;
    
    @Autowired
    private ModuleLinkDisplayRepository moduleLinkDisplayRepo;
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.IModuleManager#storeModule(edu.asu.
     * diging.vspace.core.model.IModule)
     */
    @Override
    public IModule storeModule(IModule module) {
        return moduleRepo.save((Module) module);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.IModuleManager#getModule(java.lang.
     * String)
     */
    @Override
    public IModule getModule(String id) {
        Optional<Module> module = moduleRepo.findById(id);
        if (module.isPresent()) {
            return module.get();
        }
        return null;
    }       
    
    @Override
    public List<IModule> getAllModules() {
        List<IModule> modules = new ArrayList<>();
        moduleRepo.findAll().forEach(s -> modules.add(s));
        return modules;
    }

    @Override
    public List<ISlide> getModuleSlides(String moduleId) {
        return new ArrayList<>(slideRepo.findSlidesForModule(moduleId));
    }


    @Override
    public List<ISequence> getModuleSequences(String moduleId) {
        return new LinkedList<>(sequenceRepo.findSequencesForModule(moduleId));
    }
    
    @Override
    public ISequence checkIfSequenceExists(String moduleId, String sequenceId) {
        return sequenceRepo.findSequenceForModuleAndSequence(moduleId,sequenceId);
    }
    
    @Override
    public Page<IModule> findByNameOrDescription(Pageable requestedPage,String searchText) {
        return moduleRepo.findDistinctByNameContainingOrDescriptionContaining(requestedPage,searchText,searchText);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteModule(String moduleId) throws ModuleNotFoundException {
        if(moduleId == null) {
            return;
        }
        moduleLinkRepo.findModuleLinksByModuleId(moduleId).forEach(
                moduleLink -> {
                    moduleLinkDisplayRepo.deleteByLink(moduleLink);
                    moduleLinkRepo.deleteById(moduleLink.getId());
                });
        //delete all slides
        Optional<Module> moduleOptional = moduleRepo.findById(moduleId);
        if(moduleOptional.isPresent()) {
            Module module = moduleOptional.get();
            module.getSlides().forEach(
                    slide -> {
                        slideManager.deleteSlideById(slide.getId(), moduleId);
                    });
            getModuleSequences(moduleId).forEach(
                    sequence -> {
                        sequenceRepo.deleteById(sequence.getId());
                    });
        } else {
            throw new ModuleNotFoundException("Module not found");
        }
        moduleRepo.deleteById(moduleId);
    }
    
    
    public Page<IModule> findByNameOrDescriptionLinkedToSpace(Pageable requestedPage,String searchText){
              
        return moduleRepo.findDistinctByModuleStatusNameContainingOrDescriptionContainingLinkedToSpace(requestedPage, ModuleStatus.PUBLISHED,searchText,  searchText);
        
    }
}
