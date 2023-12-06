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
    
    public Page<ISpace> searchSpacesAndPaginate(String searchTerm, int page);

    /**
     * Method to return the requested modules whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.
     * @return set of modules whose name or description contains the search string
     *         in the requested page.
     */
    Page<IModule> searchModulesAndPaginate(String searchTerm, int page);

    /**
     * Method to return the requested slides whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.
     * @return set of slides whose name or description contains the search string in
     *         the requested page.
     */
    Page<ISlide> searchSlidesAndPaginate(String searchTerm, int page);

    /**
     * Method to return the requested slides whose text blocks contains the search
     * string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.
     * @return list of slides whose text blocks contains the search string in the
     *         requested page.
     */
    Page<ISlide> searchSlideTextsAndPaginate(String searchTerm, int page);
    
    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) and return the module corresponding to the page number
     * specified in the input parameter(spacePagenum) whose name or description
     * contains the search string. This also filters modules which are linked to the spaces.
     * 
     * @param modulePagenum current page number sent as request parameter in the
     *                      URL.
     * @param searchTerm    This is the search string which is being searched.
     */
    SearchModuleResults searchForModule(String modulePagenum, String searchTerm);

    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the slides corresponding to
     * the page number specified in the input parameter(slidePagenum) whose name or
     * description contains the search string. This also filters Slides from modules 
     * which are linked to the spaces.
     * 
     * @param slidePagenum current page number sent as request parameter in the URL.
     * @param searchTerm   This is the search string which is being searched.
     */
    SearchSlideResults searchForSlide(String slidePagenum, String searchTerm);
    
    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the spaces corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string. This also filters Spaces from modules 
     * which are linked to the spaces.
     * 
     * @param spacePagenum current page number sent as request parameter in the URL.
     * @param searchTerm   This is the search string which is being searched.
     */
    SearchSpaceResults searchForSpace(String spacePagenum, String searchTerm);

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) and return the slides corresponding to the page number
     * specified in the input parameter(spacePagenum) whose text block contains the
     * search string. This also filters Slides from modules which are linked to the spaces.
     * 
     * @param slideTextPagenum current page number sent as request parameter in the
     *                         URL.
     * @param searchTerm       This is the search string which is being searched.
     */
    SearchSlideTextBlockResults searchForSlideText(String slideTextPagenum, String searchTerm);
    
    /**
     * This method is used to convert the searched results specified in the input
     * parameter(spacePage) into SearchSpaceResults object and return the published spaces.
     * @param spacePage  set of spaces whose name or description contains the search string
     */
    SearchSpaceResults convertToSearchSpaceResults(Page<ISpace> spacePage);
    
    
}
