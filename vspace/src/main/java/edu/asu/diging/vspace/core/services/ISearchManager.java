package edu.asu.diging.vspace.core.services;

import java.util.List;

import org.springframework.data.domain.Page;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.impl.model.SearchModuleResults;
import edu.asu.diging.vspace.core.services.impl.model.SearchSlideResults;
import edu.asu.diging.vspace.core.services.impl.model.SearchSlideTextBlockResults;
import edu.asu.diging.vspace.core.services.impl.model.SearchSpaceResults;

public interface ISearchManager {
    
    public Page<ISpace> paginationInSpaces(String searchTerm, int page);

    /**
     * Method to return the requested modules whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.
     * @return set of modules whose name or description contains the search string
     *         in the requested page.
     */
    Page<IModule> paginationInModules(String searchTerm, int page);

    /**
     * Method to return the requested slides whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.
     * @return set of slides whose name or description contains the search string in
     *         the requested page.
     */
    Page<ISlide> paginationInSlides(String searchTerm, int page);

    /**
     * Method to return the requested slides whose text blocks contains the search
     * string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.
     * @return list of slides whose text blocks contains the search string in the
     *         requested page.
     */
    Page<ISlide> paginationInSlideTexts(String searchTerm, int page);

    SearchModuleResults getSearchModuleResults(List<IModule> moduleList);

    SearchSlideResults getSearchSlideResults(List<ISlide> slideList);

    SearchSlideTextBlockResults getSearchSlideTextBlockResults(List<ISlide> slideTextList, String searchTerm);

    SearchSpaceResults getSearchSpaceResults(List<ISpace> spaceList);
    

}
