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

    SearchModuleResults convertToSearchModuleResults(List<IModule> moduleList);

    SearchSlideResults convertToSearchSlideResults(List<ISlide> slideList);

    SearchSlideTextBlockResults convertToSearchSlideTextBlockResults(List<ISlide> slideTextList, String searchTerm);

    SearchSpaceResults convertToSearchSpaceResults(List<ISpace> spaceList);

    SearchSpaceResults searchForSpace(String spacePagenum, String searchTerm);

    SearchModuleResults searchForModule(String modulePagenum, String searchTerm);

    SearchSlideResults searchForSlide(String slidePagenum, String searchTerm);

    SearchSlideTextBlockResults searchForSlideText(String slideTextPagenum, String searchTerm);
}
