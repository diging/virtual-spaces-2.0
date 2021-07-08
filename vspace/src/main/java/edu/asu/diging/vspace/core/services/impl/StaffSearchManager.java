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
     * Method to return the requested spaces whose name or description contains the search string
     * 
     * @param pageNo. if pageNo<1, 1st page is returned, if pageNo>total pages,last
     *                page is returned
     * @return list of spaces whose name or description contains the search string in the requested pageNo and requested order.
     */

    @Override
    public HashSet<IVSpaceElement> searchSpaces(String search, int page, long totalSpacePage, String tab,
            StringBuilder strSpacePageNo) {
        int spacePageNo;
        try {
            spacePageNo = validatePageNumber(page, totalSpacePage);
        } catch (NumberFormatException numberFormatException) {
            spacePageNo = 1;
        }
        // if it is not the current tab then reset the page number to 1.
        if (!tab.trim().equals("space")) {
            spacePageNo = 1;
        }
        strSpacePageNo.setLength(0);
        strSpacePageNo.append(String.valueOf(spacePageNo));
        Pageable requestedPageForSpace = PageRequest.of(spacePageNo - 1, pageSize);
        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements.addAll(spaceManager.findInNameOrDescription(requestedPageForSpace, search).getContent());
        return resultElements;
    }
    /**
     * Method to return the requested modules whose name or description contains the search string
     * 
     * @param pageNo. if pageNo<1, 1st page is returned, if pageNo>total pages,last
     *                page is returned
     * @return list of modules whose name or description contains the search string in the requested pageNo and requested order.
     */
    @Override
    public HashSet<IVSpaceElement> searchModules(String search, int page, long totalModulePage, String tab,
            StringBuilder strModulePageNo) {
        int modulePageNo;
        try {
            modulePageNo = validatePageNumber(page, totalModulePage);
        } catch (NumberFormatException numberFormatException) {
            modulePageNo = 1;
        }
        // if it is not the current tab then reset the page number to 1.
        if (!tab.trim().equals("module")) {
            modulePageNo = 1;
        }
        strModulePageNo.setLength(0);
        strModulePageNo.append(String.valueOf(modulePageNo));
        Pageable requestedPageForModule = PageRequest.of(modulePageNo - 1, pageSize);
        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements.addAll(moduleManager.findInNameOrDescription(requestedPageForModule, search).getContent());
        return resultElements;
    }
    /**
     * Method to return the requested slides whose name or description contains the search string
     * 
     * @param pageNo. if pageNo<1, 1st page is returned, if pageNo>total pages,last
     *                page is returned
     * @return list of slides whose name or description contains the search string in the requested pageNo and requested order.
     */
    @Override
    public HashSet<IVSpaceElement> searchSlides(String search, int page, long totalSlidePage, String tab,
            StringBuilder strSlidePageNo) {
        int slidePageNo;
        try {
            slidePageNo = validatePageNumber(page, totalSlidePage);
        } catch (NumberFormatException numberFormatException) {
            slidePageNo = 1;
        }
        // if it is not the current tab then reset the page number to 1.
        if (!tab.trim().equals("slide")) {
            slidePageNo = 1;
        }
        strSlidePageNo.setLength(0);
        strSlidePageNo.append(String.valueOf(slidePageNo));
        Pageable requestedPageForSlide = PageRequest.of(slidePageNo - 1, pageSize);
        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements.addAll(slideManager.findInNameOrDescription(requestedPageForSlide, search).getContent());
        return resultElements;
    }
    
    /**
     * Method to return the requested slides whose text blocks contains the search string
     * 
     * @param pageNo. if pageNo<1, 1st page is returned, if pageNo>total pages,last
     *                page is returned
     * @return list of slides whose text blocks contains the search string in the requested pageNo and requested order.
     */
    @Override
    public HashSet<IVSpaceElement> searchSlideTexts(String search, int page, long totalSlideTextPage, String tab,
            StringBuilder strSlideTextPageNo) {
        int slideTextPageNo;
        try {
            slideTextPageNo = validatePageNumber(page, totalSlideTextPage);
        } catch (NumberFormatException numberFormatException) {
            slideTextPageNo = 1;
        }
        // if it is not the current tab then reset the page number to 1.
        if (!tab.trim().equals("slideText")) {
            slideTextPageNo = 1;
        }
        strSlideTextPageNo.setLength(0);
        strSlideTextPageNo.append(String.valueOf(slideTextPageNo));
        Pageable requestedPageForSlideText = PageRequest.of(slideTextPageNo - 1, pageSize);
        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements.addAll(textContentBlockRepo.findInNameOrDescription(requestedPageForSlideText, search).getContent());
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
     * Method to return the total pages sufficient to display all spaces/modules/slides/Slides text block
     * 
     * @return totalPages required to display all spaces/modules/slides/Slides text block in DB
     */

    @Override
    public long getTotalPages(long count) {
        return (count % pageSize == 0) ? count / pageSize : (count / pageSize) + 1;
    }

    /**
     * Method to return the total count of module whose name or description
     * contains search string
     * 
     * @return total count of module whose name or description contains search
     *         string in DB
     */
    @Override
    public long getCountOfSearchedModule(String search) {
        return moduleManager.findInNameOrDescription(search);
    }

    /**
     * Method to return the total count of space whose name or description contains
     * search string
     * 
     * @return total count of space whose name or description contains search
     *         string in DB
     */
    @Override
    public long getCountOfSearchedSpace(String search) {
        return spaceManager.findInNameOrDescription(search);
    }

    /**
     * Method to return the total count of slide whose name or description contains
     * search string
     * 
     * @return total count of slide whose name or description contains search
     *         string in DB
     */
    @Override
    public long getCountOfSearchedSlide(String search) {
        return slideManager.findInNameOrDescription(search);
    }

    /**
     * Method to return the total count of slide whose text blocks contains search
     * string
     * 
     * @return total count of slide whose text blocks contains search string in DB
     */
    @Override
    public long getCountOfSearchedSlideText(String search) {
        return textContentBlockRepo.findInNameOrDescription(search);
    }

}
