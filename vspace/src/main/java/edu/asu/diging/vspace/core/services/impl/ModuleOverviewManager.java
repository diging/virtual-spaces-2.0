package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.exception.ModuleNotFoundException;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.services.ILanguageService;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.IModuleOverviewManager;
import edu.asu.diging.vspace.core.services.impl.model.ModuleOverview;
import edu.asu.diging.vspace.core.services.impl.model.SequenceOverview;
import edu.asu.diging.vspace.core.services.impl.model.SlideOverview;

@Service
public class ModuleOverviewManager implements IModuleOverviewManager {
    
    @Autowired
    private IModuleManager moduleManager;
    
    @Autowired
    private ILanguageService languageService;
    
    public ModuleOverview getModuleOverview(String moduleId) throws ModuleNotFoundException{
        return getModuleOverview(moduleId, languageService.getDefaultLanguageCode(), languageService.getDefaultLanguageCode());
    }
    
    public ModuleOverview getModuleOverview(String moduleId, String selectedLanguage, String defaultLanguageCode) throws ModuleNotFoundException{
        IModule module = moduleManager.getModule(moduleId);
        if(module==null) {
            throw new ModuleNotFoundException("Module not found");
        }
        ISequence startSequence = module.getStartSequence();
        
        List<ISequence> sequences = moduleManager.getModuleSequences(moduleId);
        
        SequenceOverview sequenceOverviewNode = createSequenceOverviewNode(startSequence, selectedLanguage, defaultLanguageCode);
        List<SequenceOverview> otherSequences = new ArrayList<SequenceOverview>();
        for(ISequence sequence : sequences) {
            
            if(sequence != startSequence) {
                otherSequences.add(createSequenceOverviewNode(sequence, selectedLanguage, defaultLanguageCode));
            }
        }
        
        ModuleOverview moduleOverview = new ModuleOverview();
        moduleOverview.setStartSequence(sequenceOverviewNode);
        moduleOverview.setOtherSequences(otherSequences);
        return moduleOverview;
    }
    
    /**
     * This method is used to fetch all Sequences which belong to a module and 
     * convert this into a SequenceOverview node. The SequenceOverviewNode is added to 
     * ModuleOverview
     * @return ModuleOverview which contains the module and the list of sequences and its slides
     */   
    private SequenceOverview createSequenceOverviewNode(ISequence sequence) {
        return createSequenceOverviewNode(sequence, languageService.getDefaultLanguageCode(), languageService.getDefaultLanguageCode());
    }
    
    private SequenceOverview createSequenceOverviewNode(ISequence sequence, String selectedLanguage, String defaultLanguageCode) {
        if(sequence==null) {
            return null;
        }
        
        SequenceOverview sequenceOverview = new SequenceOverview();
        sequenceOverview.setName(sequence.getName());
        sequenceOverview.setId(sequence.getId());
        List<SlideOverview> slideOverviews = createSlideOverviewNode(sequence.getSlides(), selectedLanguage, defaultLanguageCode);
        sequenceOverview.setSlideOverviews(slideOverviews);
        return sequenceOverview;  
    }
    
    private List<SlideOverview> createSlideOverviewNode(List<ISlide> slides){
        return createSlideOverviewNode(slides, languageService.getDefaultLanguageCode(), languageService.getDefaultLanguageCode());
    }
    
    private List<SlideOverview> createSlideOverviewNode(List<ISlide> slides, String selectedLanguage, String defaultLanguageCode){
        List<SlideOverview> slideOverviews = new ArrayList<SlideOverview>();
        for(ISlide slide : slides) {
            SlideOverview slideOverview = new SlideOverview(); 
            slideOverview.setId(slide.getId());
            String localizedName = slide.getLocalizedName(selectedLanguage, defaultLanguageCode);
            slideOverview.setName(localizedName != null && !localizedName.isEmpty() ? localizedName : slide.getName());
            if(slide instanceof BranchingPoint) {
                slideOverview.setBranchingPoint(true);
                List<IChoice> sequenceChoices = ((BranchingPoint)slide).getChoices();
                List<String> slideOverviewSequenceNames = new ArrayList<String>();
                sequenceChoices.stream().forEach(sequenceChoice -> 
                    slideOverviewSequenceNames.add(sequenceChoice.getSequence().getId()));
                slideOverview.setChoiceSequenceIds(slideOverviewSequenceNames);
            } 
            slideOverviews.add(slideOverview);          
        } 
        return slideOverviews;
    }
 
}
