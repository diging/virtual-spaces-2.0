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
    
    
    /**
     * This method is used to fetch the staff side search. The search text id searched 
     * in space, module and slide tables.
     * @param searchString - This is the string entered in the search box.
     * @return resultElements - Has all relevant VSpaceElements with the search texts.
     */
    @Override
    public HashSet<IVSpaceElement> getAllSearchedElements(String searchString) {
        
        HashSet<IVSpaceElement> resultElements = new HashSet<>();
        
        resultElements.addAll(spaceManager.findInNameOrDescriptionOfSpaces(searchString));
        resultElements.addAll(moduleManager.findInNameOrDescriptionOfModule(searchString));
        resultElements.addAll(slideManager.findInNameOrDescriptionOfSlide(searchString));
        resultElements.addAll(textContentBlockRepo.findInNameOrDescription(searchString));
        return resultElements;
    }
    
    
}
