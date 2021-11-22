package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ModuleLinkRepository;
import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
import edu.asu.diging.vspace.core.services.IModuleManager;

@Transactional
@Service
public class ModuleManager implements IModuleManager {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ModuleLinkRepository moduleLinkRepo;

    @Autowired
    private ModuleRepository moduleRepo;

    @Autowired
    private SlideRepository slideRepo;

    @Autowired
    private SequenceRepository sequenceRepo;
    
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
    
    @Transactional
    @Override
    public void deleteModule(String id) {
        logger.info("id is {}", id);
        if (id != null) {
            List<IModuleLink> moduleLinks = moduleLinkRepo.findModuleLinkByModuleId(id);
            for (IModuleLink moduleLink: moduleLinks) {
                 logger.info("Module link id deleting is {}", moduleLink.getId());
                 moduleLink.setModule(null);
            }
           moduleLinkRepo.saveAll(Iterable<S> moduleLinks);
           logger.info("deleting module with id {} now", id);
            if(moduleRepo.findById(id).get()!=null) {
                logger.info("Found the module {}", moduleRepo.findById(id).get().getId());
            }
            moduleRepo.deleteById(id);
            
            logger.info("deleting module");
            
        }
        return ;
    }
}
