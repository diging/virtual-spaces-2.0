package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.services.IModuleManager;

@Transactional
@Service
public class ModuleManager implements IModuleManager {

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
    public HashMap<ISlide, Integer> getModuleSlides(String moduleId) {
        HashMap<ISlide, Integer> slidesWithSeq = new HashMap<ISlide, Integer>();        
        for(ISlide slide: slideRepo.findSlidesForModule(moduleId)) {
            slidesWithSeq.put(slide, slideRepo.countSequencesForSlide(slide.getId()).size());
        }
        return slidesWithSeq;
    }


    @Override
    public List<ISequence> getModuleSequences(String moduleId) {
        return new LinkedList<>(sequenceRepo.findSequencesForModule(moduleId));
    }

}
