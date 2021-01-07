package edu.asu.diging.vspace.core.services.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Service
public class StaffSearchManager implements IStaffSearchManager{

    @Autowired
    private ISpaceManager spaceManager;
    
    @Autowired
    private IModuleManager moduleManager;
    
    @Autowired
    private ISlideManager slideManager;
    
    @Autowired
    private TextContentBlockRepository textContentBlockRepo;
    
    @Override
    public HashSet<IVSpaceElement> getAllContainingElements(String searchString) {
        
        HashSet<IVSpaceElement> resultElements = new HashSet<>();
        
        resultElements.addAll(spaceManager.getSpacesContainingSearchedText(searchString));
        resultElements.addAll(moduleManager.getModulesContainingSearchedText(searchString));
        resultElements.addAll(slideManager.getSlidesContainingSearchedText(searchString));
        resultElements.addAll(textContentBlockRepo.getSearchedTextContainingSlides(searchString));
        return resultElements;
    }
    
    

}
