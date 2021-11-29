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
    
    @Autowired
    private SlideManager slideManager;
    
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
    
    @Override
    public void deleteModule(String moduleId) {
        logger.info("module id to delete is {}", moduleId);
        List<ModuleLink> moduleLinksToPersist = new ArrayList<ModuleLink>();
        if (moduleId != null) {
            List<IModuleLink> moduleLinks = moduleLinkRepo.findModuleLinkByModuleId(moduleId);
            for (IModuleLink moduleLink: moduleLinks) {
                 logger.info("Module link id deleting is {}", moduleLink.getId());
                 moduleLink.setModule(null); 
                 moduleLinksToPersist.add((ModuleLink)moduleLink);
        }
        moduleLinkRepo.saveAll(moduleLinksToPersist);
        logger.info("deleting module with id {} now", moduleId);
        //delete all slides
        Optional<Module> moduleOptional = moduleRepo.findById(moduleId);
        if(moduleOptional.isPresent()) {
            Module module = moduleOptional.get();
            List<ISlide> slides = module.getSlides();
            for(ISlide slide:slides) {
                slideManager.deleteSlideById(slide.getId(), moduleId);
            }
            logger.info("starting to delete sequences");
            List<ISequence> sequences = getModuleSequences(moduleId);
            for (ISequence sequence:sequences) {
                logger.info("deleting sequence {}", sequence.getId());
                sequenceRepo.deleteById(sequence.getId());
                
            }
            logger.info("finished deleting sequences");
                    
        }
        moduleRepo.deleteById(moduleId);
        
        }
        return ;
    }
}
