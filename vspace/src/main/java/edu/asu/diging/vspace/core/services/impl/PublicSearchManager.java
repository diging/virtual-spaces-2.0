package edu.asu.diging.vspace.core.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.IPublicSearchManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Service
public class PublicSearchManager implements IPublicSearchManager{

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
    public Page<ISpace> searchInSpaces(String searchTerm, int page) {
        if (page < 1) {
            page = 1;
        }
        Pageable requestedPageForSpace = PageRequest.of(page - 1, pageSize);
        Page<ISpace> spacePage = spaceManager.findBySpaceStatusAndNameOrDescription(requestedPageForSpace, SpaceStatus.PUBLISHED, searchTerm);
        int totalSpacePage = spacePage.getTotalPages();
        /* if page>total pages,last page is returned */
        if (page > totalSpacePage) {
            totalSpacePage = totalSpacePage == 0 ? 1 : totalSpacePage;
            requestedPageForSpace = PageRequest.of(totalSpacePage - 1, pageSize);
            spacePage = spaceManager.findBySpaceStatusAndNameOrDescription(requestedPageForSpace, SpaceStatus.PUBLISHED, searchTerm);
        }
        return spacePage;
    }
}