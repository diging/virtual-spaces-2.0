package edu.asu.diging.vspace.core.services.impl;

import java.util.HashSet;
import java.util.LinkedHashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Service
public class StaffSearchManager implements IStaffSearchManager {

    // @Value("${page_size}")
    private int pageSize = 3;

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
     * @param search  string which has been searched
     * @param page. if page<1, 1st page is returned, if page>total pages,last
     *                page is returned
     * @return set of spaces whose name or description contains the search string
     *         in the requested page.
     */

    @Override
    public HashSet<IVSpaceElement> searchInSpaces(String search, int page) {
        int spacePageNo;
        try {
            spacePageNo = validatePageNumber(page, getTotalSpacePages(search));
        } catch (NumberFormatException numberFormatException) {
            spacePageNo = 1;
        }
        Pageable requestedPageForSpace = PageRequest.of(spacePageNo - 1, pageSize);
        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements.addAll(spaceManager.findByNameOrDescription(requestedPageForSpace, search).getContent());
        return resultElements;
    }

    /**
     * Method to return the requested modules whose name or description contains the
     * search string
     * 
     * @param pageNo. if page<1, 1st page is returned, if page>total pages,last
     *                page is returned
     * @return set of modules whose name or description contains the search string
     *         in the requested page.
     */
    @Override
    public HashSet<IVSpaceElement> searchInModules(String search, int page) {
        int modulePageNo;
        try {
            modulePageNo = validatePageNumber(page, getTotalModulePages(search));
        } catch (NumberFormatException numberFormatException) {
            modulePageNo = 1;
        }
        Pageable requestedPageForModule = PageRequest.of(modulePageNo - 1, pageSize);
        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements.addAll(moduleManager.findByNameOrDescription(requestedPageForModule, search).getContent());
        return resultElements;
    }

    /**
     * Method to return the requested slides whose name or description contains the
     * search string
     * @param search  string which has been searched
     * @param page. if page<1, 1st page is returned, if page>total pages,last
     *                page is returned
     * @return set of slides whose name or description contains the search string
     *         in the requested page.
     */
    @Override
    public HashSet<IVSpaceElement> searchInSlides(String search, int page) {
        int slidePageNo;
        try {
            slidePageNo = validatePageNumber(page, getTotalSlidePages(search));
        } catch (NumberFormatException numberFormatException) {
            slidePageNo = 1;
        }
        Pageable requestedPageForSlide = PageRequest.of(slidePageNo - 1, pageSize);
        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements.addAll(slideManager.findByNameOrDescription(requestedPageForSlide, search).getContent());
        return resultElements;
    }

    /**
     * Method to return the requested slides whose text blocks contains the search
     * string
     * @param search  string which has been searched
     * @param page. if page<1, 1st page is returned, if page>total pages,last
     *                page is returned
     * @return list of slides whose text blocks contains the search string in the
     *         requested page.
     */
    @Override
    public HashSet<IVSpaceElement> searchInSlideTexts(String search, int page) {
        int slideTextPageNo;
        try {
            slideTextPageNo = validatePageNumber(page, getTotalSlideTextPages(search));
        } catch (NumberFormatException numberFormatException) {
            slideTextPageNo = 1;
        }
        Pageable requestedPageForSlideText = PageRequest.of(slideTextPageNo - 1, pageSize);
        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements
                .addAll(textContentBlockRepo.findWithNameOrDescription(requestedPageForSlideText, search).getContent());
        return resultElements;
    }

    /**
     * Method to return page number after validation
     * 
     * @param pageNo page provided by calling method
     * @return 1 if pageNo less than 1 and lastPage if pageNo greater than
     *         totalPages.
     */
    @Override
    public int validatePageNumber(int pageNo, long totalPages) {
        if (pageNo < 1) {
            return 1;
        } else if (pageNo > totalPages) {
            return (totalPages == 0) ? 1 : (int) totalPages;
        }
        return pageNo;
    }

    /**
     * Method to return the total pages sufficient to display all
     * spaces whose name or description matches with the search string
     * 
     * @return totalPages required to display all spaces
     *  whose name or description matches with the search string in DB
     */
    @Override
    public long getTotalSpacePages(String search) {
        Pageable requestedPageForSpace = PageRequest.of(0, pageSize);
        return spaceManager.findByNameOrDescription(requestedPageForSpace, search).getTotalPages();
    }
    /**
     * Method to return the total pages sufficient to display all
     * modules whose name or description matches with the search string
     * 
     * @return totalPages required to display all modules
     *  whose name or description matches with the search string in DB
     */
    @Override
    public long getTotalModulePages(String search) {
        Pageable requestedPageForModule = PageRequest.of(0, pageSize);
        return moduleManager.findByNameOrDescription(requestedPageForModule, search).getTotalPages();
    }
    /**
     * Method to return the total pages sufficient to display all
     * slides whose name or description matches with the search string
     * 
     * @return totalPages required to display all slides
     *  whose text blocks contains the search string in DB
     */
    @Override
    public long getTotalSlidePages(String search) {
        Pageable requestedPageForSlide = PageRequest.of(0, pageSize);
        return slideManager.findByNameOrDescription(requestedPageForSlide, search).getTotalPages();
    }
    /**
     * Method to return the total pages sufficient to display all
     * slides whose text blocks contains the search string
     * 
     * @return totalPages required to display all slides
     *   whose text blocks contains the search string in DB
     */
    @Override
    public long getTotalSlideTextPages(String search) {
        Pageable requestedPageForSlideText = PageRequest.of(0, pageSize);
        return textContentBlockRepo.findWithNameOrDescription(requestedPageForSlideText, search).getTotalPages();
    }
}
