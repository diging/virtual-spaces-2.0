package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

public class StaffSearchManager implements IStaffSearchManager{

    @Autowired
    private SpaceManager spaceManager;
    
    @Autowired
    private ModuleManager moduleManager;
    
    @Autowired
    private SlideManager slideManager;
    
    @Override
    public List<IVSpaceElement> getAllContainingElements(String searchString) {
        
        List<IVSpaceElement> resultElements = new ArrayList<>();
        
        resultElements.addAll(spaceManager.getSpacesContainingSearchedText(searchString));
        resultElements.addAll(moduleManager.getModulesContainingSearchedText(searchString));
        //resultElements.addAll(moduleManager.get);
        
        return resultElements;
    }
    
    

}
