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
import edu.asu.diging.vspace.core.services.impl.model.SearchSpaceResults;

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

    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the slides corresponding to the page number
     * specified in the input parameter(spacePagenum) whose text block contains the
     * search string
     * 
     * @param slideTextPagenum current page number sent as request parameter in the
     *                         URL.
     * @param searchTerm       This is the search string which is being searched.
     */
    public SearchSlideTextBlockResults searchForSlideText(String slideTextPagenum, String searchTerm) {

        return searchSlideTextBlockAndProcessResults(searchTerm, Integer.parseInt(slideTextPagenum));

    }

    
    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the spaces corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string.
     * 
     * @param spacePagenum current page number sent as request parameter in the URL.
     * @param searchTerm   This is the search string which is being searched.
     */
    public SearchSpaceResults searchForSpace(String spacePagenum, String searchTerm) {      
        return searchSpacesAndProcessResults(searchTerm, Integer.parseInt(spacePagenum));
    }


    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the module corresponding to the page number
     * specified in the input parameter(spacePagenum) whose name or description
     * contains the search string.
     * 
     * @param modulePagenum current page number sent as request parameter in the
     *                      URL.
     * @param searchTerm    This is the search string which is being searched.
     */
    @Override
    public SearchModuleResults searchForModule(String modulePagenum, String searchTerm) {
        
        return searchModulesAndProcessResults(searchTerm, Integer.parseInt(modulePagenum));

    }


    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the slides corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string.
     * 
     * @param slidePagenum current page number sent as request parameter in the URL.
     * @param searchTerm   This is the search string which is being searched.
     */
    @Override
    public SearchSlideResults searchForSlide(String slidePagenum, String searchTerm) {
        return searchSlidesAndProcessResults(searchTerm, Integer.parseInt(slidePagenum));
    }
    
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
    protected List<IModule> updateModulePageWithSpaceInfo(Page<IModule> modulePage) {
        return modulePage.getContent();
    }


    @Override
    protected List<ISlide> updateSlideTextPageWithSpaceInfo(Page<ISlide> slideTextPage) {
       
        return slideTextPage.getContent();
    }


    @Override
    protected List<ISlide> updateSlidePageWithSpaceInfo(Page<ISlide> slidePage) {
       
        return slidePage.getContent();
    }
}
