package edu.asu.diging.vspace.core.services;

import java.util.List;

import org.springframework.data.domain.Page;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.impl.model.StaffSearchModuleResults;
import edu.asu.diging.vspace.core.services.impl.model.StaffSearchSlideResults;
import edu.asu.diging.vspace.core.services.impl.model.StaffSearchSlideTextBlockResults;
import edu.asu.diging.vspace.core.services.impl.model.StaffSearchSpaceResults;

public interface ISearchManager {
    
    public Page<ISpace> searchInSpaces(String searchTerm, int page);

    /**
     * Method to return the requested modules whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.
     * @return set of modules whose name or description contains the search string
     *         in the requested page.
     */
    Page<IModule> searchInModules(String searchTerm, int page);

    /**
     * Method to return the requested slides whose name or description contains the
     * search string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.
     * @return set of slides whose name or description contains the search string in
     *         the requested page.
     */
    Page<ISlide> searchInSlides(String searchTerm, int page);

    /**
     * Method to return the requested slides whose text blocks contains the search
     * string
     * 
     * @param searchTerm string which has been searched
     * @param page.      The page number starts from 1.
     * @return list of slides whose text blocks contains the search string in the
     *         requested page.
     */
    Page<ISlide> searchInSlideTexts(String searchTerm, int page);

    StaffSearchModuleResults getStaffSearchModuleResults(List<IModule> moduleList);

    StaffSearchSlideResults getStaffSearchSlideResults(List<ISlide> slideList);

    StaffSearchSlideTextBlockResults getStaffSearchSlideTextBlockResults(List<ISlide> slideTextList, String searchTerm);

    StaffSearchSpaceResults getStaffSearchSpaceResults(List<ISpace> spaceList);
    

}
