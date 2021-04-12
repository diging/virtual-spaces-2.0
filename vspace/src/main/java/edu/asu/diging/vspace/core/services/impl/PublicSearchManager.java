package edu.asu.diging.vspace.core.services.impl;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.IPublicSearchManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Service
public class PublicSearchManager implements IPublicSearchManager{

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
     * @param searchText - This is the string entered in the search box.
     * @return resultElements - Has all relevant VSpaceElements with the search texts.
     */
    @Override
    public HashSet<IVSpaceElement> getAllSearchedElements(String searchText) {
        
        HashSet<IVSpaceElement> resultElements = new HashSet<>();
        
        resultElements.addAll(spaceManager.findInNameOrDescriptionOfPublicSpaces(searchText));
        resultElements.addAll(moduleManager.findInNameOrDescriptionOfPublicModule(searchText));
        resultElements.addAll(slideManager.findInNameOrDescriptionOfPublicSlide(searchText));
        resultElements.addAll(textContentBlockRepo.findInNameOrDescriptionOfPublic(searchText));
        return resultElements;
    }
    
    
}