package edu.asu.diging.vspace.core.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ISearchManager;

public abstract class SearchManager implements ISearchManager {
    
    @Value("${page_size}")
    private int pageSize;
    
    /**
     * Method to return the requested spaces whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.
     * @return set of spaces whose name or description contains the search string in
     *         the requested page.
     */
    @Override
    public Page<ISpace> searchInSpaces(String searchTerm, int page) {
        if (page < 1) {
            page = 1;
        }
        Pageable requestedPageForSpace = PageRequest.of(page - 1, pageSize);       
        Page<ISpace> spacePage =  spaceSearch(requestedPageForSpace, searchTerm);
        
        int totalSpacePage = spacePage.getTotalPages();
        /* if page>total pages,last page is returned */
        if (page > totalSpacePage) {
            totalSpacePage = totalSpacePage == 0 ? 1 : totalSpacePage;
            requestedPageForSpace = PageRequest.of(totalSpacePage - 1, pageSize);   
            spacePage = spaceSearch(requestedPageForSpace,  searchTerm);
        }
        return spacePage;
    }
    
    
    protected abstract  Page<ISpace> spaceSearch(Pageable requestedPageForSpace ,  String searchTerm);
        
    

}
