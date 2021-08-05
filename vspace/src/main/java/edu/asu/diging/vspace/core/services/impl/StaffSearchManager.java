package edu.asu.diging.vspace.core.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

/**
 * This class has all the methods with the business logics for searching a word
 * in staff page.
 * 
 * @author Avirup Biswas
 *
 */
@Service
public class StaffSearchManager implements IStaffSearchManager {

    @Value("${page_size}")
    private int pageSize;

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private TextContentBlockRepository textContentBlockRepo;

    /**
     * Method to return the requested spaces whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1. if page<1, 1st page is
     *                   returned, if page>total pages,last page is returned
     * @return set of spaces whose name or description contains the search string in
     *         the requested page.
     */

    @Override
    public Page<Space> searchInSpaces(String searchTerm, int page) {
        if (page < 1) {
            page = 1;
        }
        Pageable requestedPageForSpace = PageRequest.of(page - 1, pageSize);
        Page<Space> spacePage = spaceManager.findByNameOrDescription(requestedPageForSpace, searchTerm);
        int totalSpacePage = spacePage.getTotalPages();
        /* if page>total pages,last page is returned */
        if (page > totalSpacePage) {
            totalSpacePage = totalSpacePage == 0 ? 1 : totalSpacePage;
            requestedPageForSpace = PageRequest.of(totalSpacePage - 1, pageSize);
            spacePage = spaceManager.findByNameOrDescription(requestedPageForSpace, searchTerm);
        }
        return spacePage;
    }

    /**
     * Method to return the requested modules whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.if page<1, 1st page is
     *                   returned, if page>total pages,last page is returned
     * @return set of modules whose name or description contains the search string
     *         in the requested page.
     */
    @Override
    public Page<Module> searchInModules(String searchTerm, int page) {
        if (page < 1) {
            page = 1;
        }
        Pageable requestedPageForModule = PageRequest.of(page - 1, pageSize);
        Page<Module> modulePage = moduleManager.findByNameOrDescription(requestedPageForModule, searchTerm);
        int totalModulePage = modulePage.getTotalPages();
        /* if page>total pages,last page is returned */
        if (page > totalModulePage) {
            totalModulePage = totalModulePage == 0 ? 1 : totalModulePage;
            requestedPageForModule = PageRequest.of(totalModulePage - 1, pageSize);
            modulePage = moduleManager.findByNameOrDescription(requestedPageForModule, searchTerm);
        }
        return modulePage;
    }

    /**
     * Method to return the requested slides whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.if page<1, 1st page is
     *                   returned, if page>total pages,last page is returned
     * @return set of slides whose name or description contains the search string in
     *         the requested page.
     */
    @Override
    public Page<Slide> searchInSlides(String searchTerm, int page) {
        if (page < 1) {
            page = 1;
        }
        Pageable requestedPageForSlide = PageRequest.of(page - 1, pageSize);
        Page<Slide> slidePage = slideManager.findByNameOrDescription(requestedPageForSlide, searchTerm);
        int totalSlidePage = slidePage.getTotalPages();
        /* if page>total pages,last page is returned */
        if (page > totalSlidePage) {
            totalSlidePage = totalSlidePage == 0 ? 1 : totalSlidePage;
            requestedPageForSlide = PageRequest.of(totalSlidePage - 1, pageSize);
            slidePage = slideManager.findByNameOrDescription(requestedPageForSlide, searchTerm);
        }
        return slidePage;
    }

    /**
     * Method to return the requested slides whose text blocks contains the search
     * string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.if page<1, 1st page is
     *                   returned, if page>total pages,last page is returned
     * @return list of slides whose text blocks contains the search string in the
     *         requested page.
     */
    @Override
    public Page<Slide> searchInSlideTexts(String searchTerm, int page) {
        if (page < 1) {
            page = 1;
        }
        Pageable requestedPageForSlideText = PageRequest.of(page - 1, pageSize);
        Page<Slide> slidetextPage = textContentBlockRepo.findWithNameOrDescription(requestedPageForSlideText,
                searchTerm);
        int totalSlideTextPage = slidetextPage.getTotalPages();
        /* if page>total pages,last page is returned */
        if (page > totalSlideTextPage) {
            totalSlideTextPage = totalSlideTextPage == 0 ? 1 : totalSlideTextPage;
            requestedPageForSlideText = PageRequest.of(totalSlideTextPage - 1, pageSize);
            slidetextPage = textContentBlockRepo.findWithNameOrDescription(requestedPageForSlideText, searchTerm);
        }
        return slidetextPage;
    }

    /**
     * Method to return total count of spaces whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     */
    @Override
    public int getTotalSpaceCount(String searchTerm) {
        int totalSpaceCount = spaceManager.findByNameOrDescription(searchTerm);
        return totalSpaceCount;
    }

    /**
     * Method to return total count of modules whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     */
    @Override
    public int getTotalModuleCount(String searchTerm) {
        int totalModuleCount = moduleManager.findByNameOrDescription(searchTerm);
        return totalModuleCount;
    }
    
    /**
     * Method to return total count of slides whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     */
    @Override
    public int getTotalSlideCount(String searchTerm) {
        int totalSlideCount = slideManager.findByNameOrDescription(searchTerm);
        return totalSlideCount;
    }
    
    /**
     * Method to return total count of slides whose whose text blocks contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     */
    @Override
    public int getTotalSlideTextCount(String searchTerm) {
        List<Slide> totalSlideTextList = textContentBlockRepo.findWithNameOrDescription(searchTerm);
        return totalSlideTextList.size();
    }
}
