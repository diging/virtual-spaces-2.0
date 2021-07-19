package edu.asu.diging.vspace.core.services.impl;

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
        int spacePageNo = validatePageNumber(page, getTotalSpacePages(searchTerm));
        Pageable requestedPageForSpace = PageRequest.of(spacePageNo - 1, pageSize);
        return spaceManager.findByNameOrDescription(requestedPageForSpace, searchTerm);
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
        int modulePageNo = validatePageNumber(page, getTotalModulePages(searchTerm));
        Pageable requestedPageForModule = PageRequest.of(modulePageNo - 1, pageSize);
        return moduleManager.findByNameOrDescription(requestedPageForModule, searchTerm);
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
        int slidePageNo = validatePageNumber(page, getTotalSlidePages(searchTerm));
        Pageable requestedPageForSlide = PageRequest.of(slidePageNo - 1, pageSize);
        return slideManager.findByNameOrDescription(requestedPageForSlide, searchTerm);
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
        int slideTextPageNo = validatePageNumber(page, getTotalSlideTextPages(searchTerm));
        Pageable requestedPageForSlideText = PageRequest.of(slideTextPageNo - 1, pageSize);
        return textContentBlockRepo.findWithNameOrDescription(requestedPageForSlideText, searchTerm);
    }

    /**
     * Method to return page number after validation
     * 
     * @param pageNo page provided by calling method(Note: The page number starts
     *               from 1.)
     * @return 1 if pageNo less than 1 and lastPage if pageNo greater than
     *         totalPages.
     */
    private int validatePageNumber(int pageNo, long totalPages) {
        if (pageNo < 1) {
            return 1;
        } else if (pageNo > totalPages) {
            return (totalPages == 0) ? 1 : (int) totalPages;
        }
        return pageNo;
    }

    /**
     * Method to return the total pages sufficient to display all spaces whose name
     * or description matches with the search string
     * 
     * @param searchTerm string which has been searched
     * @return totalPages required to display all spaces whose name or description
     *         matches with the search string in DB
     */
    @Override
    public long getTotalSpacePages(String searchTerm) {
        Pageable requestedPageForSpace = PageRequest.of(0, pageSize);
        return spaceManager.findByNameOrDescription(requestedPageForSpace, searchTerm).getTotalPages();
    }

    /**
     * Method to return the total pages sufficient to display all modules whose name
     * or description matches with the search string
     *
     * @param searchTerm string which has been searched
     * @return totalPages required to display all modules whose name or description
     *         matches with the search string in DB
     */
    @Override
    public long getTotalModulePages(String searchTerm) {
        Pageable requestedPageForModule = PageRequest.of(0, pageSize);
        return moduleManager.findByNameOrDescription(requestedPageForModule, searchTerm).getTotalPages();
    }

    /**
     * Method to return the total pages sufficient to display all slides whose name
     * or description matches with the search string
     * 
     * @param searchTerm string which has been searched
     * @return totalPages required to display all slides whose text blocks contains
     *         the search string in DB
     */
    @Override
    public long getTotalSlidePages(String searchTerm) {
        Pageable requestedPageForSlide = PageRequest.of(0, pageSize);
        return slideManager.findByNameOrDescription(requestedPageForSlide, searchTerm).getTotalPages();
    }

    /**
     * Method to return the total pages sufficient to display all slides whose text
     * blocks contains the search string
     * 
     * @param searchTerm string which has been searched
     * @return totalPages required to display all slides whose text blocks contains
     *         the search string in DB
     */
    @Override
    public long getTotalSlideTextPages(String searchTerm) {
        Pageable requestedPageForSlideText = PageRequest.of(0, pageSize);
        return textContentBlockRepo.findWithNameOrDescription(requestedPageForSlideText, searchTerm).getTotalPages();
    }
}
