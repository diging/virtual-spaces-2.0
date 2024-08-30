package edu.asu.diging.vspace.core.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;
import edu.asu.diging.vspace.core.services.impl.model.SearchModuleResults;
import edu.asu.diging.vspace.core.services.impl.model.SearchSlideResults;
import edu.asu.diging.vspace.core.services.impl.model.SearchSlideTextBlockResults;

/**
 * This class has all the methods with the business logics for searching a word
 * in staff page.
 * 
 * @author Avirup Biswas
 *
 */
@Service
public class StaffSearchManager extends SearchManager implements IStaffSearchManager {

    @Autowired
    private ISpaceManager spaceManager;
    
    @Autowired
    private IModuleManager moduleManager;
    
    @Autowired
    private ISlideManager slideManager;
    
    @Autowired
    private TextContentBlockRepository textContentBlockRepo;
   
    @Override
    protected Page<ISpace> searchSpaces(Pageable requestedPageForSpace, String searchTerm) {
        return spaceManager.findBySpaceStatusAndNameOrDescription(requestedPageForSpace, null, searchTerm);
    } 

    @Override
    protected Page<IModule> searchModules(Pageable requestedPageForModule, String searchTerm) {
        return moduleManager.findByNameOrDescription(requestedPageForModule, searchTerm);
    }

    @Override
    protected Page<ISlide> searchSlides(Pageable requestedPageForSlide, String searchTerm) {
        return slideManager.findByNameOrDescription(requestedPageForSlide, searchTerm);
    }

    @Override
    protected Page<ISlide> searchSlideTexts(Pageable requestedPageForSlideText, String searchTerm) {
        return textContentBlockRepo.findWithNameOrDescription(requestedPageForSlideText,
                searchTerm);
    }

    @Override
    protected Page<IModule> updateModulePageWithSpaceInfo(Page<IModule> modulePage) {
        return modulePage;
    }


    @Override
    protected Page<ISlide> updateSlideTextPageWithSpaceInfo(Page<ISlide> slideTextPage) {       
        return slideTextPage;
    }


    @Override
    protected Page<ISlide> updateSlidePageWithSpaceInfo(Page<ISlide> slidePage) {
        return slidePage;
    }
}
